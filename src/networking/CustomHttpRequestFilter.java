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
		
		public CompiledFilter(String regex, String replaceWith){
			filter = Pattern.compile(regex);
			this.replaceWith = replaceWith;
		}
	}
	
	public CustomHttpRequestFilter(List<Filter> filters) {
		this.filters = new ArrayList<CompiledFilter>();
		
		for(Filter filter : filters) {
			this.filters.add(new CompiledFilter(filter.getRegex(), filter.getReplaceWith()));
		}
	}
	
	public void filter(HttpRequest httpRequest) {

	}
}