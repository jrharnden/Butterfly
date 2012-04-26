package networking;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class ProxyUtils {
	private static final Set<String> hopByHopHeaders = new HashSet<String>();
	private static final String via;
	private static final String hostName;

	static {
		try {
			final InetAddress localAddress = InetAddress.getLocalHost();
			hostName = localAddress.getHostName();
		} catch (final UnknownHostException e) {
			throw new IllegalStateException("Could not determine host!", e);
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("Via: 1.1 ");
		sb.append(hostName);
		sb.append("\r\n");
		via = sb.toString();

		hopByHopHeaders.add("connection");
		hopByHopHeaders.add("keep-alive");
		hopByHopHeaders.add("proxy-authenticate");
		hopByHopHeaders.add("proxy-authorization");
		hopByHopHeaders.add("te");
		hopByHopHeaders.add("trailers");
		hopByHopHeaders.add("upgrade");
	}
	
    public static void stripHopByHopHeaders(final HttpMessage msg) {
        final Set<String> headerNames = msg.getHeaderNames();
        for (final String name : headerNames) {
            if (hopByHopHeaders.contains(name.toLowerCase())) {
                msg.removeHeader(name);
            }
        }
    }
    

	public static final String CONNECT_OK_HEADERS = "Connection: Keep-Alive\r\nProxy-Connection: Keep-Alive\r\n" + via + "\r\n";
	public static final String PROXY_ERROR_HEADERS = "Connection: close\r\nProxy-Connection: close\r\nPragma: no-cache\r\nCache-Control: no-cache\r\n" + via + "\r\n";

	private ProxyUtils() {
	}

	public static String stripHost(final String uri) {
		if (!uri.startsWith("http")) {
			return uri;
		}
		final String noHttpUri = substringAfter(uri, "://");
		final int slashIndex = noHttpUri.indexOf("/");
		if (slashIndex == -1) {
			return "/";
		}
		final String noHostUri = noHttpUri.substring(slashIndex);
		return noHostUri;
	}

	public static HttpResponse copyMutableResponseFields(final HttpResponse original, final HttpResponse copy) {
		final Collection<String> headerNames = original.getHeaderNames();
		for (final String name : headerNames) {
			final List<String> values = original.getHeaders(name);
			copy.setHeader(name, values);
		}
		copy.setContent(original.getContent());
		if (original.isChunked()) {
			copy.setChunked(true);
		}
		return copy;
	}

	public static void writeResponse(final Channel channel,	final String statusLine, final String headers) {
		writeResponse(channel, statusLine, headers, "");
	}

	public static void writeResponse(final Channel channel,	final String statusLine, final String headers, final String responseBody) {
		final String fullResponse = statusLine + headers + responseBody;
		try {
			final ChannelBuffer buf = ChannelBuffers.copiedBuffer(fullResponse.getBytes("UTF-8"));
			channel.write(buf);
			channel.setReadable(true);
			return;
		} catch (final UnsupportedEncodingException e) {
			return;
		}
	}

	static boolean isLastChunk(final Object msg) {
		if (msg instanceof HttpChunk) {
			final HttpChunk chunk = (HttpChunk) msg;
			return chunk.isLast();
		} else {
			return false;
		}
	}

	private static ChannelFutureListener CLOSE = new ChannelFutureListener() {
		public void operationComplete(final ChannelFuture future) {
			final Channel ch = future.getChannel();
			if (ch.isOpen()) {
				ch.close();
			}
		}
	};

	public static void closeOnFlush(final Channel ch) {
		if (ch.isConnected()) {
			ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ProxyUtils.CLOSE);
		}
	}

	public static String parseHostAndPort(final HttpRequest httpRequest) {
		return parseHostAndPort(httpRequest.getUri());
	}

	public static String parseHostAndPort(final String uri) {
		final String tempUri;
		if (!uri.startsWith("http")) {
			tempUri = uri;
		} else {
			tempUri = substringAfter(uri, "://");
		}
		final String hostAndPort;
		if (tempUri.contains("/")) {
			hostAndPort = tempUri.substring(0, tempUri.indexOf("/"));
		} else {
			hostAndPort = tempUri;
		}
		return hostAndPort;
	}

	public static int parsePort(final HttpRequest httpRequest) {
		final String uri = httpRequest.getUri();
		if (uri.contains(":")) {
			final String portStr = substringAfter(uri, ":");
			return Integer.parseInt(portStr);
		} else if (uri.startsWith("http")) {
			return 80;
		} else if (uri.startsWith("https")) {
			return 443;
		} else {
			return 80;
		}
	}

	public static HttpRequest copyHttpRequest(final HttpRequest original) {
		final HttpMethod method = original.getMethod();
		final String uri = original.getUri();
		final HttpRequest copy;

		final String noHostUri = ProxyUtils.stripHost(uri);
		copy = new DefaultHttpRequest(original.getProtocolVersion(), method, noHostUri);

		final ChannelBuffer originalContent = original.getContent();

		if (originalContent != null) {
			copy.setContent(originalContent);
		}

		copyHeaders(original, copy);

		final String ae = copy.getHeader(HttpHeaders.Names.ACCEPT_ENCODING);
		if (isNotBlank(ae)) {
			final String noSdch = ae.replace(",sdch", "").replace("sdch", "");
			copy.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, noSdch);
		}

		final String proxyConnectionKey = "Proxy-Connection";
		if (copy.containsHeader(proxyConnectionKey)) {
			final String header = copy.getHeader(proxyConnectionKey);
			copy.removeHeader(proxyConnectionKey);
			copy.setHeader("Connection", header);
		}

		ProxyUtils.addVia(copy);
		return copy;
	}

	private static void copyHeaders(final HttpMessage original,
			final HttpMessage copy) {
		final Set<String> headerNames = original.getHeaderNames();
		for (final String name : headerNames) {
			if (!hopByHopHeaders.contains(name.toLowerCase())) {
				final List<String> values = original.getHeaders(name);
				copy.setHeader(name, values);
			}
		}
	}

	public static void addVia(final HttpMessage msg) {
		final StringBuilder sb = new StringBuilder();
		sb.append(msg.getProtocolVersion().getMajorVersion());
		sb.append(".");
		sb.append(msg.getProtocolVersion().getMinorVersion());
		sb.append(".");
		sb.append(hostName);
		final List<String> vias;
		if (msg.containsHeader(HttpHeaders.Names.VIA)) {
			vias = msg.getHeaders(HttpHeaders.Names.VIA);
			vias.add(sb.toString());
		} else {
			vias = Arrays.asList(sb.toString());
		}
		msg.setHeader(HttpHeaders.Names.VIA, vias);
	}
	
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	public static boolean isBlank(String str) {
		int strLen;
		
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static String substringAfter(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		
		if (separator == null) {
			return "";
		}
		
		int pos = str.indexOf(separator);
		
		if (pos == -1) {
			return "";
		}
		
		return str.substring(pos + separator.length());
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static String substringBefore(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		
		if (separator.length() == 0) {
			return "";
		}
		
		int pos = str.indexOf(separator);
		
		if (pos == -1) {
			return str;
		}
	
		return str.substring(0, pos);
	}
}