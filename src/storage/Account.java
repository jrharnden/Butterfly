package storage;
import java.io.Serializable;


public class Account implements Serializable{

	private static final long serialVersionUID = -218624877012403429L;
	private String accName, passHash, group;
	private Boolean[] permissions;
	private Filters filters;
	public Account(String name, String pass, String gr){
		accName = name;
		passHash = pass;
		group = gr;
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
	public void changeName(String newName){
		accName = newName;
	}
	public void changePass(String newPass){
		passHash = newPass;
	}
	public void changeGroup(String newGroup){
		group = newGroup;
	}
	public void addFilter(Filter f){
		filters.addFilter(f);
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
			return acc.getName().equals(accName) && acc.getPassHash().equals(passHash) &&acc.getGroup().equals(group);
		}catch(ClassCastException e){
			return false;
		}
		
	}
	
}
