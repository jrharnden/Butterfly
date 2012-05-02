/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class DefaultRelayPipelineFactoryFactory implements RelayPipelineFactoryFactory {
	private final ChannelGroup			channelGroup;
	private final HttpRequestFilter		requestFilter;
	private final HttpResponseFilters	responseFilters;

	public DefaultRelayPipelineFactoryFactory(final HttpResponseFilters responseFilters, final HttpRequestFilter requestFilter, final ChannelGroup channelGroup) {
		this.responseFilters = responseFilters;
		this.channelGroup = channelGroup;
		this.requestFilter = requestFilter;
	}

	public ChannelPipelineFactory getRelayPipelineFactory(final HttpRequest httpRequest, final Channel browserToProxyChannel, final RelayListener relayListener) {
		return new DefaultRelayPipelineFactory(ProxyUtils.parseHostAndPort(httpRequest), httpRequest, relayListener, browserToProxyChannel, channelGroup, responseFilters, requestFilter);
	}
}