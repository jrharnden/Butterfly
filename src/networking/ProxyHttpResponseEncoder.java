/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/**
* HTTP response encoder for the proxy.
*/
public class ProxyHttpResponseEncoder extends HttpResponseEncoder {
	@Override
	protected Object encode(final ChannelHandlerContext ctx, final Channel channel, final Object msg) throws Exception {
		if(msg instanceof ProxyHttpResponse) {
			final ProxyHttpResponse proxyResponse = (ProxyHttpResponse) msg;
			final Object response = proxyResponse.getResponse();

			if(response instanceof HttpResponse) {
				final HttpResponse hr = (HttpResponse) response;
				
				ProxyUtils.stripHopByHopHeaders(hr);
				ProxyUtils.addVia(hr);
			}

			final ChannelBuffer encoded = (ChannelBuffer) super.encode(ctx, channel, response);
			return encoded;
		}
		else if(msg instanceof HttpResponse) {
			final HttpResponse hr = (HttpResponse) msg;
			
			ProxyUtils.stripHopByHopHeaders(hr);
			ProxyUtils.addVia(hr);
		}

		return super.encode(ctx, channel, msg);
	}
}
