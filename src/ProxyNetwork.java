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
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;




public class ProxyNetwork implements Runnable {
	private static final String DEFAULT_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_PORT = 8080;
	private boolean running = false;
	private InetAddress serverAddress;
	private int port, connectionCount = 0;
	private ServerSocketChannel server;
	private Selector selector;
	private ConcurrentLinkedQueue<ProxyDatagram> inQueue, outQueue;
	private HashMap<SocketChannel,ArrayList<byte[]>> writingMap;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	
	public ProxyNetwork(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing) throws IOException {
		initProxy(incoming,outgoing,DEFAULT_ADDRESS,DEFAULT_PORT);
	}
	
	public ProxyNetwork(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing, int newPort) throws IOException {
		initProxy(incoming,outgoing,DEFAULT_ADDRESS,newPort);
	}
	
	public ProxyNetwork(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing, String newAddress, int newPort) throws IOException {
		initProxy(incoming,outgoing,DEFAULT_ADDRESS,newPort);
	}
	
	public boolean isRunning() {
		return running;
	}
	
	private void initProxy(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing, String newAddress, int newPort) throws IOException {
		this.writingMap = new HashMap<SocketChannel,ArrayList<byte[]>>();
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
	
	public void setServerAddress(String newAddress) throws UnknownHostException {
		// TODO Auto-generated method stub
		serverAddress = InetAddress.getByName(newAddress);
	}

	public boolean setPort(int newPort) {
		if (running||newPort < 0 || newPort > 65555) {
			return false;
		}
		else {
			port = newPort;
		}
		return true;
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		// turn on running
		running = true;
		
		
	    // Bind the server socket to the specified address and port
	    InetSocketAddress address = new InetSocketAddress(this.serverAddress, this.port);
	    try {
			this.server.socket().bind(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			this.PullDataFromInQueue();
			
			// Wait for an event one of the registered channels
	        try {
				this.selector.select();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
	        		try {
	        			this.acceptConnection(currentKey);
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			System.err.println("Unable to accept a connection");
	        		}
	        	}
	        	else if (currentKey.isReadable()) {
	        		try {
	        			this.readSocket(currentKey);
	        		} catch (IOException e) {
	        			System.err.println("Unabled to read connection");
	        		}
	        	}
	        	else if (currentKey.isWritable()) {
	        		try {
	        			this.writeSocketChannel(currentKey);
	        		} catch (IOException e) {
	        			System.err.println("Unable to write to connection");
	        		}
	        	}
	        } //end while(selectedKeys.hasNext())
			
		} //end while(running)
		
	}

	private void PullDataFromInQueue() {
		// Pull any data from inQueue
		ProxyDatagram filteredData;
		while ((filteredData = inQueue.poll())!= null) {
			ProxyConnection conn = (ProxyConnection) filteredData.getKey().attachment();
			
			// create new SocketChannel for new host connection
			if ((conn.getHostURL()!= null&&conn.isOutgoingConnection())||(conn.isIncomingConnection())){
				if (conn.getToKey() == null) {
					SocketChannel hostSocketChannel = null;
					try {
						hostSocketChannel = SocketChannel.open(new InetSocketAddress(conn.getHostURL(), 80));
						hostSocketChannel.configureBlocking(false);
						
						System.out.println("Connected to host: " + hostSocketChannel.toString() );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.err.println("Unabled to connect/un-block host: " + conn.getHostURL());
					}
					
					SelectionKey hostKey = null;
					try {
						hostKey = hostSocketChannel.register(this.selector, SelectionKey.OP_WRITE, new ProxyConnection(filteredData.getKey() , ProxyConnection.INCOMING));
					} catch (ClosedChannelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.err.println("Unable to register host key");
					}
					conn.setToKey(hostKey);
					// add the data to hash map, to be written
					this.writingMap.put((SocketChannel) hostKey.channel(), filteredData.getData());
				}
				// else we change the toKey to be ready to write
				else {
					conn.getToKey().interestOps(SelectionKey.OP_WRITE);
					// add the data to hash map, to be written
					this.writingMap.put((SocketChannel) conn.getToKey().channel(), filteredData.getData());
				}
			}
		}
	}

	private void writeSocketChannel(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ArrayList<byte[]> data = this.writingMap.get(socketChannel);
		
		
		for (int i = 0; i < data.size(); i++) {
			buffer.clear();
			buffer.put(data.get(i));
			buffer.flip();
			int bytesWritten = socketChannel.write(buffer);
			//System.out.println("Bytes written " + bytesWritten + "\n" + new String(data.get(i)));
		}
		key.interestOps(SelectionKey.OP_READ);
		
		System.out.println("Wrote something: " + key.channel().toString() );
	}

	private void acceptConnection(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

	    // Accept the connection and make it non-blocking
	    SocketChannel socketChannel = serverChannel.accept();
	    socketChannel.configureBlocking(false);

	    // Register the new SocketChannel with our Selector, indicating
	    // we'd like to be notified when there's data waiting to be read
	    socketChannel.register(this.selector, SelectionKey.OP_READ, new ProxyConnection(ProxyConnection.OUTGOING));
		
		System.out.println("Connected: " + socketChannel.toString() );
	}
	
	private void readSocket(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int numRead;
		ProxyDatagram datagram = new ProxyDatagram(key);
		do {
			buffer.clear();
			numRead = socketChannel.read(this.buffer);
			
			if (numRead > 0){
				byte[] data = new byte[numRead];
			    System.arraycopy(buffer.array(), 0, data, 0, numRead);
				datagram.addData(data);
			}
		} while (numRead > 0);
		if (numRead==-1) {	
			System.out.println("Closed: " + key.channel().toString() );
			socketChannel.close();
			key.cancel();
		}
		else {
			outQueue.add(datagram);
			
			System.out.println("Read something: " + key.channel().toString() );
		}
	}
}
