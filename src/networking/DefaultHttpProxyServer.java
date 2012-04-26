package networking;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class DefaultHttpProxyServer {
    
    private final ChannelGroup allChannels = new DefaultChannelGroup("HTTP-Proxy-Server");
            
    private final int port;
    
    private final HttpRequestFilter requestFilter;

    private final ServerBootstrap serverBootstrap;

    private final HttpResponseFilters responseFilters;
    
    private boolean isRunning = false;
    
    public DefaultHttpProxyServer(final int port, final HttpResponseFilters responseFilters, final HttpRequestFilter requestFilter) {
        this.port = port;
        this.responseFilters = responseFilters;
        this.requestFilter = requestFilter;

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(final Thread t, final Throwable e) {

            }
        });
        
        this.serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    }
    
    public void start() {
    	start(false, true);
    }
    
    public void start(final boolean localOnly, final boolean anyAddress) {
    	isRunning = true;
    	final HttpServerPipelineFactory factory = new HttpServerPipelineFactory(this.allChannels, new DefaultRelayPipelineFactoryFactory(this.responseFilters, this.requestFilter, this.allChannels));
        serverBootstrap.setPipelineFactory(factory);
        
        InetSocketAddress isa;
        if (localOnly) {
            isa = new InetSocketAddress("127.0.0.1", port);
        }
        else if (anyAddress) {
            isa = new InetSocketAddress(port);
        } else {
            try {
                isa = new InetSocketAddress(NetworkUtils.getLocalHost(), port);
            } catch (final UnknownHostException e) {
                isa = new InetSocketAddress(port);
            }
        }
        final Channel channel = serverBootstrap.bind(isa);
        allChannels.add(channel);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                stop();
            }
        }));
    }
    
    public void stop() {
        final ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly(6*1000);
        serverBootstrap.releaseExternalResources();
    }
    
    public boolean isRunning() {
    	return isRunning;
    }
}
