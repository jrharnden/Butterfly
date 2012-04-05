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
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;
import org.jboss.netty.handler.codec.http.HttpResponseDecoder;


public class ProxyClientHandler extends SimpleChannelUpstreamHandler{
	private static final int DEFAULT_HOST_PORT = 80;
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
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		final Channel clientChannel = e.getChannel();
		//clientChannel.setReadable();
		
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception{
		final Channel clientChannel = e.getChannel();
		final HttpRequest msg = (DefaultHttpRequest) e.getMessage();
		clientChannel.setReadable(false);
		
		if (this.hostChannel == null) {
			String host = msg.getHeader("Host");
			this.clientbs.getPipeline().addLast("decoder", new HttpResponseDecoder());
			this.clientbs.getPipeline().addLast("encoder", new HttpRequestEncoder());
			this.clientbs.getPipeline().addLast("aggregator", new HttpChunkAggregator(1048576));
			this.clientbs.getPipeline().addLast("handler", new ProxyHostHandler(clientChannel));
			ChannelFuture cf = this.clientbs.connect(new InetSocketAddress(host, DEFAULT_HOST_PORT));
			this.hostChannel = cf.getChannel();
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture f)throws Exception {
					if (f.isSuccess()) {
						clientChannel.setReadable(true);
						synchronized (trafficLock) {
							hostChannel.write(msg);
							if (!hostChannel.isWritable()) {
								clientChannel.setReadable(false);
							}
						}
						ProxyLog.write(clientChannel, hostChannel, msg);
					}
					else {
						clientChannel.close();
					}
				}
			});
		}
		else {
			synchronized (this.trafficLock) {
				this.hostChannel.write(msg);
				if (!this.hostChannel.isWritable()) {
					clientChannel.setReadable(false);
				}
			}
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
		if (this.hostChannel != null) {
			ProxyClientHandler.closeOnFlush(this.hostChannel);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception{
		e.getCause().printStackTrace();
		ProxyClientHandler.closeOnFlush(e.getChannel());
	}
	
	/****************************************************************
	 * Static Methods
	 ****************************************************************/
	static void closeOnFlush(Channel ch) {
		if (ch.isConnected()) {
			ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	/****************************************************************
	 * ProxyServerHandler Class
	 ****************************************************************/
	private class ProxyHostHandler extends SimpleChannelUpstreamHandler {
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
		public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception{
			DefaultHttpResponse msg = (DefaultHttpResponse) e.getMessage();
			synchronized(trafficLock) {
				this.clientChannel.write(msg);
				ProxyLog.write(this.clientChannel, e.getChannel(), msg);
				if (!this.clientChannel.isWritable()) {
					e.getChannel().setReadable(false);
				}
			}
		}
		
		@Override
		public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			synchronized(trafficLock) {
				if (e.getChannel().isWritable()) {
					this.clientChannel.setReadable(false);
				}
			}
		}
		
		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			ProxyClientHandler.closeOnFlush(this.clientChannel);
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
			e.getCause().printStackTrace();
			ProxyClientHandler.closeOnFlush(this.clientChannel);
		}
		/******************************************************************
		 * Private Methods
		 ******************************************************************/
	}
}
