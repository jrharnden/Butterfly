package storage;

import java.io.File;

public class TestDriver {
	public static void main(String[] args){
		Accounts accounts = new Accounts();
		accounts.addAccount(new Account("Admin", accounts.hashPass("admin"),Group.ADMINISTRATOR));
		accounts.addAccount(new Account("User", accounts.hashPass("qwerty"), Group.STANDARD));
		accounts.addAccount(new Account("NotSoRandomAccountName", accounts.hashPass("z"), Group.POWER));
		accounts.addAccount(new Account("RandomAccountName", accounts.hashPass("x"), Group.STANDARD));
		accounts.addAccount(new Account("SuperRandomAccountName", accounts.hashPass("t"), Group.ADMINISTRATOR));
		accounts.addAccount(new Account("AccountName", accounts.hashPass("u"), Group.POWER));
		accounts.addAccount(new Account("User1", accounts.hashPass("g"),Group.STANDARD));
		Account cd = new Account("cd",accounts.hashPass("qwerty"),Group.STANDARD);
		cd.removePermission(Permission.DELETEFILTER);
		cd.removePermission(Permission.CREATEFILTER);
		cd.removePermission(Permission.EDITFILTER);
		accounts.addAccount(cd);

		try {
			Account user = accounts.getAccount("User",accounts.hashPass("qwerty"));
			user.addFilter(new Filter("No Images", "(?i)(?s)<img(.)*?>", "<!-- Killed Image -->"));
			user.addFilter(new Filter("No Script", "(?i)(?s)<script(.)*?</script>", "<!-- Killed Script -->"));
			user.addFilter(new Filter("No Nosccript","(?i)(?s)<noscript(.)*?</noscript>","<!-- Killed Noscript -->"));
			user.addFilter(new Filter("No Objects", "(?i)(?s)<object(.)*?</object>", "<!-- Killed Object -->"));
			user.addInactiveFilter(new Filter("No Applets", "(?i)(?s)<applet(.)*?</applet>", "<!-- Killed Applet -->"));
			Account admin = accounts.getAccount("Admin",accounts.hashPass("admin"));
			user.addFilter(new Filter("No Images", "(?i)(?s)<img(.)*?>", "<!-- Killed Image -->"));
			user.addFilter(new Filter("No Script", "(?i)(?s)<script(.)*?</script>", "<!-- Killed Script -->"));
			user.addFilter(new Filter("No Nosccript","(?i)(?s)<noscript(.)*?</noscript>","<!-- Killed Noscript -->"));
			user.addFilter(new Filter("No Objects", "(?i)(?s)<object(.)*?</object>", "<!-- Killed Object -->"));
			user.addInactiveFilter(new Filter("No Applets", "(?i)(?s)<applet(.)*?</applet>", "<!-- Killed Applet -->"));
			accounts.exportFilters(admin.getActiveFilters(), new File("Newfilters.txt"));
			cd.addFilter(accounts.importFilters(new File("Newfilters.txt")));
		} catch (Exception e) {
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
