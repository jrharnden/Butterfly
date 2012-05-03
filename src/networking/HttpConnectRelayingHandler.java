/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.group.ChannelGroup;

/**
* Class that simply relays traffic the channel this is connected to to
* another channel passed in to the constructor.
*/
@Sharable
public class HttpConnectRelayingHandler extends SimpleChannelUpstreamHandler {
    /**
     * The channel to relay to. This could be a connection from the browser
     * to the proxy or it could be a connection from the proxy to an external
     * site.
     */
	private final Channel		relayChannel;
	private final ChannelGroup	channelGroup;

    /**
     * Creates a new {@link HttpConnectRelayingHandler} with the specified
     * connection to relay to..
     *
     * @param relayChannel The channel to relay messages to.
     * @param channelGroup The group of channels to close on shutdown.
     */
	public HttpConnectRelayingHandler(final Channel relayChannel, final ChannelGroup channelGroup) {
		this.relayChannel = relayChannel;
		this.channelGroup = channelGroup;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		/**
		 * Edit:
		 * ProxyLog for dialog
		 * Author: Zong
		 */
		ProxyLog.appendDialog(ProxyLog.clientToString(relayChannel), ProxyLog.serverToString(e.getChannel()), ProxyLog.READ_RESPONSE);
		
		if(relayChannel.isConnected()) {
			/**
			 * Edit:
			 * ProxyLog for dialog and log files
			 * Author: Zong
			 */
			ChannelBuffer cb = (ChannelBuffer) e.getMessage();
			ProxyLog.write(ProxyLog.clientToString(relayChannel), ProxyLog.serverToString(e.getChannel()), cb.toString(ProxyLog.DEFAULT_ENCODING));
			ProxyLog.appendDialog(ProxyLog.clientToString(relayChannel), ProxyLog.serverToString(e.getChannel()), ProxyLog.WROTE_RESPONSE);
			
			relayChannel.write(cb);
		}
		else {
			ProxyUtils.closeOnFlush(e.getChannel());
		}
	}

	@Override
	public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent cse) throws Exception {
		final Channel ch = cse.getChannel();

		if(channelGroup != null) {
			channelGroup.add(ch);
		}
	}

	@Override
	public void channelClosed(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
		ProxyUtils.closeOnFlush(relayChannel);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
		ProxyUtils.closeOnFlush(e.getChannel());
	}
}
