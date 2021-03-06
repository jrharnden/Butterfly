package gui.view;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.swing.JFileChooser;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import storage.Account;
import storage.Accounts;
import storage.Filter;
import storage.Group;
import storage.Permission;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class EditShell {
	private boolean opened_user_account = false;
	
	//Used currently to just add the reset password button
	private boolean opened_user_group = false;
	
	private String sName = "";
	JFileChooser chooser;
	protected Shell shell;
	protected Object result;
	protected Display display;
	
	//User account that opened the window
	protected Account account;
	
	//Account selected that should be edited 
	protected Account edit_account;
	
	//User group defaults to be edited
	protected Group edit_group;
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text textHandle;
	private ArrayList<Filter> imported;
	private Accounts accounts;
	private File file;
	private Button btnCreateFilters, btnDeleteFilters, btnEditFilters, btnSetProxyListening;
	
	/**
	 * Import/Export constructor 
	 * @param acc Account that is importing/exporting
	 * @param d default display
	 * @param s string import/export
	 */
	public EditShell(Display d, Account acc, String s){
		display =d;
		sName = s;
		account = acc;
	}
	
	/**
	 * Constructor for editing user group settings
	 * @param d
	 * @param group
	 */
	public EditShell(Display d, Group group, Accounts acc){
		display = d;
		edit_group = group;
		sName = "Edit Default Group Settings";
		opened_user_account = false;
		opened_user_group = true;
		accounts = acc;
	}
	
	/**
	 * Constructor for editing user group settings
	 * @param d
	 * @param group
	 */
	public EditShell(Display d, Group group, Account admin, Accounts acc){
		display = d;
		edit_group = group;
		sName = "Edit Default Group Settings";
		opened_user_account = false;
		opened_user_group = true;
		accounts = acc;
		account = admin;
	}
	
	/**
	 * Constructor for editing user account settings
	 * @param d display default
	 * @param accName account being edited
	 * @param group Group
	 */
	public EditShell(Display d, Account accToEdit,Account adminAccount, Accounts acc){
		display = d;
		edit_account = accToEdit;
		account = adminAccount;
		sName = "Edit: " + edit_account.getName() + " Settings";
		opened_user_account = true;
		accounts = acc;
	}
	
	public EditShell(Display d, Account accToEdit, String s, Accounts a) {
		display =d;
		sName = s;
		account = accToEdit;
		accounts = a;
	}

	/**
	 * Gets the inactive filter label for the window
	 * User Group/User/import export edit
	 * @return String inactive label
	 */
	public String getInactiveLabel(){
		String out  = "";
		//If the window was opened for user account editing
		if (opened_user_account){
			out = "Filters for " + edit_account.getName();
		
		}
		else if(opened_user_group){out = "Default Filters";}
		else {
			//If the window was opened for export
			if (sName == ApplicationWindow.EXPORT){
				out = "Filters to be exported";
				
			//If the window was opened for import
			} else {
				out = "Filters to be Imported";
			}
		}
		return out;
		
	}
	
	/**
	 * Gets the active filter label for the window
	 * User Group/User/import export edit
	 * @return String active label
	 */
	public String getActiveLabel(){
		String out = "";
		if (opened_user_account || opened_user_group){
			out = "Administrator's Filters";
		} else {
			//If the window was opened for export
			if (sName == ApplicationWindow.EXPORT){
				out = account.getName() + " Filters";
				
			//If the window was opened for import	
			} else {
				out = "Filters from File";
			}
		}
		return out;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object open(){
		
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
	 * Create contents of the shell.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
		shell.setText(sName + " Filters");
		shell.setSize(786, 600);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite filterComposite = new Composite(shell, SWT.NONE);
		formToolkit.paintBordersFor(filterComposite);
		filterComposite.setLayout(new GridLayout(1, false));
		
		Composite filterComposite_1 = new Composite(filterComposite, SWT.BORDER);
		GridData gd_filterComposite_1 = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterComposite_1.widthHint = 770;
		gd_filterComposite_1.heightHint = 525;
		filterComposite_1.setLayoutData(gd_filterComposite_1);
		formToolkit.adapt(filterComposite_1);
		formToolkit.paintBordersFor(filterComposite_1);
		filterComposite_1.setLayout(new GridLayout(3, false));
		
		//Active filter list/Filters to be Exported
		Label lblActiveFilters = new Label(filterComposite_1, SWT.NONE);
		lblActiveFilters.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblActiveFilters, true, true);
		lblActiveFilters.setText(getActiveLabel());
		
		new Label(filterComposite_1, SWT.NONE);
		
		//Inactive filter list/Filters to be imported
		Label lblInactiveFilters = new Label(filterComposite_1, SWT.NONE);
		lblInactiveFilters.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblInactiveFilters, true, true);
		lblInactiveFilters.setText(getInactiveLabel());
		
	
		//Active Filer Composite
		Composite filterActiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterActiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterActiveComposite.heightHint = 511;
		gd_filterActiveComposite.widthHint = 333;
		
		filterActiveComposite.setLayoutData(gd_filterActiveComposite);
		formToolkit.adapt(filterActiveComposite);
		formToolkit.paintBordersFor(filterActiveComposite);
		filterActiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		//Active List
		final ListViewer filterActiveListViewer = new ListViewer(filterActiveComposite, SWT.BORDER | SWT.V_SCROLL);
		List filterActiveList_1 = filterActiveListViewer.getList();
		//Populate list with all filters if export
		if(sName.equalsIgnoreCase("Export")){
			
				for(Filter f: account.getAllFilters()){
					filterActiveList_1.add(f.toString());
						
					}
				
		}
		if(opened_user_account) {
			for(Filter f: account.getAllFilters()){
				boolean identical = false;
				for (Filter df: edit_account.getDefaultFilters()) {
					if (f.getId() == df.getId()) {
						identical=true;;
					}
				}
				if (!identical) {
					filterActiveList_1.add(f.toString());
				}
			}
		} else if (opened_user_group) {
			for(Filter f: account.getAllFilters()){
				boolean identical = false;
				for (Filter df: accounts.getDefaultFilters(edit_group)) {
					if (f.getId() == df.getId()) {
						identical=true;;
					}
				}
				if (!identical) {
					filterActiveList_1.add(f.toString());
				}
			}
		}
		//Button composite
		Composite filterBtnComposite = new Composite(filterComposite_1, SWT.NONE);
		filterBtnComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData gd_filterBtnComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterBtnComposite.heightHint = 510;
		gd_filterBtnComposite.widthHint = 85;
		filterBtnComposite.setLayoutData(gd_filterBtnComposite);
		formToolkit.adapt(filterBtnComposite);
		formToolkit.paintBordersFor(filterBtnComposite);
		
		//North button composite
		Composite filterBtnComposite_NORTH = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_NORTH);
		formToolkit.paintBordersFor(filterBtnComposite_NORTH);
		filterBtnComposite_NORTH.setLayout(new GridLayout(1, false));
		
		
		//Center Button Composite
		Composite filterBtnComposite_CENTER = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_CENTER);
		formToolkit.paintBordersFor(filterBtnComposite_CENTER);
		filterBtnComposite_CENTER.setLayout(new FillLayout(SWT.HORIZONTAL));
		
			//Add items from inactive to active < / move from "User Filters" to "to be exported"
			Button btnAdd = new Button(filterBtnComposite_CENTER, SWT.NONE);
			formToolkit.adapt(btnAdd, true, true);
			btnAdd.setText("<");
			if (opened_user_account) {
				btnAdd.setEnabled(false);
			}
			
			
			//Remove items from active to inactive > / move from "File filters" to "Filters to be imported"
			Button btnRemove = new Button(filterBtnComposite_CENTER, SWT.NONE);
			formToolkit.adapt(btnRemove, true, true);
			btnRemove.setText(">");
			
		//South Button Composite
		Composite filterBtnComposite_SOUTH = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_SOUTH);
		formToolkit.paintBordersFor(filterBtnComposite_SOUTH);
		filterBtnComposite_SOUTH.setLayout(new GridLayout(1, false));
			
	
		//Inactive filter composite
		Composite filterInactiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterInactiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterInactiveComposite.heightHint = 511;
		gd_filterInactiveComposite.widthHint = 333;
		filterInactiveComposite.setLayoutData(gd_filterInactiveComposite);
		formToolkit.adapt(filterInactiveComposite);
		formToolkit.paintBordersFor(filterInactiveComposite);
		filterInactiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
			//Inactive filter list
			final ListViewer filterInactiveListViewer = new ListViewer(filterInactiveComposite, SWT.BORDER | SWT.V_SCROLL);
			List inactiveList = filterInactiveListViewer.getList();
			if(opened_user_account){
				for(Filter f: edit_account.getAllFilters())
					inactiveList.add(f.toString());
			} else if (opened_user_group) {
				for(Filter f: accounts.getDefaultFilters(edit_group)) {
					inactiveList.add(f.toString());
				}
			}
			Composite btnBarComposite = new Composite(filterComposite, SWT.BORDER);
			btnBarComposite.setLayout(new GridLayout(9, false));
			GridData gd_btnBarComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
			gd_btnBarComposite.heightHint = 43;
			gd_btnBarComposite.widthHint = 771;
			btnBarComposite.setLayoutData(gd_btnBarComposite);
			btnBarComposite.setBounds(0, 0, 64, 64);
			formToolkit.adapt(btnBarComposite);
			formToolkit.paintBordersFor(btnBarComposite);
			
	//If the shell was opened for import/export
	if (opened_user_account || opened_user_group) {
		
		Label lblNewLabel = new Label(btnBarComposite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 98;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("User Permissions:");
		
		// ****************** PERMISSIONS *******************************************************
		//Create button check box
		btnCreateFilters = new Button(btnBarComposite, SWT.CHECK);
		GridData gd_btnCreateFilters = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCreateFilters.widthHint = 87;
		btnCreateFilters.setLayoutData(gd_btnCreateFilters);
		formToolkit.adapt(btnCreateFilters, true, true);
		
		btnCreateFilters.setText("Create Filters");
		/*
				btnCreateFilters.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if (btnCreateFilters.getSelection()) {
							System.out.println("Checked");
						} else {
							System.out.println("Unchecked");
						}
					}
				});
		*/
		
		//Edit filters check box
		btnEditFilters = new Button(btnBarComposite, SWT.CHECK);
		GridData gd_btnEditFilters = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnEditFilters.widthHint = 73;
		btnEditFilters.setLayoutData(gd_btnEditFilters);
		formToolkit.adapt(btnEditFilters, true, true);
		btnEditFilters.setText("Edit Filters");
		/*btnEditFilters.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btnEditFilters.getSelection()) {
					System.out.println("Checked");
				} else {
					System.out.println("Unchecked");
				}
			}
		});
		*/
		
		if(opened_user_account && edit_account.getPermissions().contains(Permission.CREATEFILTER)) {
			btnCreateFilters.setSelection(true);
		}
		else if(opened_user_group){
			switch(edit_group){
			case ADMINISTRATOR:
				if(accounts.getAdminPermissions().contains(Permission.CREATEFILTER))
				btnCreateFilters.setSelection(true);
				break;
			case POWER:
				if(accounts.getPowerPermissions().contains(Permission.CREATEFILTER))
					
				btnCreateFilters.setSelection(true);
				break;
			case STANDARD:
				if(accounts.getStandardPermissions().contains(Permission.CREATEFILTER))
				btnCreateFilters.setSelection(true);
			}
		}
		
		//Delete filters check box
		btnDeleteFilters = new Button(btnBarComposite, SWT.CHECK);
		GridData gd_btnDeleteFilters = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDeleteFilters.widthHint = 87;
		btnDeleteFilters.setLayoutData(gd_btnDeleteFilters);
		formToolkit.adapt(btnDeleteFilters, true, true);
		btnDeleteFilters.setText("Delete Filters");
/*		btnDeleteFilters.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btnDeleteFilters.getSelection()) {
					System.out.println("Checked");
				} else {
					System.out.println("Unchecked");
				}
			}
		});
		*/
	
		if(opened_user_account && edit_account.getPermissions().contains(Permission.EDITFILTER)) {
			btnEditFilters.setSelection(true);
		}
		else if(opened_user_group){
			switch(edit_group){
			case ADMINISTRATOR:
				if(accounts.getAdminPermissions().contains(Permission.EDITFILTER))
				btnEditFilters.setSelection(true);
				break;
			case POWER:
				if(accounts.getPowerPermissions().contains(Permission.EDITFILTER))
				btnEditFilters.setSelection(true);
				break;
			case STANDARD:
				if(accounts.getStandardPermissions().contains(Permission.EDITFILTER))
					btnEditFilters.setSelection(true);
			}
		}
		
		//change port check box
		btnSetProxyListening = new Button(btnBarComposite, SWT.CHECK);
		//Make it pretty
		boolean expand = true;
		if (opened_user_account){expand = false;}
		GridData gd_btnSetProxyListening = new GridData(SWT.LEFT, SWT.CENTER, expand, false, 1, 1);
		gd_btnSetProxyListening.widthHint = 130;
		btnSetProxyListening.setLayoutData(gd_btnSetProxyListening);
		formToolkit.adapt(btnSetProxyListening, true, true);
		btnSetProxyListening.setText("Change Port Number");
/*		
				btnSetProxyListening.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if (btnSetProxyListening.getSelection()) {
							System.out.println("Checked");
						} else {
							System.out.println("Unchecked");
						}
					}
				});
				
		*/
		if(opened_user_account && edit_account.getPermissions().contains(Permission.DELETEFILTER)) {
			btnDeleteFilters.setSelection(true);
		}
		
		else if(opened_user_group){
			switch(edit_group){
			case ADMINISTRATOR:
				if(accounts.getAdminPermissions().contains(Permission.DELETEFILTER))
					btnDeleteFilters.setSelection(true);
				break;
			case POWER:
				if(accounts.getPowerPermissions().contains(Permission.DELETEFILTER))
					btnDeleteFilters.setSelection(true);
				break;
			case STANDARD:
				if(accounts.getStandardPermissions().contains(Permission.DELETEFILTER))
					btnDeleteFilters.setSelection(true);
				break;
			}
		}
		
		
	//Change password button
		if (opened_user_account){	
			Button btnResetPassword = new Button(btnBarComposite, SWT.NONE);
			formToolkit.adapt(btnResetPassword, true, true);
			btnResetPassword.setText("Reset Password");
			
			btnResetPassword.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e){
					switch (e.type) {
					case SWT.Selection:
						edit_account = accounts.getAccount(edit_account);
						edit_account.setPass(accounts.hashPass(""));
						accounts.saveAccounts();
					}
				}
			});
		
		}
		
		if(opened_user_account && edit_account.getPermissions().contains(Permission.SETPORT)) {
			btnSetProxyListening.setSelection(true);
		}
		else if(opened_user_group){
			switch(edit_group){
			case ADMINISTRATOR:
				if(accounts.getAdminPermissions().contains(Permission.SETPORT))
				btnSetProxyListening.setSelection(true);
				break;
			case POWER:
				if(accounts.getPowerPermissions().contains(Permission.SETPORT))
				
				btnSetProxyListening.setSelection(true);
				break;
			case STANDARD:
				if(accounts.getStandardPermissions().contains(Permission.SETPORT))
				btnSetProxyListening.setSelection(true);
			}
		}
		
		Label label = new Label(btnBarComposite, SWT.SEPARATOR | SWT.VERTICAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_label.heightHint = 25;
		label.setLayoutData(gd_label);
		formToolkit.adapt(label, true, true);
		
		
		// if editing user account
	if (opened_user_account) {
			// add remove button to delete filters
			Button btnREmove = new Button(filterBtnComposite_SOUTH, SWT.NONE);
			GridData gd_btnREmove = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_btnREmove.widthHint = 77;
			btnREmove.setLayoutData(gd_btnREmove);
			formToolkit.adapt(btnREmove, true, true);
			btnREmove.setText("Remove");
			
			// remove listener
			btnREmove.addListener(SWT.Selection, new Listener(){
				@Override
				public void handleEvent(Event e) {
					if (e.type == SWT.Selection) {
					    List al = filterActiveListViewer.getList();
						List il = filterInactiveListViewer.getList();
						try{
							String filter = il.getSelection()[0];
							String[] fil = filter.split(":");
							if(opened_user_account){
								accounts.getAccount(edit_account).removeFilter(Integer.parseInt(fil[0]));
								Filter f = accounts.getAccount(edit_account).removeDefaultFilter(Integer.parseInt(fil[0]));
								if (f!=null) {
									al.add(filter);
								}
							}
							
							il.remove(filter);
							if(!opened_user_account)
								al.add(filter);
						}catch(ArrayIndexOutOfBoundsException err){
						}
					}
				}
			});
			
			// User group label in the north center panel
			Label lblUserPermissions = new Label(filterBtnComposite_NORTH, SWT.NONE);
			formToolkit.adapt(lblUserPermissions, true, true);
			lblUserPermissions.setText("User Group:");
			
			final Button btnPower = new Button(filterBtnComposite_NORTH, SWT.RADIO);
			formToolkit.adapt(btnPower, true, true);
			btnPower.setText("Power");
			
			final Button btnStandard = new Button(filterBtnComposite_NORTH, SWT.RADIO);
			formToolkit.adapt(btnStandard, true, true);
			btnStandard.setText("Standard");
			
			if (accounts.getAccount(edit_account).getGroup() == Group.POWER) {btnPower.setSelection(true);}
			if (accounts.getAccount(edit_account).getGroup() == Group.STANDARD) {btnStandard.setSelection(true);}

			// power selection listener
			btnPower.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (btnPower.getSelection() == true){
						accounts.getAccount(edit_account).setGroup(Group.POWER);
					}
				}
			});
			
			
			// Standard seleciton listener
			btnStandard.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
					if (btnStandard.getSelection() == true){
						accounts.getAccount(edit_account).setGroup(Group.STANDARD);
					}
				}
			});
			
		}
				
			
					
				//Window was opened for editing user account/user group
				//Add the extra check boxes for accounts and user groups
				//Add reset password for accounts
			} else {
				// Import/Export Button
				Button btnImport = new Button(btnBarComposite, SWT.NONE);
				GridData gd_btnImport = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_btnImport.widthHint = 75;
				btnImport.setLayoutData(gd_btnImport);
				btnImport.setText(sName);
				formToolkit.adapt(btnImport, true, true);
				
				btnImport.addListener(SWT.Selection, new Listener(){
					public void handleEvent(Event e){
						switch (e.type){
						case SWT.Selection:
							FileDialog dialog = new FileDialog(shell, SWT.NULL);
							String path = dialog.open();
							if (path != null) {
							  file = new File(path);
							  if (file.isFile()){
								  displayFiles(new String[] { file.toString()});
							  	  Accounts accounts = new Accounts();
								  List filterActiveList = filterActiveListViewer.getList();
							  	  if(sName.equalsIgnoreCase("IMPORT")){
							  	  imported = accounts.importFilters(file);

							  	//Active List
									for(Filter fil: imported)
										filterActiveList.add(fil.toString());
							  	  }
							  	  
									
							  
							  	  
							  }
							  else
								  displayFiles(file.list());
							}
						}
					}
				});
				
					//File handle for import/export
					textHandle = new Text(btnBarComposite, SWT.BORDER);
					GridData gd_textHandle = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
					gd_textHandle.widthHint = 371;
					textHandle.setLayoutData(gd_textHandle);
					formToolkit.adapt(textHandle, true, true);
		}
				
				
				//Save the changes
				Button btnSave = new Button(btnBarComposite, SWT.NONE);
				GridData gd_btnSave = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_btnSave.widthHint = 75;
				btnSave.setLayoutData(gd_btnSave);
				formToolkit.adapt(btnSave, true, true);
				btnSave.setText("Save");
				
				btnSave.addListener(SWT.Selection, new Listener(){
					public void handleEvent(Event e){
						switch (e.type){
						case SWT.Selection:
							if(sName.equalsIgnoreCase("IMPORT")){
							List importFilters = filterInactiveListViewer.getList();
							String[] filtersToImport = importFilters.getItems();
							for(Filter f: imported){
								String filterName = f.toString();
								for(int i = 0; i < filtersToImport.length; ++i){
									if(filterName.equals(filtersToImport[i])){
										account.addFilter(f);

									}
								}
							}
							
							accounts.saveAccounts();
							
							
								
							}
							else if(sName.equalsIgnoreCase("EXPORT")){
								ArrayList<Filter> filtersToExport = new ArrayList<Filter>();
								List exportFilters = filterInactiveListViewer.getList();
								String[] filtersStrings = exportFilters.getItems();
								for(Filter f: account.getAllFilters()){
									String filterName = f.toString();
									for(int i = 0; i < filtersStrings.length; ++i){
										if(filterName.equals(filtersStrings[i])){
											filtersToExport.add(f);
										}
									}
								}
								if(!accounts.exportFilters(filtersToExport, file)) return;
							}
							else if(opened_user_account){
								
								if(btnCreateFilters.getSelection()){
									accounts.getAccount(edit_account).addPermission(Permission.CREATEFILTER);
								}
								else accounts.getAccount(edit_account).removePermission(Permission.CREATEFILTER);
								if(btnDeleteFilters.getSelection()){
									accounts.getAccount(edit_account).addPermission(Permission.DELETEFILTER);
								}
								else accounts.getAccount(edit_account).removePermission(Permission.DELETEFILTER);
								if(btnEditFilters.getSelection()){
									accounts.getAccount(edit_account).addPermission(Permission.EDITFILTER);
								}
								else accounts.getAccount(edit_account).removePermission(Permission.EDITFILTER);
								if(btnSetProxyListening.getSelection()){
									accounts.getAccount(edit_account).addPermission(Permission.SETPORT);
								}
								else accounts.getAccount(edit_account).removePermission(Permission.SETPORT);
								
								List exportFilters = filterInactiveListViewer.getList();
								String[] filtersStrings = exportFilters.getItems();
								for(Filter f: account.getAllFilters()){
									String filterName = f.toString();
									for(int i = 0; i < filtersStrings.length; ++i){
										if(filterName.equals(filtersStrings[i])){
											if (accounts.getAccount(edit_account).getGroup()==Group.POWER) {
												if (accounts.getAccount(edit_account).removeActiveFilter(f.getId())!= null) {
													accounts.getAccount(edit_account).addFilter(f);
												} else if (accounts.getAccount(edit_account).removeInactiveFilter(f.getId())!=null){
													accounts.getAccount(edit_account).addInactiveFilter(f);
												} else {
													accounts.getAccount(edit_account).addFilter(f.makeCopyWithNewId());
												}
											} else {
												accounts.getAccount(edit_account).addDefaultFilter(f);
											}
										}
									}
								}
								if (edit_account.getGroup() != accounts.getAccount(edit_account).getGroup()) {
									accounts.getAccount(edit_account).removePermission(EnumSet.allOf(Permission.class));
									if (accounts.getAccount(edit_account).getGroup()==Group.POWER) {
										accounts.getAccount(edit_account).addPermission(accounts.getPowerPermissions());
										accounts.getAccount(edit_account).removeAllDefaultFilters();
										for (Filter f : accounts.getDefaultFilters(Group.POWER)) {
											accounts.getAccount(edit_account).addFilter(f.makeCopyWithNewId());
										}
									}
									else if (accounts.getAccount(edit_account).getGroup()==Group.STANDARD){
										accounts.getAccount(edit_account).addPermission(accounts.getStandardPermissions());
										for (Filter f : accounts.getDefaultFilters(Group.STANDARD)) {
											accounts.getAccount(edit_account).addDefaultFilter(f);
										}
										
									}
								}
								accounts.saveAccounts();


							}
							else if(opened_user_group){
								EnumSet<Permission> p = EnumSet.noneOf(Permission.class);
								if(btnCreateFilters.getSelection()){
									p.add(Permission.CREATEFILTER);
								}
								if(btnDeleteFilters.getSelection()){
									p.add(Permission.DELETEFILTER);
								}
								if(btnEditFilters.getSelection()){
									p.add(Permission.EDITFILTER);
								}
								if(btnSetProxyListening.getSelection()) {
									p.add(Permission.SETPORT);
								}
								switch(edit_group){
								case ADMINISTRATOR:
									accounts.setAdminPermissions(p);
									break;
								case POWER:
									accounts.setPowerPermission(p);
									break;
								case STANDARD:
									accounts.setStandardPermission(p);
								}
								
								// Reset and add default filters
								accounts.removeAllDefaultFilters(edit_group);
								List exportFilters = filterInactiveListViewer.getList();
								String[] filtersStrings = exportFilters.getItems();
								for(Filter f: account.getAllFilters()){
									String filterName = f.toString();
									for(int i = 0; i < filtersStrings.length; ++i){
										if(filterName.equals(filtersStrings[i])){
											accounts.addDefaultFilter(edit_group, f);
										}
									}
								}
								
								accounts.saveAccounts();
								
							}
							
							shell.close();
							shell.dispose();
							
						}
					}
				});
			
			//Cancel the changes
			Button btnCancel = new Button(btnBarComposite, SWT.NONE);
			GridData gd_btnCancel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			gd_btnCancel.widthHint = 75;
			btnCancel.setLayoutData(gd_btnCancel);
			formToolkit.adapt(btnCancel, true, true);
			btnCancel.setText("Cancel");
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			new Label(btnBarComposite, SWT.NONE);
			
			btnCancel.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){  
					switch(e.type) {
						case SWT.Selection:
							shell.close();
							shell.dispose();
						
					}
				}
			});
			//Add Filter from left to right list
			btnAdd.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event event) {
					switch(event.type){
					case SWT.Selection:
						    List al = filterActiveListViewer.getList();
							List il = filterInactiveListViewer.getList();
							try{
								String filter = il.getSelection()[0];
								String[] fil = filter.split(":");
								if(opened_user_account){
									accounts.getAccount(edit_account).removeDefaultFilter(Integer.parseInt(fil[0]));
								}
								
								il.remove(filter);
								if(!opened_user_account)
									al.add(filter);
							}catch(ArrayIndexOutOfBoundsException e){
							}
						
						
					}
				}
				
			});
			
			btnRemove.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event event) {
					switch(event.type){
					case SWT.Selection:

						try{
							List al = filterActiveListViewer.getList();
							List il = filterInactiveListViewer.getList();
							
							String filter = al.getSelection()[0];
							String[] fil = filter.split(":");
							
							al.remove(filter.toString());
							il.add(filter.toString());
						}catch(ArrayIndexOutOfBoundsException e){
						}
					
					}
				}
				
			});
				
	}
	 private void displayFiles(String[] files) {
		 for (int i = 0; files != null && i < files.length; i++) {
			 textHandle.setText(files[i]);
			 textHandle.setEditable(false);
		 }
	 }
}
