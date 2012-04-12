package gui.view;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
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

public class FilterShell {
	private String fName = ""; 
	protected Shell shell;
	protected Object result;
	protected Display display;
	
	//TODO implement method to set the text of the text area upon editing a filter
	//TODO imp1ement method to save the text of the text area to a filter 
	
	
	public FilterShell(Display d){
		display = d;
		fName = "";
	}
	
	public FilterShell(Display d, String s){
		display = d;
		fName = s;
	}
	/**
	 * Open the Filter Shell
	 * @wbp.parser.entryPoint
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	return result;
	}
	

	/**
	 * Create the shell.
	 * @param display
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell(display, SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shell.setSize(786, 510);
		shell.setText(fName + " Filters");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite filterEditComposite = new Composite(shell, SWT.NONE);
		filterEditComposite.setLayout(new GridLayout(1, false));
		
		//Text area edit pane
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
		
			//Jtextarea edit pane 
			JTextArea filterEditTextArea = new JTextArea();
			rootPane.getContentPane().add(filterEditTextArea);
		
		//Bottom button bar composite
		Composite filterEditBtnComposite = new Composite(filterEditComposite, SWT.NONE);
		filterEditBtnComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_filterEditBtnComposite = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_filterEditBtnComposite.widthHint = 761;
		gd_filterEditBtnComposite.heightHint = 30;
		filterEditBtnComposite.setLayoutData(gd_filterEditBtnComposite);
			
			//submit button
			Button btnSubmitButton = new Button(filterEditBtnComposite, SWT.NONE);
			btnSubmitButton.setText("Submit");
			
			//cancel button
			Button btnCancelButton = new Button(filterEditBtnComposite, SWT.NONE);
			btnCancelButton.setText("Cancel");
			
			//Create filter button listener. Open text area with highlighted filters text.
			btnCancelButton.addListener(SWT.Selection, new Listener(){
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
