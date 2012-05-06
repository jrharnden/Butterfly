package storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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
	
	/**
	 * Returns a default filter should only be used by Administrator
	 * 
	 * @param filterName
	 * @return
	 */
	public Filter getDefaultFilter(int filterID) {
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
	
	public Filter getFilter(int filterID) {
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

	
	public void addFilter(final Filter filter) throws PatternSyntaxException {
		if(filter == null) throw new IllegalArgumentException();
		
		if (filter != null) {
			Pattern.compile(filter.getRegex());
			activeFilters.add(filter);
		}
	}
	
	public void addFilter(Collection<Filter> filters) {
		for (Filter filter : filters) {
			activeFilters.add(filter);
		}
	}
	
	public void addDefaultFilter(Filter f) {
		if (!defaultFilters.contains(f))
			defaultFilters.add(f);
	}
	
	public void addInactiveFilter(final Filter filter) throws PatternSyntaxException {
		if(filter == null) throw new IllegalArgumentException();
		
		if (filter != null) {
			Pattern.compile(filter.getRegex());
			inactiveFilters.add(new Filter(filter));
		}
	}
		
	public void addInactiveFilter(Collection<Filter> filters) {
		for (Filter filter : filters) {
			inactiveFilters.add(new Filter(filter)); 
		}
	}
	
	public void addPermission(Permission perm) {
		permissions.add(perm);
	}
	
	public void addPermission(EnumSet<Permission> perm) {
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
	public Filter removeFilter(int filterID) {
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

	/**
	 * Removes a filter from the users active filters
	 * 
	 * @param filterName
	 *            filter to be removed
	 * @return removed filter
	 */
	public Filter removeActiveFilter(int filterID) {
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
	public Filter removeInactiveFilter(int filterID) {
		final int inactiveLength = inactiveFilters.size();
		
		for(int i = 0; i < inactiveLength; ++i) {
			if (inactiveFilters.get(i).getId() == filterID) {
				return inactiveFilters.remove(i);
			}
		}
		return null;
	}

	public void removePermission(Permission perm) {
		permissions.remove(perm);
	}

	public void removePermission(EnumSet<Permission> perm) {
		permissions.removeAll(perm);
	}
	
	/*******************************************************************************************************************
	/ Utility Methods 
	/******************************************************************************************************************/

	@Override
	public boolean equals(Object o) {
		try {
			Account acc = (Account) o;
			return acc.getName().equals(accName) && acc.getPassHash().equals(passHash);
		} 
		catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return accName + ":" + passHash + ":" + group;
	}

}
