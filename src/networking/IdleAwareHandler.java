/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
* This handles idle sockets.
*/
public class IdleAwareHandler extends IdleStateAwareChannelHandler {
	private final String	handlerName;

	public IdleAwareHandler(final String handlerName) {
		this.handlerName = handlerName;
	}

	@Override
	public void channelIdle(final ChannelHandlerContext ctx, final IdleStateEvent e) {
		if(e.getState() == IdleState.READER_IDLE) {
			e.getChannel().close();
		}
		else if(e.getState() == IdleState.WRITER_IDLE) {
			e.getChannel().close();
		}
	}

	@Override
	public String toString() {
		return "IdleAwareHandler [handlerName=" + handlerName + "]";
	}
}
