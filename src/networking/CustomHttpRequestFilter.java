package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jboss.netty.handler.codec.http.HttpRequest;

import storage.Filter;

/**
* Class for modifying HTTP requests.
*/
public class CustomHttpRequestFilter implements HttpRequestFilter {
	
	private final List<CompiledFilter> filters;
	
	private final class CompiledFilter{ 
		private final Pattern filter;
		private final String replaceWith;
		private final String header;
		
		public CompiledFilter(String regex, String replaceWith, String header){
			filter = Pattern.compile(regex);
			this.replaceWith = replaceWith;
			this.header = header;
		}
	}
	
	public CustomHttpRequestFilter(List<Filter> filters) {
		this.filters = new ArrayList<CompiledFilter>();
		
		for(Filter filter : filters) {
			if(filter.getHeader())
				this.filters.add(new CompiledFilter(ProxyUtils.substringAfter(filter.getRegex(), ":"), filter.getReplaceWith(), ProxyUtils.substringBefore(filter.getRegex(), ":")));
		}
	}
	
	public void filter(HttpRequest httpRequest) {
		//System.out.println(httpRequest.getHeader("Referer"));
		
		//for(CompiledFilter filter : filters){
			//if(httpRequest.containsHeader(filter.header));
				//httpRequest.setHeader(filter.header, filter.filter.matcher(httpRequest.getHeader(filter.header)).replaceAll(filter.replaceWith));
		//}
	}
}