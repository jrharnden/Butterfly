package storage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;


public class Account implements Serializable{
	private static int ids = 0;
	private static final long serialVersionUID = -218624877012403429L;
	private String accName, passHash;
	private Group group;
	private EnumSet<Permission> permissions = EnumSet.of(Permission.CREATEFILTER, Permission.DELETEFILTER, Permission.EDITFILTER);
	private ArrayList<Filter> activeFilters;
	private ArrayList<Filter> inactiveFilters;
	private ArrayList<Filter> defaultFilters;
	private int accountId;
	public Account(String name, String pass, Group gr){
		accName = name;
		passHash = pass;
		if(!(gr == null)){
			group = gr;
			if(group==Group.ADMINISTRATOR){
				permissions = EnumSet.allOf(Permission.class);
			}
			
		}
		else group = Group.STANDARD;
		activeFilters = new ArrayList<Filter>();
		inactiveFilters = new ArrayList<Filter>();
		defaultFilters = new ArrayList<Filter>();
		accountId = ids++;
	}
	public String getName(){
		return accName;
	}
	public String getPassHash(){
		return passHash;
	}
	public Group getGroup(){
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
	public void changeGroup(Group newGroup){
		if(!(newGroup == null))
			group = newGroup;
		else group = Group.STANDARD;
		
	}
	public int getAccountId(){
		return accountId;
	}
	public void addFilter(Filter f){
		if(f!=null)
			activeFilters.add(f);
	}
	public void addFilter(Collection<Filter> f){
		for(Filter filt: f){
			inactiveFilters.add(filt);
		}
	}
	public void addInactiveFilter(Filter f){
		if(f!= null)
			inactiveFilters.add(f);
	}
	public ArrayList<Filter> getAllFilters(){
		ArrayList<Filter> allFilters = new ArrayList<Filter>();
		allFilters.addAll(inactiveFilters);
		allFilters.addAll(activeFilters);
		allFilters.addAll(defaultFilters);
		return allFilters;
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
	public void removePermission(EnumSet<Permission> perm){
		permissions.removeAll(perm);
	}
	public void addPermission(EnumSet<Permission> perm){
		permissions.addAll(perm);
	}
	public EnumSet<Permission> getPermissions(){
		return permissions;
	}
	public void addDefaultFilter(Filter f){
		if(!defaultFilters.contains(f))
			defaultFilters.add(f);
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
