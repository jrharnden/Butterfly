import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class FilterSystem implements Runnable{

	private boolean running = false;
	private ConcurrentLinkedQueue<ProxyDatagram> inQueue;
	private ConcurrentLinkedQueue<ProxyDatagram> outQueue;
	
	public FilterSystem(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing) {
		inQueue = incoming;
		outQueue = outgoing;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		running  = true;
		ProxyDatagram currentDatagram;
		while (running) {
			currentDatagram = inQueue.poll();
			if (currentDatagram == null) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					//possible catch for interrupt based interrupt, maybe make thread sleep until things here
					//then have network subsystem wake it up... seems round about that
					System.out.println("Filter system has been interrupted");
				}
			}
			else {
				String dataString = null;
				try {
					dataString = getString(currentDatagram);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((ProxyConnection) currentDatagram.getKey().attachment()).setHostURL(this.getHostURL(dataString));
				//filter stuff here
				
				//write to out-bound Queue
				outQueue.add(currentDatagram);
				currentDatagram.getSelector().wakeup();
			}
		}
	}

	private String getHostURL(String dataString) {
		String returnString = null;
		String [] parsed = dataString.split("\r\n");
		
		for (int i = 0; i < parsed.length; i++) {
			if (parsed[i].startsWith("Host:")) {
				returnString = parsed[i].substring(6);
				i = parsed.length;
			}
		}
		return returnString;
	}

	private String getString(ProxyDatagram datagram) throws UnsupportedEncodingException {
		String returnString = "";
		
		for (int i =0; i < datagram.getData().size(); i++) {
			returnString += new String(datagram.getData().get(i), "UTF8");
		}
		
		return returnString;
	}
	
}
