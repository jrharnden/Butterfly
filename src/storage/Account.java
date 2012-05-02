package storage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;


public class Account implements Serializable{

	private static final long serialVersionUID = -218624877012403429L;
	private String accName, passHash, group;
	private Set<Permission> permissions = EnumSet.of(Permission.CREATEFILTER, Permission.DELETEFILTER, Permission.EDITFILTER);
	private ArrayList<Filter> activeFilters;
	private ArrayList<Filter> inactiveFilters;
	private ArrayList<Filter> defaultFilters;
	public Account(String name, String pass, String gr){
		accName = name;
		passHash = pass;
		if(!(gr == null)){
			group = gr;
			if(gr.toLowerCase().equals("administrator"))
					permissions.add(Permission.SETPORT);	
		}
		else group = "";
		activeFilters = new ArrayList<Filter>();
		inactiveFilters = new ArrayList<Filter>();
		defaultFilters = new ArrayList<Filter>();
	}
	public String getName(){
		return accName;
	}
	public String getPassHash(){
		return passHash;
	}
	public String getGroup(){
		return group;
	}
	public ArrayList<Filter> getActiveFilters(){
		return activeFilters;
	}
	public ArrayList<Filter> getInactiveFilters(){
		return inactiveFilters;
	}
	public void changeName(String newName){
		accName = newName;
	}
	public void changePass(String newPass){
		passHash = newPass;
	}
	public void changeGroup(String newGroup){
		if(!(newGroup == null))
			group = newGroup;
		else group = "";
		
	}
	public void addFilter(Filter f){
		activeFilters.add(f);
	}
	public void addInactiveFilter(Filter f){
		inactiveFilters.add(f);
	}
	/**
	 * Removes a filter from the users inactive or active filter list
	 * @param filterName the filter to be removed
	 * @return removed filter
	 */
	public Filter removeFilter(String filterName){
		for(Filter f: activeFilters){
			if(f.getName().equals(filterName)){
				activeFilters.remove(f);
				return f;
			}
		}
		for(Filter f: inactiveFilters){
			if(f.getName().equals(filterName)){
				inactiveFilters.remove(f);
				return f;
			}
		}
		return null;
	}
	/**
	 * Removes a filter from the users active filters
	 * @param filterName filter to be removed
	 * @return removed filter
	 */
	public Filter removeActiveFilter(String filterName){
		for(Filter f: activeFilters){
			if(f.getName().equals(filterName)){
				activeFilters.remove(f);
				return f;
			}
		}
		return null;
	}
	/**
	 * Remove filter from the users inactive filters
	 * @param filterName name of the filter to be removed
	 * @return removed filter
	 */
	public Filter removeInactiveFilter(String filterName){
		for(Filter f: inactiveFilters){
			if(f.getName().equals(filterName)){
				inactiveFilters.remove(f);
				return f;
			}
		}
		return null;
	}

	public Filter getFilter(String filterName){
		for(Filter f: activeFilters){
			if(f.getName().equals(filterName))
				return f;
		}
		for(Filter f: inactiveFilters){
			if(f.getName().equals(filterName))
				return f;
		}
		return null;
	}
	/**
	 * Returns a default filter should only be used by admin
	 * @param filterName
	 * @return
	 */
	public Filter getDefaultFilter(String filterName){
		for(Filter f: defaultFilters){
			if(f.getName().equals(filterName))
				return f;
		}
		return null;
	}
	public ArrayList<Filter> getDefaultFilters(){
		return defaultFilters;
	}
	public void addPermission(Permission perm){
		permissions.add(perm);
	}
	public void removePermission(Permission perm){
		permissions.remove(perm);
	}
	public Set<Permission> getPermissions(){
		return permissions;
	}
	@Override
	public boolean equals(Object o){
		try{
			Account acc = (Account) o;
			return acc.getName().equals(accName) && acc.getPassHash().equals(passHash);
		}catch(ClassCastException e){
			return false;
		}
		
	}
	@Override
	public String toString(){
		return accName+":"+passHash+":"+group;
	}
	
}
