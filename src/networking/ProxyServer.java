package networking;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class ProxyServer {


	public static ChannelGroup allChannels = new DefaultChannelGroup("Proxy-Server");
	private boolean running = false;
	private ServerBootstrap sb;
	private NioClientSocketChannelFactory cf;
	private int port = 8080;

	public void start() {
		Executor executor = Executors.newCachedThreadPool();
		this.sb = new ServerBootstrap(new NioServerSocketChannelFactory(executor, executor));
		this.sb.setOption("backlog", 1024);
		this.cf = new NioClientSocketChannelFactory(executor, executor);
		this.sb.setPipelineFactory(new ProxyPipelineFactory(this.cf)); 
        Channel channel = this.sb.bind(new InetSocketAddress(this.port));
        allChannels.add(channel);
        this.running = true;
	}
	
	public void stop() {
        ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly();
        cf.releaseExternalResources();
        this.running = false;
	}

	public void setPort(int newPort) {
		this.port = newPort;
	}
	
	public boolean isRunning() {
		return this.running;
	}
}
