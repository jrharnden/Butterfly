import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class ProxyConnection {
	public final static int OUTGOING = 0, INCOMING = 1, DEFAULT_PORT = 80;
	private SelectionKey toKey;
	private int type;
	private String hostURL;
	
	public ProxyConnection(int inType) {
		type = inType;
	}
	
	public ProxyConnection(SelectionKey inSocketChannel, int inType) {
		toKey = inSocketChannel;
		type = inType;
	}
	
	public void setToKey(SelectionKey inSocketChannel) {
		toKey = inSocketChannel;
	}
	
	public void setHostURL(String inURL) {
		hostURL = inURL;
	}
	
	public SelectionKey getToKey() {
		return toKey;
	}
	
	public boolean isOutgoingConnection() {
		return type == OUTGOING;
	}
	
	public boolean isIncomingConnection() {
		return type == INCOMING;
	}
	
	public int getConnectionType() {
		return type;
	}
	
	public String getHostURL() {
		return hostURL;
	}
}
