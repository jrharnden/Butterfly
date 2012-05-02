import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JButton;

import org.eclipse.swt.widgets.Shell;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import gui.view.*;


public class Backbone {

	/**
	 * @param args
	 */
	private Channel channel;
	protected Thread proxyThread;
	protected JButton btnListen;
	protected Thread filterThread;
	protected NioClientSocketChannelFactory cf;
	protected ServerBootstrap sb;
	static ChannelGroup allChannels = new DefaultChannelGroup("Proxy-Server");

	public static void main(String[] args) {
			
			Backbone b = new Backbone();
		//	b.startNetworking();
			b.startDisplay();
	}
	
	public void startDisplay(){
		Shell shell = new Shell();
		LoginShell lw = new LoginShell(shell);
		ApplicationWindow ap = new ApplicationWindow();
		//lw.open(ap);
		ap.open();
	}
	

}

