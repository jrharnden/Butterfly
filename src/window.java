import java.awt.EventQueue;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;


public class window {

	private JFrame frame;
	protected JTextField txtPort;
	protected Thread proxyThread;
	private ProxyNetwork proxy;
	private FilterSystem filter;
	ConcurrentLinkedQueue<ProxyDatagram> toProxy, toFilter;
	protected JButton btnListen;
	protected Thread filterThread;

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
		toProxy = new ConcurrentLinkedQueue<ProxyDatagram>();
		toFilter = new ConcurrentLinkedQueue<ProxyDatagram>();
		proxy = new ProxyNetwork(toProxy,toFilter);
		filter = new FilterSystem(toFilter,toProxy);
		frame = new JFrame();
		frame.setBounds(100, 100, 321, 78);
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
				if (btnListen.getText().equals("Listen")&&!proxy.isRunning()) {
					btnListen.setText("Stop");
					proxy.setPort(Integer.parseInt(txtPort.getText()));
					try {
						proxyThread = new Thread(new ProxyNetwork(toProxy, toFilter, Integer.parseInt(txtPort.getText())),"Network Subsystem");
						filterThread = new Thread(new FilterSystem(toFilter, toProxy),"Filter System");
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					proxyThread.start();
					filterThread.start();
				}
				else if (btnListen.getText().equals("Stop")){
					filterThread.stop();
					filterThread = null;
					proxyThread.stop();
					proxyThread = null;
					toFilter.clear();
					toProxy.clear();
					btnListen.setText("Listen");
				}
			}
		});
		btnListen.setBounds(205, 7, 89, 23);
		frame.getContentPane().add(btnListen);
	}
}
