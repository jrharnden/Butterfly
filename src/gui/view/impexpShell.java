package gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;
import org.eclipse.swt.layout.FillLayout;
import javax.swing.JToggleButton;

public class impexpShell {

	/**
	 * Create the shell.
	 * @param display
	 */
	public impexpShell() {
	}

	/**
	 * Create contents of the shell.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		Display display = Display.getDefault();
		Shell impexpShell = new Shell(SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		impexpShell.setText("Import/Export Filters");
		impexpShell.setSize(800, 600);
		impexpShell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(impexpShell, SWT.EMBEDDED);
		
		Frame frame = SWT_AWT.new_Frame(composite);
		
		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);
		

		
		
		
		impexpShell.open();
		impexpShell.layout();
		while (!impexpShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
