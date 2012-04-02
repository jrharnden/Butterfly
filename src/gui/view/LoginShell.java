package gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;


public class LoginShell {
	private Text txtUsername;
	private Text txtPassword;
	/**
	 * Launch the application.
	 * @param args
	 */

	public LoginShell() {
	}
		

	/**
	 * Create contents of the shell.
	 * @wbp.parser.entryPoint
	 */
	public void open(){
		Display display = Display.getDefault();
		Shell shlButterflyLogIn = new Shell(SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shlButterflyLogIn.setSize(450, 300);
		//shlButterflyLogIn.setRedraw(false);
		shlButterflyLogIn.setText("Butterfly Log In");
		shlButterflyLogIn.setLayout(new FormLayout());
		
		Composite loginComposite = new Composite(shlButterflyLogIn, SWT.NO_REDRAW_RESIZE | SWT.EMBEDDED);
		FormData fd_loginComposite = new FormData();
		fd_loginComposite.top = new FormAttachment(0);
		fd_loginComposite.left = new FormAttachment(0);
		fd_loginComposite.bottom = new FormAttachment(0, 262);
		fd_loginComposite.right = new FormAttachment(0, 434);
		loginComposite.setLayoutData(fd_loginComposite);
		loginComposite.setLayout(new GridLayout(1, false));
		
		Composite composite_1 = new Composite(loginComposite, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 194;
		gd_composite_1.widthHint = 424;
		composite_1.setLayoutData(gd_composite_1);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
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
		GridData gd_sashForm = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sashForm.widthHint = 425;
		gd_sashForm.heightHint = 53;
		sashForm.setLayoutData(gd_sashForm);
		
		Button btnSubmit = new Button(sashForm, SWT.NONE);
		btnSubmit.setText("Submit");
		
		Button btnCancel = new Button(sashForm, SWT.NONE);
		btnCancel.setText("Cancel");
		sashForm.setWeights(new int[] {1, 1});
		
		shlButterflyLogIn.open();
		shlButterflyLogIn.layout();
		while (!shlButterflyLogIn.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();

			}
		}
	}
}
