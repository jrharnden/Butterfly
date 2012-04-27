package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import org.apache.commons.io.IOUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * ProxyLog Class to write out logs for the network system
 * @author Zong Xiong
 */
public class ProxyLog {
	public static final Object		fileLock	= new Object();
	/********************************************************
	 * Fields
	 ********************************************************/
	public static String			root		= "./proxyLog";
	private static final DateFormat	dateformat	= new SimpleDateFormat("MM-dd-YYYY");

	/********************************************************
	 * Constructor
	 ********************************************************/
	public ProxyLog(String path) {
		// this.root = path;
	}

	/********************************************************
	 * Public Methods
	 ********************************************************/
	@SuppressWarnings("deprecation")
	public static boolean write(Channel client, Channel host, HttpRequest msg) {
		String hostString = host.getRemoteAddress().toString().split("/")[0];
		String clientString = client.getRemoteAddress().toString().replaceAll(":", ".");
		int index = clientString.lastIndexOf(".");
		clientString = clientString.substring(0, index);
		Date date = new Date();
		String path = root + "/" + dateformat.format(date) + "/" + clientString;

		try {
			File file = new File(path);
			file.mkdirs();

			if(!file.exists()) {
				file.createNewFile();
			}

			FileWriter out = new FileWriter(new File(path + "/" + hostString + ".txt"), true);
			out.write("Client writes to host\r\n---------\r\n");
			out.write(msg.toString());
			out.write("\r\n---CONTENT---\r\n");
			out.write(msg.getContent().toString("UTF-8"));
			out.write("\r\n---End---\r\n\r\n");
			out.close();
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean write(Channel client, Channel host, HttpResponse msg) throws IOException {
		String hostString = host.getRemoteAddress().toString().split("/")[0];
		String clientString = client.getRemoteAddress().toString().replaceAll(":", ".");
		int index = clientString.lastIndexOf(".");
		clientString = clientString.substring(0, index);
		DateFormat dateformat = new SimpleDateFormat("MM-dd-YYYY");
		Date date = new Date();
		String path = root + "/" + dateformat.format(date) + "/" + clientString;

		try {
			File file = new File(path);
			file.mkdirs();

			if(!file.exists()) {
				file.createNewFile();
			}

			FileWriter out = new FileWriter(new File(path + "/" + hostString + ".txt"), true);
			out.write("Host writes to client\r\n---------\r\n");
			out.write(msg.toString());
			out.write("\r\n---Content---\r\n");
			String encoding = msg.getHeader("Content-Encoding");

			if(encoding != null && encoding.equals("gzip")) {
				GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(msg.getContent().toByteBuffer().array()));
				ByteArrayOutputStream gout = new ByteArrayOutputStream();
				IOUtils.copy(gin, gout);
				out.write(gout.toString("UTF-8"));
			}
			else {
				out.write(msg.getContent().toString("UTF-8"));
			}

			out.write("\r\n---End---\r\n\r\n");
			out.close();
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
