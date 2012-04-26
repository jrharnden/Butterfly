package networking;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;

public class ProxyHttpRequestEncoder extends HttpRequestEncoder {
    private final HttpRelayingHandler relayingHandler;
    private final HttpRequestFilter requestFilter;

    public ProxyHttpRequestEncoder(final HttpRelayingHandler handler, final HttpRequestFilter requestFilter) {
        this.relayingHandler = handler;
        this.requestFilter = requestFilter;
    }

    @Override
    protected Object encode(final ChannelHandlerContext ctx,
        final Channel channel, final Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest) msg;
            this.relayingHandler.requestEncoded(request);

            final HttpRequest toSend = ProxyUtils.copyHttpRequest(request);
            
            if (this.requestFilter != null) {
                this.requestFilter.filter(toSend);
            }
            return super.encode(ctx, channel, toSend);
        }
        return super.encode(ctx, channel, msg);
    }
}
