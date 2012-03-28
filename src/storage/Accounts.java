package storage;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Accounts implements Serializable {
	private static final long serialVersionUID = -6591287821163963006L;
	private ArrayList<Account> accounts = new ArrayList<Account>();
	
	public void addAccount(Account a){
		accounts.add(a);
	}
	public void removeAccount(Account a){
		accounts.remove(a);
	}
	public Account getAccount(Account a){
		try{
			return accounts.get(accounts.indexOf(a));
		}catch(IndexOutOfBoundsException e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	public Account getAccount(String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Account p = new Account(username, password,null);
		for(Account a: accounts){
			if(a.equals(p))
				return a;
		}
		return null;
	}
	
	 
}
