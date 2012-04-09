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

public class AccountShell extends Dialog {
	private Text accUserNameText;
	private Text accPassText;
	private Text accPassConfirmText;
	protected Object result;
	protected Shell shell;
	private ApplicationWindow ap;
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public AccountShell(Shell parent) {
		super(parent);
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
	
	protected void createContents(){
		shell = new Shell(getParent(), SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shell.setSize(450, 300);
		shell.setText("Create Account");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		
		//Account Name and password composite
		Composite accComposite = new Composite(shell, SWT.NONE);
		accComposite.setLayout(new GridLayout(1, false));
		Composite accNamePassComposite = new Composite(accComposite, SWT.NONE);
		GridData gd_accNamePassComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accNamePassComposite.heightHint = 125;
		gd_accNamePassComposite.widthHint = 429;
		accNamePassComposite.setLayoutData(gd_accNamePassComposite);
		accNamePassComposite.setLayout(new GridLayout(2, false));
		
		//Top label
		Label lblPleaseFillOut = new Label(accNamePassComposite, SWT.NONE);
		lblPleaseFillOut.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblPleaseFillOut.setText("Please fill out the following information for the new account:");
		new Label(accNamePassComposite, SWT.NONE);
		new Label(accNamePassComposite, SWT.NONE);
		
		//Account name
		Label lblAccountName = new Label(accNamePassComposite, SWT.NONE);
		lblAccountName.setText("Account Name:");
		
			//Account data
			accUserNameText = new Text(accNamePassComposite, SWT.BORDER);
			accUserNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//Password
		Label lblPassword = new Label(accNamePassComposite, SWT.NONE);
		lblPassword.setText("Password:");
		
			//Password data
			accPassText = new Text(accNamePassComposite, SWT.BORDER | SWT.PASSWORD);
			accPassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//Password confirm
		Label lblConfirmpassword = new Label(accNamePassComposite, SWT.NONE);
		lblConfirmpassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmpassword.setText("ConfirmPassword:");
		
			// Password confirm data
			accPassConfirmText = new Text(accNamePassComposite, SWT.BORDER | SWT.PASSWORD);
			accPassConfirmText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//User Group information composite
		Composite accUserGroupComposite = new Composite(accComposite, SWT.NONE);
		GridData gd_accUserGroupComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accUserGroupComposite.heightHint = 74;
		gd_accUserGroupComposite.widthHint = 428;
		accUserGroupComposite.setLayoutData(gd_accUserGroupComposite);
		accUserGroupComposite.setLayout(new GridLayout(2, false));
		
		//User group radio button
		Label lblUserGroup = new Label(accUserGroupComposite, SWT.NONE);
		lblUserGroup.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblUserGroup.setText("User Group:");
		new Label(accUserGroupComposite, SWT.NONE);
		
			//Power User
			Button btnPowerUser = new Button(accUserGroupComposite, SWT.RADIO);
			btnPowerUser.setText("Power User");
			new Label(accUserGroupComposite, SWT.NONE);
		
			//Standard User
			Button btnStandardUser = new Button(accUserGroupComposite, SWT.RADIO);
			btnStandardUser.setText("Standard User");
	}

}
