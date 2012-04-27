/*
*	This file was modified from the LittleProxy source. 
*/
package networking;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class VerifiedAddressFactory {
	public static InetSocketAddress newInetSocketAddress(final String host, final int port) throws UnknownHostException {
		return new InetSocketAddress(InetAddress.getByName(host), port);
	}
}
