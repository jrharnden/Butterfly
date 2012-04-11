package networking;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * ProxyLog
 * 
 * Class to write out logs for the network system
 * 
 * @author Zong Xiong
 *
 */
public class ProxyLog {
	/********************************************************
	 * Fields
	 ********************************************************/
	private String root;


	/********************************************************
	 * Constructor
	 ********************************************************/
	public ProxyLog(String path) {
		this.root = path;
	}

	/********************************************************
	 * Public Methods
	 ********************************************************/
	
	public static void write(Channel client, Channel host, HttpRequest msg) {
		File file = new File("/");
		//file.mkdirs();
		file = new File("/connection.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter out = new FileWriter(file, true);
			out.write(client.getRemoteAddress().toString() + " to " + host.getRemoteAddress().toString() + "\r\n---------\r\n");
			out.write(((HttpRequest) msg).toString());
			out.write("\r\n---End---\r\n\r\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Unable to create or write to file: " + e.getMessage());
		}
	}
	
	public static void write(Channel client, Channel host, HttpResponse msg) throws IOException {
		File file = new File("C:/proxylog/" + client.getRemoteAddress().toString() + "/" + host.getRemoteAddress().toString());
		//file.mkdirs();
		file = new File("/connection.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter out = new FileWriter(file, true);
			out.write(host.getRemoteAddress().toString() + " to " + client.getRemoteAddress().toString() + "\r\n---------\r\n");
			out.write(((HttpResponse) msg).toString());
			out.write("\r\n---End---\r\n\r\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Unable to create or write to file: " + e.getMessage());
		}
	}



	/********************************************************
	 * Private Methods
	 ********************************************************/
	private String ArrayToString(ArrayList<byte[]> data) {
		String returnString = "";
		
		for (int i =0; i < data.size(); i++) {
			returnString += new String(data.get(i));
		}
		
		return returnString;
	}

}
