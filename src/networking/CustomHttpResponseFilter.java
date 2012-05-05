package networking;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
* Custom HTTP filter.
*/
public class CustomHttpResponseFilter implements HttpFilter {
	
	public CustomHttpResponseFilter() {
		
	}
	
	public boolean filterResponses(final HttpRequest httpRequest) {
		return true;
	}

	public HttpResponse filterResponse(final HttpRequest httpRequest, final HttpResponse response) {
		String header = response.getHeader("Content-Type");
		String html = null;
		
		if(header != null) {
			if(header.contains("text/html")) {
				if(header.contains("charset")){
					html = response.getContent().toString(Charset.forName(ProxyUtils.substringAfter(header, "=")));
					response.setContent(ChannelBuffers.copiedBuffer(ChannelBuffers.BIG_ENDIAN, html.getBytes(Charset.forName("UTF-8"))));
				}
				else {
					html = response.getContent().toString(Charset.forName("ISO-8859-1"));
					response.setContent(ChannelBuffers.copiedBuffer(ChannelBuffers.BIG_ENDIAN, html.getBytes(Charset.forName("UTF-8"))));					
				}
			}
		}

		return response;
	}

	public int getMaxResponseSize() {
		//Netty uses 1048576
		return 1048576;
	}
}