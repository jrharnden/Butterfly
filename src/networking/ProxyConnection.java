package networking;
import java.nio.channels.SelectionKey;

/**
 * ProxyConnection
 * Data container to signify what type of connection a channel may have and to where
 * 
 * @author Zong
 *
 */
public class ProxyConnection {
	/*****************************************************************
	 * Static Constants
	 *****************************************************************/
	public final static int OUTGOING = 0, INCOMING = 1, DEFAULT_PORT = 80;
	
	/*****************************************************************
	 * Fields
	 *****************************************************************/
	private SelectionKey toKey;
	private int type;
	private String hostURL;

	/*****************************************************************
	 * Constructors
	 *****************************************************************/
	public ProxyConnection(int inType) {
		this.type = inType;
	}
	
	public ProxyConnection(SelectionKey inSocketChannel, int inType) {
		this.toKey = inSocketChannel;
		this.type = inType;
	}
	

	/*****************************************************************
	 * Public Methods
	 *****************************************************************/
	
	/**
	 * setToKey
	 * Sets the selection key that this key is connected to
	 * 
	 * @param inSocketChannel, key that is being connected to
	 */
	public void setToKey(SelectionKey inKey) {
		this.toKey = inKey;
	}
	
	/**
	 * setHostURL
	 * Sets the host URL being connected to. Only to be used for client connections to servers.
	 * 
	 * @param inURL, URL address of host server
	 */
	public void setHostURL(String inURL) {
		this.hostURL = inURL;
	}
	
	/**
	 * getToKey
	 * Returns the key being connected to
	 * 
	 * @return key being connected to
	 */
	public SelectionKey getToKey() {
		return this.toKey;
	}
	
	/**
	 * isOutgoingConnection
	 * Checks if the connection is an outgoing connection, in that the connection is from a client to a host server.
	 * 
	 * @return true if the connection is outgoing
	 */
	public boolean isOutgoingConnection() {
		return this.type == OUTGOING;
	}
	
	/**
	 * isIncomingConnection
	 * Checks if the connection is an incoming connection, in that the connection is from a host server to a client.
	 * 
	 * @return true if the connection is incoming
	 */
	public boolean isIncomingConnection() {
		return this.type == INCOMING;
	}
	
	/**
	 * getConnectionType
	 * Returns the integer representation of the type of connection.
	 * 0 for Outgoing
	 * 1 for Incoming
	 * 
	 * @return type of connection
	 */
	public int getConnectionType() {
		return this.type;
	}
	
	/**
	 * getHostURL
	 * Returns the string representation of the host URL, null if there is none
	 * 
	 * @return string representation of host URL
	 */
	public String getHostURL() {
		return this.hostURL;
	}
}
