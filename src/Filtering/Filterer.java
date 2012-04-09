package Filtering;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import storage.Filter;

public final class Filterer {
	
	private final List<CompiledFilter> filters;
	
	private final class CompiledFilter{ 
		private final Pattern filter;
		private final String replaceWith;
		
		public CompiledFilter(String regex, String replaceWith){
			filter = Pattern.compile(regex);
			this.replaceWith = replaceWith;
		}
	}

	public Filterer(List<Filter> filters){
		this.filters = new ArrayList<CompiledFilter>();
		
		for(Filter filter : filters) {
			this.filters.add(new CompiledFilter(filter.getRegex(), filter.getReplaceWith()));
		}
	}
	
	public String filterHtmlString(String html){
		for(CompiledFilter filter : filters){
			html = filter.filter.matcher(html).replaceAll(filter.replaceWith);
		}
		
		return html;
	}
}
