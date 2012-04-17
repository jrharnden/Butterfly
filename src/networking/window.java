package networking;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
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
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

/**
 * window
 * Dummy window to start the demo
 * 
 * @author Zong
 *
 */
public class window {
	public static ProxyLog log = new ProxyLog("./proxylog");
	public static final int WRITE_TO_CLIENT = 0, READ_FROM_CLIENT = 1, WRITE_TO_HOST = 2, READ_FROM_HOST = 3;
	public static final int MAX_DIALOG_SIZE = 20;
	static public JTextArea txtNumConn, txtConnections, txtDialog; 
	
	private static final Object ConnectionsLock = new Object();
	private static final Object DialogLock = new Object();
	private static ArrayList<String> connectionList = new ArrayList<String>(), dialogList = new ArrayList<String>();
	private static int NumConnections = 0;
	private JFrame frame;
	private ProxyServer server;
	
	protected JTextField txtPort;
	protected JButton btnListen;

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
		server = new ProxyServer();
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 650, 430);
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

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (server.isRunning()) {
					server.stop();
			        btnListen.setText("Listen");
				} else {
					server.start();
					btnListen.setText("Stop");
				}
				
			}
		});
		btnListen.setBounds(205, 7, 89, 23);
		frame.getContentPane().add(btnListen);
		
		txtConnections = new JTextArea();
		txtConnections.setLineWrap(true);
		txtConnections.setEditable(false);
		txtConnections.setBounds(10, 61, 284, 331);
		//frame.getContentPane().add(txtConnections);
		
		txtDialog = new JTextArea();
		txtDialog.setLineWrap(true);
		txtDialog.setEditable(false);
		txtDialog.setBounds(304, 6, 331, 386);
		//frame.getContentPane().add(txtDialog);
		
		JLabel lblConnections = new JLabel("Connections");
		lblConnections.setBounds(10, 36, 103, 14);
		frame.getContentPane().add(lblConnections);
		
		txtNumConn = new JTextArea();
		txtNumConn.setText("0");
		txtNumConn.setEditable(false);
		txtNumConn.setBounds(109, 31, 185, 22);
		frame.getContentPane().add(txtNumConn);
		
		JScrollPane sbConnections = new JScrollPane(txtConnections);
		sbConnections.setBounds(10, 61, 284, 331);
		frame.getContentPane().add(sbConnections);
		
		JScrollPane sbDialog = new JScrollPane(txtDialog);
		sbDialog.setBounds(304, 6, 331, 386);
		frame.getContentPane().add(sbDialog);
	}
	
	public static void addConnection(String client, String server) {
		synchronized(ConnectionsLock) {
			NumConnections++;
			txtNumConn.setText(Integer.toString(NumConnections));
			connectionList.add(client);
			connectionList.add(server);
			String updatedList = "";
			for (int i=0; i<connectionList.size(); i+=2) {
				updatedList += connectionList.get(i);
				updatedList += " connected to ";
				updatedList += connectionList.get(i+1);
				updatedList += "\n";
			}
			txtConnections.setText(updatedList);
		}
	}
	
	public static void removeConnection(String client, String server) {
		synchronized(ConnectionsLock) {
			NumConnections--;
			txtNumConn.setText(Integer.toString(NumConnections));
			for (int i=0; i<connectionList.size(); i+=2) {
				if (connectionList.get(i).equals(client)) {
					connectionList.remove(i+1);
					connectionList.remove(i);
					i = connectionList.size();
				}
			}
			String updatedList = "";
			for (int i=0; i<connectionList.size(); i+=2) {
				updatedList += connectionList.get(i);
				updatedList += " connected to ";
				updatedList += connectionList.get(i+1);
				updatedList += "\n";
			}
			txtConnections.setText(updatedList);
		}
	}
	
	public static void ConnectionUpdate(String client, String server, int action) {
		synchronized(DialogLock) {
			String update = "";
			switch(action) {
				case WRITE_TO_CLIENT:
					update += server;
					update += " wrote to ";
					update += client;
					update += "\n";
					break;
				case WRITE_TO_HOST:
					update += client;
					update += " wrote to ";
					update += server;
					update += "\n";
					break;
				case READ_FROM_CLIENT:
					update += client;
					update += " read for ";
					update += server;
					update += "\n";
					break;
				case READ_FROM_HOST:
					update += server;
					update += " read for ";
					update += client;
					update += "\n";
					break;
			}
			if (dialogList.size() > MAX_DIALOG_SIZE) {
				dialogList.remove(0);
			}
			dialogList.add(update);
			update = "";
			for (int i =0; i<dialogList.size(); i++) {
				update += dialogList.get(i);
			}
			txtDialog.setText(update);
		}
	}
}
