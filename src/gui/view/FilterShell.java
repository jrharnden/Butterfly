package gui.view;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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

import storage.Account;
import storage.Accounts;
import storage.Filter;

import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.regex.PatternSyntaxException;

public class FilterShell {
	private String fName = ""; 
	protected Shell shell;
	protected Object result;
	protected Display display;
	private Composite btnComposite;
	private Text textName;
	private Filter editFilter;
	private Account account;
	private Accounts accounts;
	//TODO implement method to set the text of the text area upon editing a filter
	//TODO imp1ement method to save the text of the text area to a filter 
	
	
	public FilterShell(Display d, String s, Account acc,Accounts accs){
		display = d;
		fName = "";
		account = acc;
		accounts = accs;
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
	
	public void setFilter(Filter f){
		editFilter = f;
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
		
		Composite mainComposite = new Composite(shell, SWT.NONE);
		mainComposite.setLayout(new GridLayout(2, false));
		
		
		Composite nameComposite = new Composite(mainComposite, SWT.NONE);
		nameComposite.setLayout(new GridLayout(2, false));
		GridData gd_nameComposite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_nameComposite.heightHint = 36;
		gd_nameComposite.widthHint = 627;
		nameComposite.setLayoutData(gd_nameComposite);
		
		Label lblName = new Label(nameComposite, SWT.NONE);
		lblName.setText("Name:");
		
		textName = new Text(nameComposite, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//Right side Button composite
		btnComposite = new Composite(mainComposite, SWT.NONE);
		btnComposite.setLayout(new GridLayout(1, false));
		GridData gd_btnComposite = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 5);
		gd_btnComposite.widthHint = 137;
		gd_btnComposite.heightHint = 471;
		btnComposite.setLayoutData(gd_btnComposite);
		
		//Inner button composite
		Composite innerComposite = new Composite(btnComposite, SWT.NONE);
		innerComposite.setLayout(new GridLayout(1, false));
		GridData gd_innerComposite = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_innerComposite.widthHint = 131;
		gd_innerComposite.heightHint = 218;
		innerComposite.setLayoutData(gd_innerComposite);
		
			//Save Button
			Button btnSave = new Button(innerComposite, SWT.NONE);
			GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_btnSave.widthHint = 120;
			btnSave.setLayoutData(gd_btnSave);
			btnSave.setText("Save");
			
			
		
			//Cancel Button
			Button btnCancel = new Button(innerComposite, SWT.NONE);
			GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_btnCancel.widthHint = 119;
			btnCancel.setLayoutData(gd_btnCancel);
			btnCancel.setText("Cancel");
			
		
			//Check button composite
			Composite chkComposite = new Composite(innerComposite, SWT.NONE);
			chkComposite.setLayout(new GridLayout(1, false));
			GridData gd_chkComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
			gd_chkComposite.heightHint = 148;
			gd_chkComposite.widthHint = 116;
			chkComposite.setLayoutData(gd_chkComposite);
		
				//Case Sensitive
				Button btnCaseSensitive = new Button(chkComposite, SWT.CHECK);
				btnCaseSensitive.setText("Case Sensitive");
				
				//Unicode 
				Button btnUnicode = new Button(chkComposite, SWT.CHECK);
				GridData gd_btnUnicode = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
				gd_btnUnicode.widthHint = 79;
				btnUnicode.setLayoutData(gd_btnUnicode);
				btnUnicode.setText("Unicode");
		
		//Regular Express text composite
		Label lblRegularExpression = new Label(mainComposite, SWT.NONE);
		lblRegularExpression.setBounds(0, 0, 55, 15);
		lblRegularExpression.setText("  Regular Expression:");
		
		Composite regExpComposite = new Composite(mainComposite, SWT.NONE);
		regExpComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_regExpComposite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_regExpComposite.widthHint = 627;
		gd_regExpComposite.heightHint = 192;
		regExpComposite.setLayoutData(gd_regExpComposite);

		Composite composite_1 = new Composite(regExpComposite, SWT.EMBEDDED);
		
		Frame frame = SWT_AWT.new_Frame(composite_1);
		
		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);
		rootPane.getContentPane().setLayout(new java.awt.GridLayout(1, 0, 0, 0));
		
			//Regular Expression text area
			final JTextArea textAreaRegExp = new JTextArea();
			rootPane.getContentPane().add(textAreaRegExp);
		
		//Replacement String Text Area Composite
		Label lblReplacement = new Label(mainComposite, SWT.NONE);
		lblReplacement.setText("  Replacement String:");
				
		Composite repComposite = new Composite(mainComposite, SWT.NONE);
		repComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_repComposite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_repComposite.widthHint = 627;
		gd_repComposite.heightHint = 192;
		repComposite.setLayoutData(gd_repComposite);
		
		Composite composite_2 = new Composite(repComposite, SWT.EMBEDDED);
		
		Frame frame_1 = SWT_AWT.new_Frame(composite_2);
		
		Panel panel_1 = new Panel();
		frame_1.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JRootPane rootPane_1 = new JRootPane();
		panel_1.add(rootPane_1);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		rootPane_1.getContentPane().setLayout(gridBagLayout);
		
			// Replacement String text area
			final JTextArea textAreaReplacement = new JTextArea();
			GridBagConstraints gbc_textAreaReplacement = new GridBagConstraints();
			gbc_textAreaReplacement.fill = GridBagConstraints.BOTH;
			gbc_textAreaReplacement.gridx = 0;
			gbc_textAreaReplacement.gridy = 0;
			rootPane_1.getContentPane().add(textAreaReplacement, gbc_textAreaReplacement);
			
		if(editFilter !=null){
			textAreaReplacement.setText(editFilter.getReplaceWith());
			textAreaRegExp.setText(editFilter.getRegex());
			textName.setText(editFilter.getName());
		}
		
		//Button Listeners
		btnSave.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e){
				switch (e.type){
				case SWT.Selection:
					String replaceText = textAreaReplacement.getText();
					String regexText = textAreaRegExp.getText();
					String name = textName.getText();
					if(!name.equals("")) {
						if(editFilter != null){
							account.removeFilter(editFilter.getId());
							editFilter.changeName(name);
							editFilter.changeReplaceWith(replaceText);
							try {
								editFilter.changeRegex(regexText);
								account.addFilter(editFilter);
							}
							catch(PatternSyntaxException pse){
								//TODO make this a dialogue box that pops up
								System.err.println(pse.getDescription());
							}
						}
						else{
							try {
								editFilter = new Filter(name,regexText,replaceText);
								account.addFilter(editFilter);
							}
							catch(PatternSyntaxException pse){
								//TODO make this a dialogue box that pops up
								System.err.println(pse.getDescription());
							}
						}

						accounts.saveAccounts();
						shell.close();
						shell.dispose();
					}
					else {
						//TODO make this a dialogue box that pops up
						System.err.println("Filter name cannot be empty!");
					}
				}
			}
		});
		
		btnCancel.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e){
				switch (e.type){
				case SWT.Selection:
					shell.close();
					shell.dispose();
				}
			}
		});
	
				
	}
}
