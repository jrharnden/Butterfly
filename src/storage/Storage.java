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
	 
}
