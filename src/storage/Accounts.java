package storage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	public boolean removeAccount(Account a){
		return accounts.remove(a);
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
	/**
	 * Hashes a String password
	 * @param password text password to be hashed
	 * @return Hex String of hashed password
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public String hashPass(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	       MessageDigest digest = MessageDigest.getInstance("SHA-512");
	       digest.update(password.getBytes());
	       byte[] hash = digest.digest();
	       StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<hash.length;i++) {
	    		String hex=Integer.toHexString(0xff & hash[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	return hexString.toString();
	 }
	public boolean saveAccounts(Accounts a){
		ObjectOutputStream objOut = null;
		FileOutputStream fileOut = null;
		File f = null;
		try {
			fileOut = new FileOutputStream("./data.dat");
			objOut = new ObjectOutputStream(fileOut);
			
			objOut.writeObject(a);
			objOut.close();
			f = new File("./data.dat");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public Accounts loadAccounts(){
		ObjectInputStream objIn = null;
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream("./data.dat");
			objIn = new ObjectInputStream(fileIn);
			
			Accounts a;
			try {
				a = (Accounts) objIn.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			objIn.close();
			return a;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Exports filters to a text file
	 * @param a Account for which the filters are to be exported from 
	 * @return true if successful
	 */
	public boolean exportFilters(Account a){
		return false;
		
	}
	/**
	 * Imports filters from a text file into an account
	 * @param a Account to import the filters into
	 * @param f File where the filters are stored
	 * @return true if succesful
	 */
	public Filters importFilters(Account a, File f){
		return null;
	}
	 


	
	 
}
