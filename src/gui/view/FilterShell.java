package gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.DropMode;

public class FilterShell extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			FilterShell shell = new FilterShell(display);
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
	public FilterShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite filterEditComposite = new Composite(this, SWT.NONE);
		filterEditComposite.setLayout(new GridLayout(1, false));
		
		Composite filterEditAreaComposite = new Composite(filterEditComposite, SWT.NONE);
		filterEditAreaComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_filterEditAreaComposite = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_filterEditAreaComposite.widthHint = 760;
		gd_filterEditAreaComposite.heightHint = 431;
		filterEditAreaComposite.setLayoutData(gd_filterEditAreaComposite);
		
		Composite composite = new Composite(filterEditAreaComposite, SWT.EMBEDDED);
		
		Frame frame = SWT_AWT.new_Frame(composite);
		
		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);
		rootPane.getContentPane().setLayout(new java.awt.GridLayout(0, 1, 0, 0));
		
		JTextArea filterEditTextArea = new JTextArea();
		filterEditTextArea.setDropMode(DropMode.ON);
		rootPane.getContentPane().add(filterEditTextArea);
		
		Composite filterEditBtnComposite = new Composite(filterEditComposite, SWT.NONE);
		filterEditBtnComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_filterEditBtnComposite = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_filterEditBtnComposite.widthHint = 761;
		gd_filterEditBtnComposite.heightHint = 30;
		filterEditBtnComposite.setLayoutData(gd_filterEditBtnComposite);
		
		Button btnNewButton = new Button(filterEditBtnComposite, SWT.NONE);
		btnNewButton.setText("Submit");
		
		Button btnNewButton_1 = new Button(filterEditBtnComposite, SWT.NONE);
		btnNewButton_1.setText("Cancel");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Filter Edit");
		setSize(786, 510);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
