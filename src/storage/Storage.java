package storage;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Storage {
	public static boolean writeOutAccounts(Accounts a){
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
	public boolean writeOutFilters(Filters f){
		return false;
	}
	public Accounts readInAccouns(){
		return null;
	}
	public Filters readInFilters(){
		return null;
	}
	public boolean exportFilters(Filters f){
		return false;
		
	}
	public Filters importFilters(){
		return null;
	}
	 public String getHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
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
}
