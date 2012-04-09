package storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;


public class Accounts implements Serializable,Iterable<Account> {
	private static final long serialVersionUID = -6591287821163963006L;
	private ArrayList<Account> accounts = new ArrayList<Account>();
	
	public boolean addAccount(Account a){
		if(!containsAccount(a.getName())){
			accounts.add(a);
			return true;
		}
		return false;
	}
	public boolean removeAccount(Account a){
		return accounts.remove(a);
	}
	/**
	 * Checks if an account with the same name and password exists
	 * @param name Name of the account
	 * @param hashPass hashed password of the account
	 * @return true if it exists
	 */
	public boolean containsAccount(String name, String hashPass){
		for(Account a: accounts){
			if(a.getName().equals(name) && a.getPassHash().equals(hashPass))
				return true;
		}
		System.err.println("Couldn't find "+name + " "+ hashPass);
		return false;
	}
	/**
	 * Checks if an account with the same name exists
	 * @param name Name of the account
	 * @return true if exists
	 */
	public boolean containsAccount(String name){
		for(Account a: accounts){
			if(a.getName().equals(name))
				return true;
		}
		return false;
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
	public Account getAccount(String username){
		for(Account a: accounts){
			if(a.getName().equals(username))
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
	public String hashPass(String password){
	       MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-512");
			digest.update(password.getBytes());
		       byte[] hash = digest.digest();
		       StringBuffer hexString = new StringBuffer();
		    	for (int i=0;i<hash.length;i++) {
		    		String hex=Integer.toHexString(0xff & hash[i]);
		   	     	if(hex.length()==1) hexString.append('0');
		   	     	hexString.append(hex);
		    	}
		    	return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	       
	 }
	public boolean saveAccounts(){
		ObjectOutputStream objOut = null;
		FileOutputStream fileOut = null;
		File f = null;
		try {
			fileOut = new FileOutputStream("./data.dat");
			objOut = new ObjectOutputStream(fileOut);
			
			objOut.writeObject(accounts);
			objOut.close();
			f = new File("./data.dat");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	@SuppressWarnings("unchecked")
	public boolean loadAccounts(){
		ObjectInputStream objIn = null;
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream("./data.dat");
			objIn = new ObjectInputStream(fileIn);
			
			
			try {
				accounts = (ArrayList<Account>) objIn.readObject();
				for(Account ac: accounts) System.out.println(ac.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			objIn.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Exports filters to a text file
	 * @param a Account for which the filters are to be exported from 
	 * @param f File that the filters will be exported to
	 * @return true if successful
	 */
	public boolean exportFilters(Account a, File f){
		BufferedWriter fwr;
		try{
			fwr = new BufferedWriter(new FileWriter(f));
			ArrayList<Filter> fil = a.getActiveFilters();
			for(Filter filt: fil){
				String s = filt.toString();
				fwr.write(s);
				fwr.newLine();
			}
		}
		catch(IOException e){
			System.err.println(e.getMessage());
			//TODO: Something meaningful
		}
		return false;
		
	}
	/**
	 * Imports filters from a text file into an account
	 * @param a Account to import the filters into
	 * @param f File where the filters are stored
	 * @return true if successful
	 */
	public void importFilters(Account a, File f){
		BufferedReader br;
		try {
			
			br = new BufferedReader(new FileReader(f));
			String fstr;
			while((fstr = br.readLine()) != null){
				String[] filt= fstr.split(":");
				if(filt.length==3){ 
					//TODO I had to add what we were replacing the regex with, don't know if I broke this
					a.addFilter(new Filter(filt[0],filt[1],filt[2]));
				}
				else{
					System.err.println("Malformed regex read");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NullPointerException e){
			if(a == null)
			System.err.println("Account was null");
		}
		
	}
	@Override
	public Iterator<Account> iterator() {
		// TODO Auto-generated method stub
		return accounts.iterator();
	}
	
	 


	
	 
}
