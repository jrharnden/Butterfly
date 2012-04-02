package storage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Filters implements Serializable {
	private static final long serialVersionUID = 4898437231279754736L;
	private ArrayList<Filter> filters;
	public Filters(){
		filters = new ArrayList<Filter>();
	}
	public void addFilter(Filter f){
		filters.add(f);
	}
	public boolean removeFilter(Filter f){
		return filters.remove(f);
	}
	public void addFilters(List<Filter> filterList){
		for(Filter f:filterList){
			filters.add(f);
		}
	}

}
