package gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

public class AccountShell extends Shell {
	private Text accUserNameText;
	private Text accPassText;
	private Text accPassConfirmText;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			AccountShell shell = new AccountShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public AccountShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite accComposite = new Composite(this, SWT.NONE);
		accComposite.setLayout(new GridLayout(1, false));
		
		Composite accNamePassComposite = new Composite(accComposite, SWT.NONE);
		GridData gd_accNamePassComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accNamePassComposite.heightHint = 125;
		gd_accNamePassComposite.widthHint = 429;
		accNamePassComposite.setLayoutData(gd_accNamePassComposite);
		accNamePassComposite.setLayout(new GridLayout(2, false));
		
		Label lblPleaseFillOut = new Label(accNamePassComposite, SWT.NONE);
		lblPleaseFillOut.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblPleaseFillOut.setText("Please fill out the following information for the new account:");
		new Label(accNamePassComposite, SWT.NONE);
		new Label(accNamePassComposite, SWT.NONE);
		
		Label lblAccountName = new Label(accNamePassComposite, SWT.NONE);
		lblAccountName.setText("Account Name:");
		
		accUserNameText = new Text(accNamePassComposite, SWT.BORDER);
		accUserNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(accNamePassComposite, SWT.NONE);
		lblPassword.setText("Password:");
		
		accPassText = new Text(accNamePassComposite, SWT.BORDER | SWT.PASSWORD);
		accPassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConfirmpassword = new Label(accNamePassComposite, SWT.NONE);
		lblConfirmpassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmpassword.setText("ConfirmPassword:");
		
		accPassConfirmText = new Text(accNamePassComposite, SWT.BORDER | SWT.PASSWORD);
		accPassConfirmText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite accUserGroupComposite = new Composite(accComposite, SWT.NONE);
		GridData gd_accUserGroupComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_accUserGroupComposite.heightHint = 74;
		gd_accUserGroupComposite.widthHint = 428;
		accUserGroupComposite.setLayoutData(gd_accUserGroupComposite);
		accUserGroupComposite.setLayout(new GridLayout(2, false));
		
		Label lblUserGroup = new Label(accUserGroupComposite, SWT.NONE);
		lblUserGroup.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblUserGroup.setText("User Group:");
		new Label(accUserGroupComposite, SWT.NONE);
		
		Button btnPowerUser = new Button(accUserGroupComposite, SWT.RADIO);
		btnPowerUser.setText("Power User");
		new Label(accUserGroupComposite, SWT.NONE);
		
		Button btnStandardUser = new Button(accUserGroupComposite, SWT.RADIO);
		btnStandardUser.setText("Standard User");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Create Account");
		setSize(450, 250);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
