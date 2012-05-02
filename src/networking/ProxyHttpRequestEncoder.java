/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;

/**
* Request encoder for the proxy. This is necessary because we need to have
* access to the most recent request message on this connection to determine
* caching rules.
*/
public class ProxyHttpRequestEncoder extends HttpRequestEncoder {
	private final HttpRelayingHandler	relayingHandler;
	private final HttpRequestFilter		requestFilter;

    /**
     * Creates a new request encoder.
     *
     * @param handler The class that handles relaying all data along this
     * connection. We need this to synchronize caching rules for each request
     * and response pair.
     * @param requestFilter The filter for requests.
     */
	public ProxyHttpRequestEncoder(final HttpRelayingHandler handler, final HttpRequestFilter requestFilter) {
		this.relayingHandler = handler;
		this.requestFilter = requestFilter;
	}

	@Override
	protected Object encode(final ChannelHandlerContext ctx, final Channel channel, final Object msg) throws Exception {
		if(msg instanceof HttpRequest) {
			final HttpRequest request = (HttpRequest) msg;
			final HttpRequest toSend = ProxyUtils.copyHttpRequest(request);
			
			relayingHandler.requestEncoded(request);
			
			if(this.requestFilter != null) {
				this.requestFilter.filter(toSend);
			}

			return super.encode(ctx, channel, toSend);
		}

		return super.encode(ctx, channel, msg);
	}
}
