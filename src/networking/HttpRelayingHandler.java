package networking;

import java.util.LinkedList;
import java.util.Queue;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class HttpRelayingHandler extends SimpleChannelUpstreamHandler {
    private volatile boolean readingChunks;
    private final Channel browserToProxyChannel;
    private final ChannelGroup channelGroup;
    private final HttpFilter httpFilter;
    private HttpResponse originalHttpResponse;
    private HttpRequest currentHttpRequest;
    private final RelayListener relayListener;
    private final String hostAndPort;
    private boolean closeEndsResponseBody;

    public HttpRelayingHandler(final Channel browserToProxyChannel, final ChannelGroup channelGroup, final HttpFilter filter, final RelayListener relayListener, final String hostAndPort) {
        this.browserToProxyChannel = browserToProxyChannel;
        this.channelGroup = channelGroup;
        this.httpFilter = filter;
        this.relayListener = relayListener;
        this.hostAndPort = hostAndPort;
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx,
        final MessageEvent me) throws Exception {
        
        final Object messageToWrite;

        final boolean writeEndBuffer;
        
        if (!readingChunks) {
            final HttpResponse hr = (HttpResponse) me.getMessage();

            originalHttpResponse = ProxyUtils.copyMutableResponseFields(hr, new DefaultHttpResponse(hr.getProtocolVersion(), hr.getStatus()));
            final HttpResponse response;

            final String te = hr.getHeader(HttpHeaders.Names.TRANSFER_ENCODING);
            if (ProxyUtils.isNotBlank(te) &&
                te.equalsIgnoreCase(HttpHeaders.Values.CHUNKED)) {
                if (hr.getProtocolVersion() != HttpVersion.HTTP_1_1) {
                    response = ProxyUtils.copyMutableResponseFields(hr, new DefaultHttpResponse(HttpVersion.HTTP_1_1, hr.getStatus()));
                    if (!response.containsHeader(HttpHeaders.Names.TRANSFER_ENCODING)) {
                        response.addHeader(HttpHeaders.Names.TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
                    }
                }
                else {
                    response = hr;
                }
            }
            else {
                response = hr;
            }

            if (response.isChunked()) {
                readingChunks = true;
                writeEndBuffer = false;
            }
            else {
                writeEndBuffer = true;
            }
            
            if (!this.requestQueue.isEmpty()) {
                this.currentHttpRequest = this.requestQueue.remove();
            } 
            
            messageToWrite = this.httpFilter.filterResponse(this.currentHttpRequest, response);
            
        } else {
            final HttpChunk chunk = (HttpChunk) me.getMessage();
            if (chunk.isLast()) {
                readingChunks = false;
                writeEndBuffer = true;
            }
            else {
                writeEndBuffer = false;
            }
            messageToWrite = chunk;
        }
        
        if (browserToProxyChannel.isConnected()) {
            final boolean closeRemote = shouldCloseRemoteConnection(this.currentHttpRequest, originalHttpResponse, messageToWrite);
            final boolean closePending = shouldCloseBrowserConnection(this.currentHttpRequest, originalHttpResponse, messageToWrite);
            
            final boolean wroteFullResponse = wroteFullResponse(originalHttpResponse, messageToWrite);
            
            if (closeRemote && closeEndsResponseBody(originalHttpResponse)) {
                this.closeEndsResponseBody = true;
            }
                
            ChannelFuture future =
                this.browserToProxyChannel.write(new ProxyHttpResponse(this.currentHttpRequest, originalHttpResponse, messageToWrite));

            if (writeEndBuffer) {
                future = browserToProxyChannel.write(ChannelBuffers.EMPTY_BUFFER);
            }

            if (wroteFullResponse) {
                future.addListener(new ChannelFutureListener() {
                    public void operationComplete(final ChannelFuture cf) throws Exception {
                        relayListener.onRelayHttpResponse(browserToProxyChannel, hostAndPort, currentHttpRequest);
                    }
                });
            }
            if (closeRemote) {
                future.addListener(new ChannelFutureListener() {
                    public void operationComplete(final ChannelFuture cf) throws Exception {
                        if (me.getChannel().isConnected()) {
                            me.getChannel().close();
                        }
                    }
                });
            }
            
            if (closePending) {
                future.addListener(new ChannelFutureListener() {
                    public void operationComplete(final ChannelFuture cf) throws Exception {
                        ProxyUtils.closeOnFlush(browserToProxyChannel);
                    }
                });
            }
            if (wroteFullResponse && (!closePending && !closeRemote)) {
                this.relayListener.onChannelAvailable(hostAndPort, Channels.succeededFuture(me.getChannel()));
            }
        }
        else {
            if (me.getChannel().isConnected()) {
                me.getChannel().close();
            }
        }
    }
    
    private boolean closeEndsResponseBody(final HttpResponse res) {
        final String cl = res.getHeader(HttpHeaders.Names.CONTENT_LENGTH);
        if (ProxyUtils.isNotBlank(cl)) {
            return false;
        }
        final String te = res.getHeader(HttpHeaders.Names.TRANSFER_ENCODING);
        if (ProxyUtils.isNotBlank(te) &&
            te.equalsIgnoreCase(HttpHeaders.Values.CHUNKED)) {
            return false;
        }
        return true;
    }

    private boolean wroteFullResponse(final HttpResponse res,
        final Object messageToWrite) {
        if (res.isChunked()) {
            if (messageToWrite instanceof HttpResponse) {
                return false;
            }
            return ProxyUtils.isLastChunk(messageToWrite);
        }
        return true;
    }

    private boolean shouldCloseBrowserConnection(final HttpRequest req,
        final HttpResponse res, final Object msg) {
        if (res.isChunked()) {
            if (msg != null) {
                if (!ProxyUtils.isLastChunk(msg)) {
                    return false;
                }
                else {
                }
            }
        }
        
        final String proxyConnectionKey = "Proxy-Connection";
        if (req.containsHeader(proxyConnectionKey)) {
            final String header = req.getHeader(proxyConnectionKey);
            req.removeHeader(proxyConnectionKey);
            if (req.getProtocolVersion() == HttpVersion.HTTP_1_1) {
                req.setHeader("Connection", header);
            }
        }
        
        if (!HttpHeaders.isKeepAlive(req)) {
            return true;
        }
        return false;
    }

    private boolean shouldCloseRemoteConnection(final HttpRequest req,
        final HttpResponse res, final Object msg) {
        if (res.isChunked()) {
            if (msg != null) {
                if (!ProxyUtils.isLastChunk(msg)) {
                    return false;
                }
            }
        }
        if (!HttpHeaders.isKeepAlive(req)) {
            return true;
        }
        if (!HttpHeaders.isKeepAlive(res)) {
            return true;
        }
        return false;
    }
    
    @Override
    public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent cse) throws Exception {
        final Channel ch = cse.getChannel();
        if (this.channelGroup != null) {
            this.channelGroup.add(ch);
        }
    }

    @Override
    public void channelClosed(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        final int unansweredRequests = this.requestQueue.size();
        this.relayListener.onRelayChannelClose(browserToProxyChannel, this.hostAndPort, unansweredRequests, this.closeEndsResponseBody);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
        if (e.getChannel().isConnected()) {
            ProxyUtils.closeOnFlush(e.getChannel());
        }
    }

    private final Queue<HttpRequest> requestQueue = new LinkedList<HttpRequest>();
    
    public void requestEncoded(final HttpRequest request) {
        this.requestQueue.add(request);
    }
    
}