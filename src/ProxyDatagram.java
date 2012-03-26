import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;


public class ProxyDatagram {
	private SelectionKey key;
	private ArrayList<byte[]> data;
	
	public ProxyDatagram(SelectionKey inKey) {
		key = inKey;
		data = new ArrayList<byte[]>();
	}
	
	public SelectionKey getKey() {
		return key;
	}
	
	public Selector getSelector() {
		return key.selector();
	}
	
	public ArrayList<byte[]> getData() {
		return data;
	}
	
	public void addData(byte[] bs) {
		data.add(bs);
	}
	
	public void clearData() {
		data.clear();
	}
}
