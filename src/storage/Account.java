package storage;
import java.io.Serializable;
import java.util.ArrayList;


public class Account implements Serializable{

	private static final long serialVersionUID = -218624877012403429L;
	private String accName, passHash, group;
	private Boolean[] permissions;
	private ArrayList<Filter> activeFilters;
	private ArrayList<Filter> inactiveFilters;
	public Account(String name, String pass, String gr){
		accName = name;
		passHash = pass;
		if(!(gr == null))
			group = gr;
		else group = "";
		activeFilters = new ArrayList<Filter>();
		inactiveFilters = new ArrayList<Filter>();
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
	public Filter removeActiveFilter(String filterName){
		for(Filter f: activeFilters){
			if(f.getName().equals(filterName)){
				activeFilters.remove(f);
				return f;
			}
		}
		return null;
	}
	public Filter removeInactiveFilter(String filterName){
		for(Filter f: inactiveFilters){
			if(f.getName().equals(filterName)){
				inactiveFilters.remove(f);
				return f;
			}
		}
		return null;
	}
	public void setPermissions(boolean[] permission){
		for(int i = 0; i < permission.length;++i){
			permissions[i] = permission[i] && permissions[i];
		}
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
