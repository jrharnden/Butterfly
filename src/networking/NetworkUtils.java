package networking;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkUtils {
    public static InetAddress getLocalHost() throws UnknownHostException {
        final InetAddress is = InetAddress.getLocalHost();
        if (!is.isLoopbackAddress()) {
            return is;
        }

        return getLocalHostViaUdp();
    }

    private static InetAddress getLocalHostViaUdp() throws UnknownHostException {
        final InetSocketAddress sa = new InetSocketAddress("www.google.com", 80);

        DatagramSocket sock = null;
        try {
            sock = new DatagramSocket();
            sock.connect(sa);
            final InetAddress address = sock.getLocalAddress();
            return address;
        } catch (final SocketException e) {
            return InetAddress.getLocalHost();
        } finally {
            if (sock != null) {
                sock.close();
            }
        }
    }
}