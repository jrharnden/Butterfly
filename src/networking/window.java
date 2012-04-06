package networking;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * window
 * Dummy window to start the demo
 * 
 * @author Zong
 *
 */
public class window {
	static final ChannelGroup allChannels = new DefaultChannelGroup("Proxy-Server");
	
	private JFrame frame;
	protected JTextField txtPort;
	protected Thread proxyThread;
	protected JButton btnListen;
	protected Thread filterThread;
	protected NioClientSocketChannelFactory cf;
	protected ServerBootstrap sb;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window window = new window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public window() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 314, 78);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblPortNumber = new JLabel("Port Number:");
		lblPortNumber.setHorizontalAlignment(SwingConstants.LEFT);
		lblPortNumber.setBounds(10, 11, 89, 14);
		frame.getContentPane().add(lblPortNumber);
		
		txtPort = new JTextField();
		txtPort.setText("8080");
		txtPort.setBounds(109, 8, 86, 20);
		frame.getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		btnListen = new JButton("Listen");
		btnListen.addMouseListener(new MouseAdapter() {

			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (sb==null) {
					Executor executor = Executors.newCachedThreadPool();
					sb = new ServerBootstrap(new NioServerSocketChannelFactory(executor, executor));
					cf = new NioClientSocketChannelFactory(executor, executor);
					sb.setPipelineFactory(new ProxyPipelineFactory(cf)); 
			        Channel channel = sb.bind(new InetSocketAddress(Integer.parseInt(txtPort.getText())));
			        allChannels.add(channel);
					btnListen.setText("Stop");
				} else {
			        ChannelGroupFuture future = allChannels.close();
			        future.awaitUninterruptibly();
			        cf.releaseExternalResources();
			        sb = null;
			        cf = null;
			        btnListen.setText("Listen");
				}
				
			}
		});
		btnListen.setBounds(205, 7, 89, 23);
		frame.getContentPane().add(btnListen);
	}
}
