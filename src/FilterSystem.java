import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * FilterSystem
 * A dummy filter system, doesn't do anything at the moment, except extracts the host name, which should really be a network task,
 * but was just faster in here I thought atleast.
 * 
 * @author Zong
 *
 */
public class FilterSystem implements Runnable{

	private boolean running = false;
	private ConcurrentLinkedQueue<ProxyDatagram> inQueue;
	private ConcurrentLinkedQueue<ProxyDatagram> outQueue;
	
	public FilterSystem(ConcurrentLinkedQueue<ProxyDatagram> incoming, ConcurrentLinkedQueue<ProxyDatagram> outgoing) {
		this.inQueue = incoming;
		outQueue = outgoing;
	}
	
	/**
	 * run
	 * Main body for filtering system
	 */
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
				dataString = currentDatagram.toString();
				((ProxyConnection) currentDatagram.getKey().attachment()).setHostURL(this.getHostURL(dataString));
				//filter stuff here
				
				//write to out-bound Queue
				outQueue.add(currentDatagram);
				currentDatagram.getSelector().wakeup();
			}
		}
	}

	/**
	 * getHostURL
	 * Simple Extracting of host URL, needed for Proxy Network Subsystem
	 * 
	 * @param dataString, String representation of the data
	 * @return a string for the Host URL
	 */
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
	
}
