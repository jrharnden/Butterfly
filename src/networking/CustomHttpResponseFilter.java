package networking;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import storage.Filter;

/**
* Custom HTTP filter.
*/
public class CustomHttpResponseFilter implements HttpFilter {
	
	private final List<CompiledFilter> filters;
	
	private final class CompiledFilter{ 
		private final Pattern filter;
		private final String replaceWith;
		
		public CompiledFilter(String regex, String replaceWith){
			filter = Pattern.compile(regex);
			this.replaceWith = replaceWith;
		}
	}
	
	public CustomHttpResponseFilter(List<Filter> filters) {
		this.filters = new ArrayList<CompiledFilter>();
		
		for(Filter filter : filters) {
			if(!filter.getHeader())
				this.filters.add(new CompiledFilter(filter.getRegex(), filter.getReplaceWith()));
		}
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
					html = filterHtmlString(html);
					response.setContent(ChannelBuffers.copiedBuffer(ChannelBuffers.BIG_ENDIAN, html.getBytes(Charset.forName("UTF-8"))));
				}
				else {
					html = response.getContent().toString(Charset.forName("ISO-8859-1"));
					html = filterHtmlString(html);
					response.setContent(ChannelBuffers.copiedBuffer(ChannelBuffers.BIG_ENDIAN, html.getBytes(Charset.forName("UTF-8"))));					
				}
				response.setHeader("Content-length", html.length());
			}
		}

		return response;
	}

	public int getMaxResponseSize() {
		return 1048576;
	}
	
	private String filterHtmlString(String html){
		for(CompiledFilter filter : filters){
			html = filter.filter.matcher(html).replaceAll(filter.replaceWith);
		}
		
		return html;
	}
}
