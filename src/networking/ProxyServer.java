/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
* HTTP proxy server.
*/
public class ProxyServer {
	private final ChannelGroup			allChannels	= new DefaultChannelGroup("HTTP-Proxy-Server");
	private final int					port;
	private final HttpRequestFilter		requestFilter;
	private final ServerBootstrap		serverBootstrap;
	private final HttpResponseFilters	responseFilters;
	private boolean						isRunning	= false;

    /**
     * Creates a new proxy server.
     *
     * @param port The port the server should run on.
     * @param responseFilters The {@link Map} of request domains to match
     * with associated {@link HttpFilter}s for filtering responses to
     * those requests.
     * @param requestFilter Optional filter for modifying incoming requests.
     * Often <code>null</code>.
     */
	public ProxyServer(final int port, final HttpResponseFilters responseFilters, final HttpRequestFilter requestFilter) {
		this.port = port;
		this.responseFilters = responseFilters;
		this.requestFilter = requestFilter;

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(final Thread t, final Throwable e) {}
		});

		serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
	}

	public void start() {
		isRunning = true;
		serverBootstrap.setPipelineFactory(new HttpServerPipelineFactory(allChannels, new DefaultRelayPipelineFactoryFactory(responseFilters, requestFilter, allChannels)));

		final Channel channel = serverBootstrap.bind(new InetSocketAddress("127.0.0.1", port));
		allChannels.add(channel);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				stop();
			}
		}));
	}

	public void stop() {
		final ChannelGroupFuture future = allChannels.close();
		future.awaitUninterruptibly(6 * 1000);
		serverBootstrap.releaseExternalResources();
		isRunning = false;
		ProxyLog.clearConnections();
	}

	public boolean isRunning() {
		return isRunning;
	}
	
}
