package storage;
import java.io.Serializable;
import java.util.ArrayList;


public class Account implements Serializable{

	private static final long serialVersionUID = -218624877012403429L;
	private String accName, passHash, group;
	private Boolean[] permissions;
	private ArrayList<Filter> filters;
	public Account(String name, String pass, String gr){
		accName = name;
		passHash = pass;
		if(!(gr == null))
			group = gr;
		else group = "";
		filters = new ArrayList<Filter>();
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
	public ArrayList<Filter> getFilters(){
		return filters;
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
		filters.add(f);
	}
	public void setPermissions(boolean[] permission){
		for(int i = 0; i < permission.length;++i){
			permissions[i] = permission[i] && permissions[i];
		}
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
