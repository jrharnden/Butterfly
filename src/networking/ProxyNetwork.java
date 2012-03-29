package networking;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ProxyNetwork
 * Network Subsystem to handle connections between servers and clients. The subsystem also handles transmitting data to
 * the filter system.
 * 
 * @author Zong
 *
 */
public class ProxyNetwork implements Runnable {
	/*****************************************************************
	 * Static Constants
	 *****************************************************************/
	public static final String DEFAULT_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_PORT = 8080;
	public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
	

	/*****************************************************************
	 * Fields
	 *****************************************************************/
	private boolean running = false;
	private InetAddress serverAddress;
	private int port;
	private ServerSocketChannel server;
	private Selector selector;
	private ConcurrentLinkedQueue<ProxyDatagram> inQueue, outQueue;
	private HashMap<SocketChannel,ArrayList<byte[]>> writingMap;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	private ArrayList<SelectionKey> processingChannels;
	private ArrayList<SelectionKey> canceledKeys;
	

	/*****************************************************************
	 * Constructors
	 *****************************************************************/
	public ProxyNetwork(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing) throws IOException {
		this.initProxy(incoming,outgoing,DEFAULT_ADDRESS,DEFAULT_PORT);
	}
	
	public ProxyNetwork(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing, int newPort) throws IOException {
		this.initProxy(incoming,outgoing,DEFAULT_ADDRESS,newPort);
	}
	
	public ProxyNetwork(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing, String newAddress, int newPort) throws IOException {
		this.initProxy(incoming,outgoing,DEFAULT_ADDRESS,newPort);
	}
	
	/**
	 * initProxy
	 * Initialization for a Proxy Network System
	 * 
	 * @param incoming, incoming Queue
	 * @param outgoing, outgoing Queue
	 * @param newAddress, address for proxy server
	 * @param newPort, port for proxy server
	 * @throws IOException, if unable to initialize proxy network system
	 */
	private void initProxy(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing, String newAddress, int newPort) throws IOException {
		this.writingMap = new HashMap<SocketChannel,ArrayList<byte[]>>();
		this.processingChannels = new ArrayList<SelectionKey>();
		this.canceledKeys = new ArrayList<SelectionKey>();
		this.setServerAddress(newAddress);
		this.setPort(newPort);
		this.inQueue = incoming;
		this.outQueue = outgoing;
		
		// Create a new selector
	    this.selector = Selector.open();

	    // Create a new non-blocking server socket channel
	    this.server = ServerSocketChannel.open();
	    this.server.configureBlocking(false);
	}
	
	/*****************************************************************
	 * Public Methods
	 *****************************************************************/
	
	/**
	 * isRunning
	 * Checks if the Network Subsystem is running
	 * 
	 * @return true if the Network Subsystem is running, false otherwise
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * setServerAddress
	 * Sets the server address to a new address
	 * 
	 * @param newAddress, the new address in a String format
	 * @throws UnknownHostException, if the address is unknown
	 */
	public void setServerAddress(String newAddress) throws UnknownHostException {
		this.serverAddress = InetAddress.getByName(newAddress);
	}

	/**
	 * setPort
	 * Sets a new port within 0 and 65555
	 * 
	 * @param newPort, new port number
	 * @return true if the port was set, false otherwise
	 */
	public boolean setPort(int newPort) {
		if (this.running||newPort < 0 || newPort > 65555) {
			return false;
		}
		else {
			this.port = newPort;
		}
		return true;
	}
	
	/**
	 * stop
	 * Stops the Network Subsystem
	 */
	public void stop() {
		this.running = false;
	}
	
	/**
	 * run
	 * Main body for thread
	 */
	@Override
	public void run() {
		// turn on running
		this.running = true;
		
	    // Bind the server socket to the specified address and port
	    InetSocketAddress address = new InetSocketAddress(this.serverAddress, this.port);
	    try {
			this.server.socket().bind(address);
		} catch (IOException e) {
			System.err.println("Unabled to connect to " + address.getHostName() + " - "+address.getAddress());
		}

	    // Register the server socket channel, indicating an interest in 
	    // accepting new connections
	    try {
			this.server.register(this.selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			System.err.println("Unabled to register server to selector");
		}
	    
	    // Main Event Loop for Network
		while (running) {
			// pull any data sitting in queue
			try {
				this.pullDataFromInQueue();
			} catch (IOException e1) {
				System.err.println("Unable to pull data from incoming queue");
			}
			
			// Wait for an event one of the registered channels
	        try {
				this.selector.select();
			} catch (IOException e) {
				System.err.println("Unabled to select anything from selector");
			}

	        // Iterate over the set of keys for which events are available
	        Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
	        SelectionKey currentKey;
	        while (selectedKeys.hasNext()) {
	        	currentKey = selectedKeys.next();
	        	selectedKeys.remove();
	
	        	if (!currentKey.isValid()) {
	        		continue;
	        	}

	        	// Check what event is available and deal with it
	        	if (currentKey.isAcceptable()) {
	        		// if the key is an connection request
	        		try {
	        			this.acceptConnection(currentKey);
	        		} catch (IOException e) {
	        			System.err.println("Unable to accept a connection");
	        		}
	        	}
	        	else if (currentKey.isReadable()) {
	        		// if the key is a reading request
	        		try {
	        			this.readChannel(currentKey);
	        		} catch (IOException e) {
	        			System.err.println("Unabled to read connection");
	        		}
	        	}
	        	else if (currentKey.isWritable()) {
	        		// if the key is a writing request
	        		try {
	        			this.writeSocketChannel(currentKey);
	        		} catch (IOException e) {
	        			System.err.println("Unable to write to connection");
	        		}
	        	}
	        } //end while(selectedKeys.hasNext())
		} //end while(running)
		try {
			this.server.socket().close();
		} catch (IOException e) {
			System.err.println("Unable to legally close proxy server");
		}
		
	}
	
	/*****************************************************************
	 * Private Methods
	 *****************************************************************/
	
	/**
	 * acceptConnection
	 * Accepts in connection attempt to the key
	 * 
	 * @param key, the selection key for the host
	 * @throws IOException, if unable to establish connection with client
	 */
	private void acceptConnection(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

	    // Accept the connection and make it non-blocking and setup the timeout
	    SocketChannel socketChannel = serverChannel.accept();
	    socketChannel.configureBlocking(false);
	    socketChannel.socket().setSoTimeout(DEFAULT_SOCKET_TIMEOUT);

	    // Register the new SocketChannel with our Selector, indicating
	    // we'd like to be notified when there's data waiting to be read
	    socketChannel.register(this.selector, SelectionKey.OP_READ, new ProxyConnection(ProxyConnection.OUTGOING));
		
		System.out.println("Connected: " + socketChannel.toString() );
	}
	
	/**
	 * readChannel
	 * Reads all available data from channel
	 * 
	 * @param key, the selection key the is ready for reading
	 * @throws IOException, if there was an error in reading
	 */
	private void readChannel(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int numRead;
		
		// create the default datagram
		ProxyDatagram datagram = new ProxyDatagram(key);
		
		// while there is data we read the socket
		do {
			// clear our buffer and read
			this.buffer.clear();
			numRead = socketChannel.read(this.buffer);
			
			// if there was data, we extract it and add it to the datagram
			if (numRead > 0){
				byte[] data = new byte[numRead];
			    System.arraycopy(buffer.array(), 0, data, 0, numRead);
				datagram.addData(data);
			}
		} while (numRead > 0);
		
		// if the number of bytes read was -1, then the socket is closed and therefore
		// we close our side
		if (numRead==-1) {
			// check if connection may already be waiting for filtering, if so we add it to a list
			// to be canceled
			if(this.processingChannels.contains(key)) {
				System.out.println("Attempt to close but proccessing " + key.channel().toString());
				this.canceledKeys.add(key);
			}
			// else we simply close the key
			else {
				this.closeSelectionKey(key);
			}
		}
		// once done and if the numRead is not -1, we add the data to the queue to the Filtering System
		else {
			this.processingChannels.add(key);
			this.outQueue.add(datagram);
			
			System.out.println("Read: " + key.channel().toString() );
		}
	}
	
	/**
	 * pullDataFromInQueue
	 * Pull any data from the incoming queue, and ready the key to be written
	 * 
	 * @throws IOException, if there was any problem creating a host channel connection
	 */
	private void pullDataFromInQueue() throws IOException {
		ProxyDatagram filteredData;
		
		// Pull all data from inQueue
		while ((filteredData = inQueue.poll())!= null) {
			ProxyConnection conn = (ProxyConnection) filteredData.getKey().attachment();
			
			// create new SocketChannel for new host connection
			if ((conn.getHostURL()!= null&&conn.isOutgoingConnection())||(conn.isIncomingConnection())){
				if (conn.getToKey() == null) {
					SocketChannel hostSocketChannel = null;
					try {
						hostSocketChannel = SocketChannel.open(new InetSocketAddress(conn.getHostURL(), 80));
						hostSocketChannel.configureBlocking(false);
					    hostSocketChannel.socket().setSoTimeout(DEFAULT_SOCKET_TIMEOUT);
						
						System.out.println("Host: " + hostSocketChannel.toString() );
					} catch (IOException e) {
						System.err.println("Unabled to connect/un-block host: " + conn.getHostURL());
					}
					
					SelectionKey hostKey = null;
					try {
						hostKey = hostSocketChannel.register(this.selector, SelectionKey.OP_WRITE, new ProxyConnection(filteredData.getKey() , ProxyConnection.INCOMING));
					} catch (ClosedChannelException e) {
						System.err.println("Unable to register host key");
					}
					conn.setToKey(hostKey);
					
					// add the data to hash map, to be written
					this.writingMap.put((SocketChannel) hostKey.channel(), filteredData.getData());
				}
				// else we change the toKey to be ready to write
				else {
					// change key to writing
					conn.getToKey().interestOps(SelectionKey.OP_WRITE);
					
					// add the data to hash map, to be written
					this.writingMap.put((SocketChannel) conn.getToKey().channel(), filteredData.getData());
				}
			}
			
			// check if the key was prompted to be closed, if so we close it
			SelectionKey key = filteredData.getKey();
			if (this.processingChannels.remove(key)&&this.canceledKeys.remove(key)) {
				this.closeSelectionKey(key);
			}
		}
	}

	/**
	 * writeSocketChannel
	 * Write data to a socket channel that is ready
	 * 
	 * @param key, the selection key that is ready to be written to
	 * @throws IOException, if there is an error writing the channel
	 */
	private void writeSocketChannel(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ArrayList<byte[]> data = this.writingMap.get(socketChannel);
		
		// for all of the data in list of data, we write it out
		for (int i = 0; i < data.size(); i++) {
			this.buffer.clear();
			this.buffer.put(data.get(i));
			this.buffer.flip();
			socketChannel.write(this.buffer);
		}
		
		// after finishing reading it, we change the key back to reading
		key.interestOps(SelectionKey.OP_READ);
		
		System.out.println("Wrote something: " + key.channel().toString() );
		
		// if this key was prompted to be canceled we cancel it
		if (this.canceledKeys.contains(key)) {
			this.closeSelectionKey(key);
		}
	}

	/**
	 * closeSelectionKey
	 * Close/cancel key and attempt to close the key that is connected to it
	 * 
	 * @param key, the selection key to close/cancel
	 * @throws IOException, if there was an error closing the key
	 */
	private void closeSelectionKey(SelectionKey key) throws IOException {
		ProxyConnection conn = (ProxyConnection) key.attachment();
		SelectionKey other;
		
		// We check if the other side of the connection needs to be written to of if it even exist,
		// if so we add it the list to close, or simply close it
		if (((other = conn.getToKey()) != null)&&(this.writingMap.containsKey(other.channel()))) {
			if (this.processingChannels.contains(other)||this.writingMap.containsKey(other.channel())) {
				this.canceledKeys.add(other);
			}
			else {
				System.out.println("Closed: " + other.channel().toString() );
				other.channel().close();
				other.cancel();
			}
		}
		
		System.out.println("Closed: " + key.channel().toString() );
		
		// close this key
		key.channel().close();
		key.cancel();
	}
}
