package gui.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import storage.Account;
import storage.Accounts;
import storage.Group;

public class AccountShell extends Dialog {
	
	//Static variables
	private static final int POWER = 0;
	private static final int STANDARD = 1;
	private static final int ACCNUM = 2;
	
	//Change password lock
	private boolean changepassword = false;
	
	//Data for change password defaults
	private String accname = "";
	Group group;
	
	private Label errorLabel;
	private String userGroup = "";
	Button[] userGroupRadio = new Button[ACCNUM];
	private Text accUserNameText;
	private Text accPassText;
	private Text accPassConfirmText;
	protected Object result;
	protected Shell shell;
	private ApplicationWindow ap;
	
	
	/**
	 * Create the shell.
	 * @param display
	 * @wbp.parser.constructor
	 */
	public AccountShell(Shell parent) {
		super(parent);
		//createContents();
	}
	
	/**
	 * Constructor used for creating the change password window. When used the user groups will be defaulted and set disabled.
	 * The user account name will be filled in and also disabled. Password labels will be changed.
	 * @param parent shell
	 * @param accName account name of the user
	 * @param group user group of the user
	 */
	public AccountShell(Shell parent, String accName, Group group){
		super(parent);
		changepassword = true;
		accname = accName;
		this.group = group;
		
	}
	
	/**
	 * Entry point to open the shell
	 * @param a
	 * @return
	 */
	public Object open(ApplicationWindow a){
		ap = a;
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private String getPassLabel(boolean changepass){
		String passlabel;
		if (changepass){
			passlabel = "New Password:";
		} else {
			passlabel = "Password:";
		}
		return passlabel;
	}
	

	private String getPassLabelConf(boolean changepass){
		String passlabelconf;
		if (changepass){
			passlabelconf = "Confirm Password:";	
		} else {
			passlabelconf = "Confirm Password:";
		}
		return passlabelconf;
	}
	
	private String getWindowName(boolean changepass){
		String header;
		if (changepass){
			header = "Change Password";
		} else
			header = "Create Account";
		return header;
	}
	
	/**
	 * Takes in account credentials and checks to make sure they are set. Compares the values of the
	 * passwords to ensure that they are the same. If the criteria aren't met it sets a label error
	 * message which is displayed on the window.
	 * @param userName
	 * @param pass
	 * @param passConf
	 * @param userGroup
	 * @return 
	 */
	private boolean validate(String userName, String pass, String passConf, String userGroup){
		String errBlankPass = "Error: Please fill out the password fields";
		String errMismatchPass = "Error: Passwords do no match";
		String errBlankAccount = "Error: Please enter an Account Name";
		String errBlankUserGroup = "Error: Please select a User Group";
		boolean set = false;
		Accounts a = new Accounts();
		a.loadAccounts();
		if (userName.isEmpty()) {
			errorLabel.setText(errBlankAccount);
		} else if (pass.isEmpty() || passConf.isEmpty()){
			errorLabel.setText(errBlankPass);
		} else if (userGroup.isEmpty()){
			errorLabel.setText(errBlankUserGroup);
		} else if(a.getAccount(userName)!=null){
			errorLabel.setText("Error: Account already exists");
		
		} else if (pass.equals(passConf)) {
				set = true;
		} else {
				errorLabel.setText(errMismatchPass);
		}
		return set;
	}	
	
	protected void createContents(){
		shell = new Shell(getParent(), SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shell.setSize(450, 300);
		shell.setLocation(200,200);
		shell.setText(getWindowName(changepassword));
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		
		//Account Name and password composite
		Composite accComposite = new Composite(shell, SWT.NONE);
		accComposite.setLayout(new GridLayout(1, false));
		Composite accNamePassComposite = new Composite(accComposite, SWT.BORDER);
		GridData gd_accNamePassComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accNamePassComposite.heightHint = 112;
		gd_accNamePassComposite.widthHint = 436;
		accNamePassComposite.setLayoutData(gd_accNamePassComposite);
		accNamePassComposite.setLayout(new GridLayout(2, false));
		
		//Top label
		Label lblPleaseFillOut = new Label(accNamePassComposite, SWT.NONE);
		GridData gd_lblPleaseFillOut = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_lblPleaseFillOut.heightHint = 21;
		lblPleaseFillOut.setLayoutData(gd_lblPleaseFillOut);
		lblPleaseFillOut.setText("Please fill out the following information for the new account:");
		
		//Account name
		Label lblAccountName = new Label(accNamePassComposite, SWT.NONE);
		lblAccountName.setText("Account Name:");
		
			//Account data
			accUserNameText = new Text(accNamePassComposite, SWT.BORDER);
			accUserNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
		//Password
		Label lblPassword = new Label(accNamePassComposite, SWT.NONE);
		lblPassword.setText(getPassLabel(changepassword));
			
			//If changing the password for the account set the account name and disable the field
			if (changepassword) {
				accUserNameText.setText(accname);
				accUserNameText.setEnabled(false);
			}
		
			//Password data
			accPassText = new Text(accNamePassComposite, SWT.BORDER | SWT.PASSWORD);
			accPassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
		
		//Password confirm
		Label lblConfirmpassword = new Label(accNamePassComposite, SWT.NONE);
		lblConfirmpassword.setText(getPassLabelConf(changepassword));
		
			// Password confirm data
			accPassConfirmText = new Text(accNamePassComposite, SWT.BORDER | SWT.PASSWORD);
			accPassConfirmText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//User Group information composite
		Composite accUserGroupComposite = new Composite(accComposite, SWT.BORDER);
		GridData gd_accUserGroupComposite = new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 1, 1);
		gd_accUserGroupComposite.heightHint = 71;
		gd_accUserGroupComposite.widthHint = 435;
		accUserGroupComposite.setLayoutData(gd_accUserGroupComposite);
		accUserGroupComposite.setLayout(new GridLayout(2, false));
		
		//User group radio button
		Label lblUserGroup = new Label(accUserGroupComposite, SWT.NONE);
		lblUserGroup.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblUserGroup.setText("User Group:");
		new Label(accUserGroupComposite, SWT.NONE);
		
			//Power User
			userGroupRadio[POWER] = new Button(accUserGroupComposite, SWT.RADIO);
			userGroupRadio[POWER].setText("Power");
			new Label(accUserGroupComposite, SWT.NONE);
			
			//Standard User
			userGroupRadio[STANDARD] = new Button(accUserGroupComposite, SWT.RADIO);
			userGroupRadio[STANDARD].setText("Standard");
			
			//If the window was opened for changing the password disable the fields and select the right group.
			if (changepassword){
				if (group == Group.POWER) {
					userGroupRadio[POWER].setSelection(true);
				} else if (group == Group.STANDARD) {
					userGroupRadio[STANDARD].setSelection(true);
				}
				userGroupRadio[STANDARD].setEnabled(false);
				userGroupRadio[POWER].setEnabled(false);
			}
			
		//Error Label composite
		Composite accErrorComposite = new Composite(accComposite, SWT.NONE);
		accErrorComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_accErrorComposite = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_accErrorComposite.widthHint = 435;
		gd_accErrorComposite.heightHint = 22;
		accErrorComposite.setLayoutData(gd_accErrorComposite);
		
			//Error Label
			errorLabel = new Label(accErrorComposite, SWT.NONE);
			errorLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			
		//Account button bar	
		Composite accBtnBar = new Composite(accComposite, SWT.BORDER);
		accBtnBar.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_accBtnBar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accBtnBar.widthHint = 436;
		gd_accBtnBar.heightHint = 30;
		accBtnBar.setLayoutData(gd_accBtnBar);
		
			//Create the new account
			Button accBtnSave = new Button(accBtnBar, SWT.NONE);
			accBtnSave.setText("Save");
			
			accBtnSave.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){  
					String username = accUserNameText.getText();
					String pass = accPassText.getText();
					String confPass = accPassConfirmText.getText();
					
					//Iterate through the user groups and check for which one was selected
					//Set UserGroup to the buttons name (ie Power User/Standard User)
					for (int i=0; i < ACCNUM; i++){
						if (userGroupRadio[i].getSelection()) {
							userGroup = userGroupRadio[i].getText();
						}
					}
					
					
					//TODO add in check to changepassword for password changing 
					if(changepassword){
						Accounts a = new Accounts();
						a.loadAccounts();
						a.getAccount(accname).setPass(a.hashPass(accPassText.getText()));
						a.saveAccounts();
						shell.close();
						shell.dispose();
						
					}
					else if (validate(username, pass, confPass, userGroup))	{
							Accounts a = new Accounts();
							a.loadAccounts();
							Account newAcc= new Account(username, a.hashPass(pass), Group.valueOf(userGroup.toUpperCase()));
							
							a.addAccount(newAcc);
								
							a.saveAccounts();

							shell.close();
							shell.dispose();
						}
					}
				}
			
			);
			
			//Cancel Button
			Button accBtnCancel = new Button(accBtnBar, SWT.NONE);
			accBtnCancel.setText("Cancel");
			
			//close the shell upon cancel
			accBtnCancel.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						shell.close();
						shell.dispose();
					}
				}
			}
			);
	}
}
