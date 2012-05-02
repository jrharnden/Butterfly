/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpRequest;

public interface RelayPipelineFactoryFactory {
	ChannelPipelineFactory getRelayPipelineFactory(HttpRequest httpRequest, Channel browserToProxyChannel, RelayListener relayListener);
}