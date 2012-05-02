/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import static org.jboss.netty.channel.Channels.pipeline;
import java.util.concurrent.Executors;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

/**
* Factory for creating pipelines for incoming requests to our listening
* socket.
*/
public class HttpServerPipelineFactory implements ChannelPipelineFactory {
	private final ClientSocketChannelFactory	clientSocketChannelFactory	= new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	private static final Timer					TIMER						= new HashedWheelTimer();
	private final RelayPipelineFactoryFactory	relayPipelineFactoryFactory;
	private final ChannelGroup					channelGroup;

    /**
     * Creates a new pipeline factory with the specified class for processing
     * proxy authentication.
     *
     * @param channelGroup The group that keeps track of open channels.
     * @param relayPipelineFactoryFactory produces factories for any relays created.
     */
	public HttpServerPipelineFactory(final ChannelGroup channelGroup, final RelayPipelineFactoryFactory relayPipelineFactoryFactory) {
		this.relayPipelineFactoryFactory = relayPipelineFactoryFactory;
		this.channelGroup = channelGroup;

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				clientSocketChannelFactory.releaseExternalResources();
			}
		}));
	}

	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = pipeline();
		
		pipeline.addLast("decoder", new HttpRequestDecoder(8192, 8192 * 2, 8192 * 2));
		pipeline.addLast("encoder", new ProxyHttpResponseEncoder());
		pipeline.addLast("idle", new IdleStateHandler(TIMER, 0, 0, 70));
		pipeline.addLast("idleAware", new IdleAwareHandler("Client-Pipeline"));
		pipeline.addLast("handler", new HttpRequestHandler(channelGroup, clientSocketChannelFactory, relayPipelineFactoryFactory));
		
		return pipeline;
	}
}
