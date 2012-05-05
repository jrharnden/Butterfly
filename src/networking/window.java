package networking;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

/**
 * window Dummy window to start the demo
 * @author Zong
 */
public class window {
	private JTextArea					txtNumConn, txtConnections, txtDialog;
	private JFrame						frame;
	private ProxyServer					server;
	protected JTextField				txtPort;
	protected JButton					btnListen;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window window = new window();
					window.frame.setVisible(true);
				}
				catch(Exception e) {
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
				if(server != null && server.isRunning()) {
					server.stop();
					btnListen.setText("Listen");
				}
				else {
					server = ProxyServer.createServer(Integer.parseInt(txtPort.getText()));
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
		// frame.getContentPane().add(txtConnections);
		txtDialog = new JTextArea();
		txtDialog.setLineWrap(true);
		txtDialog.setEditable(false);
		txtDialog.setBounds(304, 6, 331, 386);
		// frame.getContentPane().add(txtDialog);
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
		
		ProxyLog.setConnectionText(txtConnections);
		ProxyLog.setCountText(txtNumConn);
		ProxyLog.setDialogText(txtDialog);
		ProxyLog.setLogEnabled(true);
	}
}
