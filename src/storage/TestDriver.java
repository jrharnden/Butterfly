package storage;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class TestDriver {
	public static void main(String[] args){
		Accounts accounts = new Accounts();
		accounts.addAccount(new Account("Admin", accounts.hashPass("admin"),"Administrator"));
		accounts.addAccount(new Account("User", accounts.hashPass("qwerty"), "Standard"));
		
		try {
			Account user = accounts.getAccount("User",accounts.hashPass("qwerty"));
			user.addFilter(new Filter("Kill Nosey JavaScripts", "*(.(referrer|plugins|cookie|colorDepth|pixelDepth|external)|history.length)*", "<!-- Killed Nosey JavaScript -->"));
			user.addFilter(new Filter("Disable JavaScript (and meta) cookies", ".cookie(*[(;)=])\1|http-equiv=\"Set-Cookie", ".Cracker\1"));
			user.addFilter(new Filter("Kill alert/confirm boxes"," (<!DOCTYPE*> |)\1","$STOP()\1\r\n<!--//--><script> "));
			user.addFilter(new Filter("Filter Placeholder", "*(.(referrer|plugins|cookie|colorDepth|pixelDepth|external)|history.length)*", "<!-- Killed Nosey JavaScript -->"));
			user.addFilter(new Filter("Filter Placeholder", ".cookie(*[(;)=])\1|http-equiv=\"Set-Cookie", ".Cracker\1"));
			user.addFilter(new Filter("Filter Placeholder"," (<!DOCTYPE*> |)\1","$STOP()\1\r\n<!--//--><script> "));
			Account admin = accounts.getAccount("Admin",accounts.hashPass("admin"));
			admin.addFilter(new Filter("Kill Nosey JavaScripts", "*(.(referrer|plugins|cookie|colorDepth|pixelDepth|external)|history.length)*", "<!-- Killed Nosey JavaScript -->"));
			admin.addFilter(new Filter("Disable JavaScript (and meta) cookies", ".cookie(*[(;)=])\1|http-equiv=\"Set-Cookie", ".Cracker\1"));
			admin.addFilter(new Filter("Kill alert/confirm boxes"," (<!DOCTYPE*> |)\1","$STOP()\1\r\n<!--//--><script> "));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		accounts.saveAccounts();
		accounts.loadAccounts();
		for(Account a: accounts){
			System.out.println(a.toString());
		}
	}
}
