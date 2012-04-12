package gui.view;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import storage.*;


public class LoginShell extends Dialog {
	
	private Text txtUsername;
	private Text txtPassword;
	protected Object result;
	protected Shell shell;
	private ApplicationWindow ap;
	public LoginShell(Shell parent){
		super(parent);
	}
	
	/**
	 * Entry point to open the shell
	 * @param a
	 * @return
	 */
	public Object open(ApplicationWindow a) {
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
	
	/**
	 * Create the Shell
	 * @param display
	 */
	protected void createContents() {
		shell = new Shell(getParent(), SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shell.setSize(350, 250);
		shell.setText("Butterfly Log In");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite loginComposite = new Composite(shell, SWT.NONE);
		loginComposite.setLayout(new GridLayout(1, false));
		
		Composite composite_1 = new Composite(loginComposite, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_composite_1.heightHint = 173;
		gd_composite_1.widthHint = 334;
		composite_1.setLayoutData(gd_composite_1);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblNewLabel.setText("Please log in:");
		
		Label lblUsername = new Label(composite_1, SWT.NONE);
		lblUsername.setAlignment(SWT.CENTER);
		GridData gd_lblUsername = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblUsername.widthHint = 59;
		lblUsername.setLayoutData(gd_lblUsername);
		lblUsername.setText("Username:");
		
		txtUsername = new Text(composite_1, SWT.BORDER);
		txtUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblPassword = new Label(composite_1, SWT.NONE);
		lblPassword.setAlignment(SWT.CENTER);
		GridData gd_lblPassword = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPassword.widthHint = 63;
		lblPassword.setLayoutData(gd_lblPassword);
		lblPassword.setText("Password:");
		
		txtPassword = new Text(composite_1, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		SashForm sashForm = new SashForm(loginComposite, SWT.VERTICAL);
		sashForm.setOrientation(SWT.HORIZONTAL);
		GridData gd_sashForm = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
		gd_sashForm.widthHint = 338;
		gd_sashForm.heightHint = 36;
		sashForm.setLayoutData(gd_sashForm);
		
		
		/*
		 * Submit button
		 * Open entering the correct user name and pass should load the correct user profile
		 * for the program.
		 * 
		 * @Event Load user profile in application
		 */
		Button btnSubmit = new Button(sashForm, SWT.NONE);
		btnSubmit.setText("Submit");
		
		btnSubmit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Accounts accounts = new Accounts();
				accounts.loadAccounts();
				String username = (String) txtUsername.getText();
				String pass = (String) txtPassword.getText();
				
				
				if(pass == null || username == null) throw new UnsupportedOperationException();
				
					if(accounts.containsAccount(username, accounts.hashPass(pass))){
						System.out.println("LOGIN SUCCESS!");
						try {
							ap.setAccount(accounts.getAccount(username, accounts.hashPass(pass)));
							ap.setAccounts(accounts);
						} catch (NoSuchAlgorithmException e1){
						
							e1.printStackTrace();
						}
						catch( UnsupportedEncodingException e2) {
							e2.printStackTrace();
						}
						shell.close();
						shell.dispose();
					}else{System.err.println("LOGIN FAILED!");}
				
			}
		});
		
		/*
		 * Cancel Button
		 * Closes out of the login window which should in turn exit out of the main application
		 *
		 * @Event Close window	
		 */
		Button btnCancel = new Button(sashForm, SWT.NONE);
		btnCancel.setText("Cancel");
		sashForm.setWeights(new int[] {1, 1});
		btnCancel.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				System.exit(0);
			}
		});
		}
	
		
	}
