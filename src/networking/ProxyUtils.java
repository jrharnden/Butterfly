/*
*	This file was modified from the LittleProxy source and the Apache StringUtils source. 
*/
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

/**
* Utilities for the proxy.
*/
public class ProxyUtils {
	private static final Set<String>	hopByHopHeaders	= new HashSet<String>();
	private static final String			via;
	private static final String			hostName;
	
	private static ChannelFutureListener	CLOSE	= new ChannelFutureListener() {
		public void operationComplete(final ChannelFuture future) {
			final Channel ch = future.getChannel();

			if(ch.isOpen()) {
				ch.close();
			}
		}
	};

	static {
		try {
			final InetAddress localAddress = InetAddress.getLocalHost();
			hostName = localAddress.getHostName();
		}
		catch(final UnknownHostException e) {
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
	
	public static final String	CONNECT_OK_HEADERS	= "Connection: Keep-Alive\r\nProxy-Connection: Keep-Alive\r\n" + via + "\r\n";
	public static final String	PROXY_ERROR_HEADERS	= "Connection: close\r\nProxy-Connection: close\r\nPragma: no-cache\r\nCache-Control: no-cache\r\n" + via + "\r\n";

	private ProxyUtils() {}

    /**
     * Strips the host from a URI string. This will turn "http://host.com/path"
     * into "/path".
     *
     * @param uri The URI to transform.
     * @return A string with the URI stripped.
     */
	public static String stripHost(final String uri) {
		if(!uri.startsWith("http")) {
			return uri;
		}

		final String noHttpUri = substringAfter(uri, "://");
		final int slashIndex = noHttpUri.indexOf("/");

		if(slashIndex == -1) {
			return "/";
		}

		return noHttpUri.substring(slashIndex);
	}

    /**
     * Copies the mutable fields from the response original to the copy.
     *
     * @param original The original response to copy from.
     * @param copy The copy.
     * @return The copy with all mutable fields from the original.
     */
	public static HttpResponse copyMutableResponseFields(final HttpResponse original, final HttpResponse copy) {
		final Collection<String> headerNames = original.getHeaderNames();

		for(final String name : headerNames) {
			final List<String> values = original.getHeaders(name);
			copy.setHeader(name, values);
		}

		copy.setContent(original.getContent());

		if(original.isChunked()) {
			copy.setChunked(true);
		}

		return copy;
	}

    /**
     * Writes a raw HTTP response to the channel.
     *
     * @param channel The channel.
     * @param statusLine The status line of the response.
     * @param headers The raw headers string.
     */	
	public static void writeResponse(final Channel channel, final String statusLine, final String headers) {
		writeResponse(channel, statusLine, headers, "");
	}

    /**
     * Writes a raw HTTP response to the channel.
     *
     * @param channel The channel.
     * @param statusLine The status line of the response.
     * @param headers The raw headers string.
     * @param responseBody The response body.
     */	
	public static void writeResponse(final Channel channel, final String statusLine, final String headers, final String responseBody) {
		final String fullResponse = statusLine + headers + responseBody;

		try {
			final ChannelBuffer buf = ChannelBuffers.copiedBuffer(fullResponse.getBytes("UTF-8"));
			
			channel.write(buf);
			channel.setReadable(true);
			
			return;
		}
		catch(final UnsupportedEncodingException e) {
			return;
		}
	}

	static boolean isLastChunk(final Object msg) {
		if(msg instanceof HttpChunk) {
			final HttpChunk chunk = (HttpChunk) msg;
			
			return chunk.isLast();
		}
		else {
			return false;
		}
	}


    /**
     * Closes the specified channel after all queued write requests are flushed.
     *
     * @param ch The {@link Channel} to close.
     */	
	public static void closeOnFlush(final Channel ch) {
		if(ch.isConnected()) {
			ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ProxyUtils.CLOSE);
		}
	}

    /**
     * Parses the host and port an HTTP request is being sent to.
     *
     * @param httpRequest The request.
     * @return The host and port string.
     */	
	public static String parseHostAndPort(final HttpRequest httpRequest) {
		return parseHostAndPort(httpRequest.getUri());
	}

    /**
     * Parses the host and port an HTTP request is being sent to.
     *
     * @param httpRequest The request.
     * @return The host and port string.
     */	
	public static String parseHostAndPort(final String uri) {
		final String tempUri;

		if(!uri.startsWith("http")) {
			tempUri = uri;
		}
		else {
			tempUri = substringAfter(uri, "://");
		}

		final String hostAndPort;

		if(tempUri.contains("/")) {
			hostAndPort = tempUri.substring(0, tempUri.indexOf("/"));
		}
		else {
			hostAndPort = tempUri;
		}

		return hostAndPort;
	}

    /**
     * Parses the port from an address.
     *
     * @param httpRequest The request containing the URI.
     * @return The port. If not port is explicitly specified, returns the
     * the default port 80 if the protocol is HTTP and 443 if the protocol is
     * HTTPS.
     */
	public static int parsePort(final HttpRequest httpRequest) {
		final String uri = httpRequest.getUri();

		if(uri.contains(":")) {
			final String portStr = substringAfter(uri, ":");
			
			return Integer.parseInt(portStr);
		}
		else if(uri.startsWith("http")) {
			return 80;
		}
		else if(uri.startsWith("https")) {
			return 443;
		}
		else {
			return 80;
		}
	}


    /**
     * Creates a copy of an original HTTP request to void modifying it.
     *
     * @param original The original request.
     * @param keepProxyFormat keep proxy-formatted URI (used in chaining)
     * @return The request copy.
     */	
	public static HttpRequest copyHttpRequest(final HttpRequest original) {
		final HttpMethod method = original.getMethod();
		final String uri = original.getUri();
		final HttpRequest copy;
		final String noHostUri = ProxyUtils.stripHost(uri);
		copy = new DefaultHttpRequest(original.getProtocolVersion(), method, noHostUri);
		final ChannelBuffer originalContent = original.getContent();

		if(originalContent != null) {
			copy.setContent(originalContent);
		}

		copyHeaders(original, copy);
		
		final String ae = copy.getHeader(HttpHeaders.Names.ACCEPT_ENCODING);

		if(isNotBlank(ae)) {
			final String noSdch = ae.replace(",sdch", "").replace("sdch", "");
			
			copy.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, noSdch);
		}

		final String proxyConnectionKey = "Proxy-Connection";

		if(copy.containsHeader(proxyConnectionKey)) {
			final String header = copy.getHeader(proxyConnectionKey);
			
			copy.removeHeader(proxyConnectionKey);
			copy.setHeader("Connection", header);
		}

		ProxyUtils.addVia(copy);
		
		return copy;
	}
	
    /**
     * Removes all headers that should not be forwarded.
     * See RFC 2616 13.5.1 End-to-end and Hop-by-hop Headers.
     *
     * @param msg The message to strip headers from.
     */
	public static void stripHopByHopHeaders(final HttpMessage msg) {
		final Set<String> headerNames = msg.getHeaderNames();

		for(final String name : headerNames) {
			if(hopByHopHeaders.contains(name.toLowerCase())) {
				msg.removeHeader(name);
			}
		}
	}	

	private static void copyHeaders(final HttpMessage original, final HttpMessage copy) {
		final Set<String> headerNames = original.getHeaderNames();

		for(final String name : headerNames) {
			if(!hopByHopHeaders.contains(name.toLowerCase())) {
				final List<String> values = original.getHeaders(name);
				
				copy.setHeader(name, values);
			}
		}
	}

    /**
     * Adds the Via header to specify that the message has passed through
     * the proxy.
     *
     * @param msg The HTTP message.
     */
	public static void addVia(final HttpMessage msg) {
		final StringBuilder sb = new StringBuilder();
		final List<String> vias;
		
		sb.append(msg.getProtocolVersion().getMajorVersion());
		sb.append(".");
		sb.append(msg.getProtocolVersion().getMinorVersion());
		sb.append(".");
		sb.append(hostName);
		
		if(msg.containsHeader(HttpHeaders.Names.VIA)) {
			vias = msg.getHeaders(HttpHeaders.Names.VIA);
			vias.add(sb.toString());
		}
		else {
			vias = Arrays.asList(sb.toString());
		}

		msg.setHeader(HttpHeaders.Names.VIA, vias);
	}
	
	/**
	* <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
	*
	* <pre>
	* isNotBlank(null)      = false
	* isNotBlank("")        = false
	* isNotBlank(" ")       = false
	* isNotBlank("bob")     = true
	* isNotBlank("  bob  ") = true
	* </pre>
	*
	* @param str  the String to check, may be null
	* @return <code>true</code> if the String is
	*  not empty and not null and not whitespace
	*/
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	* <p>Checks if a String is whitespace, empty ("") or null.</p>
	*
	* <pre>
	* isBlank(null)      = true
	* isBlank("")        = true
	* isBlank(" ")       = true
	* isBlank("bob")     = false
	* isBlank("  bob  ") = false
	* </pre>
	*
	* @param str  the String to check, may be null
	* @return <code>true</code> if the String is null, empty or whitespace
	*/
	public static boolean isBlank(String str) {
		int strLen;

		if(str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for(int i = 0; i < strLen; i++) {
			if((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}

		return true;
	}

	/**
	* <p>Gets the substring after the first occurrence of a separator.
	* The separator is not returned.</p>
	*
	* <p>A <code>null</code> string input will return <code>null</code>.
	* An empty ("") string input will return the empty string.
	* A <code>null</code> separator will return the empty string if the
	* input string is not <code>null</code>.</p>
	*
	* <p>If nothing is found, the empty string is returned.</p>
	*
	* <pre>
	* substringAfter(null, *)      = null
	* substringAfter("", *)        = ""
	* substringAfter(*, null)      = ""
	* substringAfter("abc", "a")   = "bc"
	* substringAfter("abcba", "b") = "cba"
	* substringAfter("abc", "c")   = ""
	* substringAfter("abc", "d")   = ""
	* substringAfter("abc", "")    = "abc"
	* </pre>
	*
	* @param str  the String to get a substring from, may be null
	* @param separator  the String to search for, may be null
	* @return the substring after the first occurrence of the separator,
	*  <code>null</code> if null String input
	*/
	public static String substringAfter(String str, String separator) {
		if(isEmpty(str)) {
			return str;
		}

		if(separator == null) {
			return "";
		}

		int pos = str.indexOf(separator);

		if(pos == -1) {
			return "";
		}

		return str.substring(pos + separator.length());
	}

	/**
	* <p>Checks if a String is empty ("") or null.</p>
	*
	* <pre>
	* isEmpty(null)      = true
	* isEmpty("")        = true
	* isEmpty(" ")       = false
	* isEmpty("bob")     = false
	* isEmpty("  bob  ") = false
	* </pre>
	*
	* @param str  the String to check, may be null
	* @return <code>true</code> if the String is empty or null
	*/
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	* <p>Gets the substring before the first occurrence of a separator.
	* The separator is not returned.</p>
	*
	* <p>A <code>null</code> string input will return <code>null</code>.
	* An empty ("") string input will return the empty string.
	* A <code>null</code> separator will return the input string.</p>
	*
	* <p>If nothing is found, the string input is returned.</p>
	*
	* <pre>
	* substringBefore(null, *)      = null
	* substringBefore("", *)        = ""
	* substringBefore("abc", "a")   = ""
	* substringBefore("abcba", "b") = "a"
	* substringBefore("abc", "c")   = "ab"
	* substringBefore("abc", "d")   = "abc"
	* substringBefore("abc", "")    = ""
	* substringBefore("abc", null)  = "abc"
	* </pre>
	*
	* @param str  the String to get a substring from, may be null
	* @param separator  the String to search for, may be null
	* @return the substring before the first occurrence of the separator,
	*  <code>null</code> if null String input
	*/
	public static String substringBefore(String str, String separator) {
		if(isEmpty(str) || separator == null) {
			return str;
		}

		if(separator.length() == 0) {
			return "";
		}

		int pos = str.indexOf(separator);

		if(pos == -1) {
			return str;
		}

		return str.substring(0, pos);
	}
}