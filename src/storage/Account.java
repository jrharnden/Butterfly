package storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class Account implements Serializable {
	private EnumSet<Permission> permissions = EnumSet.of(Permission.CREATEFILTER, Permission.DELETEFILTER, Permission.EDITFILTER);

	private static final long serialVersionUID = -218624877012403429L;

	private final List<Filter> inactiveFilters;
	private final List<Filter> defaultFilters;
	private final List<Filter> activeFilters;

	private String accName, passHash;

	private static int ids = 0;
	private final int accountId;

	private Group group;

	public Account(final String name, final String pass, final Group gr) {
		passHash = pass;
		accName = name;

		if (gr != null) {
			group = gr;
			
			if (group == Group.ADMINISTRATOR) {
				permissions = EnumSet.allOf(Permission.class);
			}
		} 
		else { 
			group = Group.STANDARD;
		}
		
		activeFilters = new ArrayList<Filter>();
		inactiveFilters = new ArrayList<Filter>();
		defaultFilters = new ArrayList<Filter>();
		accountId = ids++;
		
	}
	
	/******************************************************************************************************************
	/ Getters 
	/*****************************************************************************************************************/
	
	public int getAccountId() {
		return accountId;
	}
	
	public List<Filter> getActiveFilters() {
		return Collections.unmodifiableList(activeFilters);
	}
	
	public List<Filter> getAllFilters() {
		List<Filter> allFilters = new ArrayList<Filter>();
		allFilters.addAll(inactiveFilters);
		allFilters.addAll(activeFilters);
		allFilters.addAll(defaultFilters);
		return Collections.unmodifiableList(allFilters);
	}
	public List<Filter> getActiveAndDefaultFilters(){
		List<Filter> activedefault = new ArrayList<Filter>();
		activedefault.addAll(activeFilters);
		activedefault.addAll(defaultFilters);
		return Collections.unmodifiableList(activedefault);
	}
	/**
	 * Returns a default filter should only be used by Administrator
	 * 
	 * @param filterName
	 * @return
	 */
	public Filter getDefaultFilter(final int filterID) {
		int defaultLength = defaultFilters.size();
		
		for(int i = 0; i < defaultLength; ++i) {
			if(defaultFilters.get(i).getId() == filterID) {
				return new Filter(defaultFilters.get(i));
			}
		}
		
		return null;
	}
	
	public List<Filter> getDefaultFilters() {
		return Collections.unmodifiableList(defaultFilters);
	}
	
	public Filter getFilter(final int filterID) {
		final int activeLength = activeFilters.size();
		
		for(int i = 0; i < activeLength; ++i) {
			if (activeFilters.get(i).getId() == filterID) {
				return new Filter(activeFilters.get(i));
			}
		}
		
		final int inactiveLength = inactiveFilters.size();
		
		for(int i = 0; i < inactiveLength; ++i) {
			if (inactiveFilters.get(i).getId() == filterID) {
				return new Filter(inactiveFilters.get(i));
			}
		}

		return null;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public List<Filter> getInactiveFilters() {
		return Collections.unmodifiableList(inactiveFilters);
	}
	
	public String getName() {
		return accName;
	}

	public String getPassHash() {
		return passHash;
	}
	
	public EnumSet<Permission> getPermissions() {
		return permissions;
	}
	
	
	/*******************************************************************************************************************
	/ Setters 
	/******************************************************************************************************************/

	public void setGroup(final Group newGroup) {
		if (newGroup != null)
			group = newGroup;
		else
			group = Group.STANDARD;
	}
	
	public void setName(final String newName) {
		accName = newName;
	}

	public void setPass(final String newPass) {
		passHash = newPass;
	}

	/*******************************************************************************************************************
	/ Adding 
	/******************************************************************************************************************/

	
	public void addFilter(final Filter filter) {
		if(filter == null) throw new IllegalArgumentException();
		
		if(!activeFilters.contains(filter))
			activeFilters.add(new Filter(filter));
	}
	
	public void addFilter(final Collection<Filter> filters) {
		if(filters == null) throw new IllegalArgumentException();
		
		for (Filter filter : filters) {
			if (!activeFilters.contains(filter))
				activeFilters.add(new Filter(filter));
		}
	}
	
	public void addDefaultFilter(final Filter filter) {
		if(filter == null) throw new IllegalArgumentException();
		boolean contain = false;
		for(Filter f: defaultFilters){
			if(f.getId() == filter.getId()) contain = true;
		}
		if (!contain)
			defaultFilters.add(new Filter(filter));
	}
	
	public void addInactiveFilter(final Filter filter) {
		if(filter == null) throw new IllegalArgumentException();
		
		if (!inactiveFilters.contains(filter))
			inactiveFilters.add(new Filter(filter));
	}
		
	public void addInactiveFilter(final Collection<Filter> filters) {
		if(filters == null) throw new IllegalArgumentException();
		
		for (Filter filter : filters) {
			if (!inactiveFilters.contains(filter))
				inactiveFilters.add(new Filter(filter));
		}
	}
	
	public void addPermission(final Permission perm) {
		permissions.add(perm);
	}
	
	public void addPermission(final EnumSet<Permission> perm) {
		permissions.addAll(perm);
	}
	
	/*******************************************************************************************************************
	/ Removing
	/******************************************************************************************************************/

	/**
	 * Removes a filter from the users inactive or active filter list
	 * 
	 * @param filterID
	 *            the filter to be removed
	 * @return removed filter
	 */
	public Filter removeFilter(final int filterID) {
		final int activeLength = activeFilters.size();
				
		for(int i = 0; i < activeLength; ++i) {
			if (activeFilters.get(i).getId() == filterID) {
				return activeFilters.remove(i);
			}
		}
		
		final int inactiveLength = inactiveFilters.size();
		
		for(int i = 0; i < inactiveLength; ++i) {
			if (inactiveFilters.get(i).getId() == filterID) {
				return inactiveFilters.remove(i);
			}
		}

		return null;
	}
	public Filter removeDefaultFilter(final int FilterID){
		for(Filter f: defaultFilters){
			if(f.getId()==FilterID){
				defaultFilters.remove(f);
				return f;
			}
		}
		return null;
	}
	/**
	 * Removes a filter from the users active filters
	 * 
	 * @param filterName
	 *            filter to be removed
	 * @return removed filter
	 */
	public Filter removeActiveFilter(final int filterID) {
		final int activeLength = activeFilters.size();
		
		for(int i = 0; i < activeLength; ++i) {
			if (activeFilters.get(i).getId() == filterID) {
				return activeFilters.remove(i);
			}
		}
		return null;
	}

	/**
	 * Remove filter from the users inactive filters
	 * 
	 * @param filterName
	 *            name of the filter to be removed
	 * @return removed filter
	 */
	public Filter removeInactiveFilter(final int filterID) {
		final int inactiveLength = inactiveFilters.size();
		
		for(int i = 0; i < inactiveLength; ++i) {
			if (inactiveFilters.get(i).getId() == filterID) {
				return inactiveFilters.remove(i);
			}
		}
		return null;
	}

	public void removePermission(final Permission perm) {
		permissions.remove(perm);
	}

	public void removePermission(final EnumSet<Permission> perm) {
		permissions.removeAll(perm);
	}
	
	/*******************************************************************************************************************
	/ Utility Methods 
	/******************************************************************************************************************/

	@Override
	public boolean equals(Object o) {
		
		if(this == o) return true;
		if(!(o instanceof Account)) return false;
		
		Account acc = (Account) o;
		
		return acc.getName().equals(accName) && acc.getPassHash().equals(passHash);
	}
	
	@Override
	public int hashCode(){
		return accName.hashCode()+passHash.hashCode();
	}


	@Override
	public String toString() {
		return accName + ":" + passHash + ":" + group;
	}

}
