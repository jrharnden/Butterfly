package networking;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;

/**
 * ProxyDatagram
 * Data container for a proxy connection
 * 
 * @author Zong
 *
 */
public class ProxyDatagram {
	/*****************************************************************
	 * Fields
	 *****************************************************************/
	private SelectionKey key;
	private ArrayList<byte[]> data;

	/*****************************************************************
	 * Constructors
	 *****************************************************************/
	public ProxyDatagram(SelectionKey inKey) {
		this.key = inKey;
		this.data = new ArrayList<byte[]>();
	}

	/*****************************************************************
	 * Public Methods
	 *****************************************************************/
	
	/**
	 * getKey
	 * Returns the key for which the data was read from, the From Selection Key
	 * 
	 * @return the From Selection Key
	 */
	public SelectionKey getKey() {
		return this.key;
	}
	
	/**
	 * getSelector
	 * Returns the selector for which the key is registered to
	 * 
	 * @return selector for the key
	 */
	public Selector getSelector() {
		return this.key.selector();
	}
	
	/**
	 * getData
	 * Gets the data that was read from the key
	 * 
	 * @return an Array List of byte arrays
	 */
	public ArrayList<byte[]> getData() {
		return this.data;
	}
	
	/**
	 * addData
	 * Appends data to the end of the current list
	 * 
	 * @param bs, the byte array to append
	 */
	public void addData(byte[] bs) {
		this.data.add(bs);
	}
	
	/**
	 * clearData
	 * Clears reference to data
	 */
	public void clearData() {
		this.data.clear();
	}

	/**
	 * getString
	 * Returns a string representation of the data, currently using UTF-8 encoding
	 * 
	 * @return String representation
	 */
	public String toString() {
		String returnString = "";
		
		for (int i =0; i < this.data.size(); i++) {
			returnString += new String(this.data.get(i));
		}
		
		return returnString;
	}
	
	/*****************************************************************
	 * Private Methods
	 *****************************************************************/
}
