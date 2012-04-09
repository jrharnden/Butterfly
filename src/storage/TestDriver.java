package storage;

public class TestDriver {
	public static void main(String[] args){
		Accounts accounts = new Accounts();
		accounts.addAccount(new Account("Admin", accounts.hashPass("admin"),null));
		accounts.addAccount(new Account("User", accounts.hashPass("qwerty"), null));
		accounts.saveAccounts();
		accounts.loadAccounts();
		for(Account a: accounts){
			System.out.println(a.toString());
		}
	}
}
