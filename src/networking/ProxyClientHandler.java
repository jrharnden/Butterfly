package networking;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;
import org.jboss.netty.handler.codec.http.HttpResponseDecoder;


public class ProxyClientHandler extends SimpleChannelHandler{
	private static final int DEFAULT_HOST_PORT = 80;
	private static final int DEFAULT_SSL_PORT = 443;
	/****************************************************************
	 * Fields
	 ****************************************************************/
	protected final Object trafficLock = new Object();
	private ClientSocketChannelFactory clientFactory;
	private ClientBootstrap clientbs;
	private volatile Channel hostChannel;
	
	/****************************************************************
	 * Constructors
	 ****************************************************************/
	public ProxyClientHandler(ClientSocketChannelFactory cf) {
		this.clientFactory = cf;
		this.hostChannel = null;
		this.clientbs = new ClientBootstrap(this.clientFactory);
	}
	
	/****************************************************************
	 * Public Methods
	 ****************************************************************/
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx ,ChannelStateEvent e) throws Exception {
		window.allChannels.add(e.getChannel());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		final Channel clientChannel = e.getChannel();
		final HttpRequest msg = (DefaultHttpRequest) e.getMessage();
		clientChannel.setReadable(false);
		
		if (this.hostChannel == null) {
			String host = msg.getHeader("Host");
			int port = DEFAULT_HOST_PORT;
			String[] host_parsed = host.split(":");
			if (host_parsed.length > 1) {
				port = Integer.parseInt(host_parsed[1]);
				host = host_parsed[0];
			}
			this.clientbs.getPipeline().addLast("decoder", new HttpResponseDecoder());
			this.clientbs.getPipeline().addLast("encoder", new HttpRequestEncoder());
			this.clientbs.getPipeline().addLast("inflater", new HttpContentDecompressor());
			this.clientbs.getPipeline().addLast("aggregator", new HttpChunkAggregator(1048576));
			this.clientbs.getPipeline().addLast("filter", new FilterResponseHandler());
			this.clientbs.getPipeline().addLast("handler", new ProxyHostHandler(clientChannel));
			ChannelFuture f = null;
			if (msg.getMethod().compareTo(HttpMethod.CONNECT)==0) {
				f = this.clientbs.connect(new InetSocketAddress(host, DEFAULT_SSL_PORT));
			} else {
				f = this.clientbs.connect(new InetSocketAddress(host, port));
			}
			if (f != null) {
				this.hostChannel = f.getChannel();
				f.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture f)throws Exception {
						if (f.isSuccess()) {
							clientChannel.setReadable(true);
							ProxyClientHandler.write(clientChannel, f.getChannel(), msg, trafficLock, msg.isKeepAlive());
							ProxyLog.write(clientChannel, hostChannel, msg);
						}
						else {
							//window.allChannels.remove(e.getChannel());
							if (clientChannel.isOpen()) {
								clientChannel.close();	
							}
						}
					}
				});
			}
		}
		else {
			ProxyClientHandler.write(clientChannel, this.hostChannel, msg, trafficLock, msg.isKeepAlive());
			ProxyLog.write(clientChannel, this.hostChannel, msg);
		}
	}
	
	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		synchronized (this.trafficLock) {
			if (e.getChannel().isWritable()&&this.hostChannel!=null) {
				this.hostChannel.setReadable(true);
			}
		}
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception{
		window.allChannels.remove(e.getChannel());
		synchronized (this.trafficLock) {
		if (this.hostChannel != null&& this.hostChannel.isOpen()) {
			ProxyClientHandler.closeOnFlush(this.hostChannel);
		}}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception{
		e.getCause().printStackTrace();
		//window.allChannels.remove(e.getChannel());
		ProxyClientHandler.closeOnFlush(e.getChannel());
	}
	
	@Override
	public void disconnectRequested(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		//window.allChannels.remove(e.getChannel());
		e.getChannel().write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		if (this.hostChannel != null) {
			ProxyClientHandler.closeOnFlush(this.hostChannel);
		}
	}
	
	/****************************************************************
	 * Static Methods
	 ****************************************************************/
	static void closeOnFlush(Channel ch) {
		if (ch.isConnected()) {
			//window.allChannels.remove(ch);
			ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	static void write(Channel in, Channel to, HttpMessage msg, Object trafficLock, boolean keepAlive) {
		if (to.isOpen()) {
			synchronized (trafficLock) {
				// check if the message is to stay alive or not
				if (keepAlive) {
					to.write(msg);
				} else {
					to.write(msg).addListener(ChannelFutureListener.CLOSE);
				}
				// check if channel is saturated
				if (!in.isWritable()&&in.isOpen()) {
					in.setReadable(false);
				}
			}// end synchronized(this.trafficeLock)
		}// end if (to.isOpen())
	}
	
	/****************************************************************
	 * ProxyServerHandler Class
	 ****************************************************************/
	private class ProxyHostHandler extends SimpleChannelHandler {
		/******************************************************************
		 * Fields
		 ******************************************************************/
		private final Channel clientChannel;
		
		/******************************************************************
		 * Constructors
		 ******************************************************************/
		public ProxyHostHandler(Channel ch) {
			this.clientChannel = ch;
		}

		/******************************************************************
		 * Public Methods 
		 ******************************************************************/
		@Override
		public void channelOpen(ChannelHandlerContext ctx ,ChannelStateEvent e) throws Exception {
			window.allChannels.add(e.getChannel());
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception{
			DefaultHttpResponse msg = (DefaultHttpResponse) e.getMessage();
			ProxyClientHandler.write(e.getChannel(), this.clientChannel, msg, trafficLock, msg.isKeepAlive());
			ProxyLog.write(this.clientChannel, e.getChannel(), msg);
		}
		
		@Override
		public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			synchronized(trafficLock) {
				if (e.getChannel().isWritable()) {
					this.clientChannel.setReadable(true);
				}
			}
		}
		
		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			window.allChannels.remove(e.getChannel());
			if (this.clientChannel != null && this.clientChannel.isOpen()) {
				ProxyClientHandler.closeOnFlush(this.clientChannel);
			}		
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
			e.getCause().printStackTrace();
			//window.allChannels.remove(e.getChannel());
			ProxyClientHandler.closeOnFlush(this.clientChannel);
		}
		
		@Override
		public void disconnectRequested(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			//window.allChannels.remove(e.getChannel());
			e.getChannel().write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
			ProxyClientHandler.closeOnFlush(this.clientChannel);
		}
		/******************************************************************
		 * Private Methods
		 ******************************************************************/
	}
}
