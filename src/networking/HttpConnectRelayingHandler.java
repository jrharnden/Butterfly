package networking;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.group.ChannelGroup;

@Sharable
public class HttpConnectRelayingHandler extends SimpleChannelUpstreamHandler {

	private final Channel relayChannel;

	private final ChannelGroup channelGroup;

	public HttpConnectRelayingHandler(final Channel relayChannel, final ChannelGroup channelGroup) {
		this.relayChannel = relayChannel;
		this.channelGroup = channelGroup;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		final ChannelBuffer msg = (ChannelBuffer) e.getMessage();
		if (relayChannel.isConnected()) {
			final ChannelFutureListener logListener = new ChannelFutureListener() {
				public void operationComplete(final ChannelFuture future) throws Exception {

				}
			};
			relayChannel.write(msg).addListener(logListener);
		} else {
			ProxyUtils.closeOnFlush(e.getChannel());
		}
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
		ProxyUtils.closeOnFlush(this.relayChannel);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
		ProxyUtils.closeOnFlush(e.getChannel());
	}
}
