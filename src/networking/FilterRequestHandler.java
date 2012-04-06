package networking;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class FilterRequestHandler extends SimpleChannelUpstreamHandler {
	/********************************************************************
	 * Fields
	 ********************************************************************/
	

	/********************************************************************
	 * Constructors
	 ********************************************************************/
	public FilterRequestHandler() {
	}
	
	/********************************************************************
	 * Public Methods
	 ********************************************************************/
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception{
		HttpRequest request = (DefaultHttpRequest) e.getMessage();
		//TODO add filter for request here
		ctx.sendUpstream(e);
	}

	/********************************************************************
	 * Private Methods
	 ********************************************************************/

}
