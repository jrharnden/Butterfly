package networking;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class FilterResponseHandler extends SimpleChannelUpstreamHandler {
	/********************************************************************
	 * Fields
	 ********************************************************************/
	

	/********************************************************************
	 * Constructors
	 ********************************************************************/
	public FilterResponseHandler() {
	}
	
	/********************************************************************
	 * Public Methods
	 ********************************************************************/
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception{
		HttpResponse response = (DefaultHttpResponse) e.getMessage();
		//TODO add filter for responses here
		ctx.sendUpstream(e);
	}

	/********************************************************************
	 * Private Methods
	 ********************************************************************/
}
