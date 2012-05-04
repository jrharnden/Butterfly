package gui.view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.SashForm;
import java.awt.Frame;

import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.awt.SWT_AWT;

import java.awt.Color;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.eclipse.swt.widgets.List;
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import storage.*;
public class ApplicationWindow{

	//final static variables
	final static String EXPORT = "Export";
	final static String IMPORT = "Import";
	
	final static String CREATE = "Create";
	final static String EDIT = "Edit";
	
	//Constructor variables
	protected Shell shlButterfly;
	protected Display display;
	private Account account;
	private Accounts accounts;
	private JFrame frame;
	protected JTextField txtPort;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private  ListViewer filterInactiveListViewer;
	private ListViewer  filterActiveListViewer;
	
	/**
	 * Launches Login window
	 * @param shell
	 * @return boolean
	 */
	private boolean authenticate(Shell shell) {
		LoginShell login = new LoginShell(shell);
		login.open(this);
		return true;
	}
	
	/**
	 * Create the filter editing window. 
	 * @param s type of window (create/edit)
	 * @return boolean true upon close
	 */
	private boolean filterEdit(String s, Filter editFilter) {
		Display display = Display.getDefault();
		FilterShell filterEdit = new FilterShell(display, s, account, accounts);
		filterEdit.setFilter(editFilter);
		filterEdit.open();
		return true;
	}
	
	/**
	 * Call the edit shell with account edit permissions
	 * @param a
	 * @return
	 */
	private boolean editUserAccount(Account a){
		Display display = Display.getDefault();
		EditShell eShell = new EditShell(display, a);
		
		//Disable the main window
		shlButterfly.setEnabled(false);
		
		// open new window
		eShell.open();
		
		//Re-Enable and make the window active
		shlButterfly.setEnabled(true);
		shlButterfly.setActive();
		return true;
	}
	
	/**
	 * Call the edit shell with edit user group permissions
	 * @param g
	 * @return
	 */
	private boolean editUserGroup(Group g){
		Display display = Display.getDefault();
		EditShell eShell = new EditShell(display, g);
		
		//Disable the main window
		shlButterfly.setEnabled(false);
		
		// open new window
		eShell.open();
		
		//Re-Enable and make the window active
		shlButterfly.setEnabled(true);
		shlButterfly.setActive();
		return true;
	}
	
	/**
	 * Open filter import/export
	 * @return
	 */
	private boolean impExpShell(Account a, String s){
		Display display = Display.getDefault();
		EditShell eShell = new EditShell(display, a, s);
		
		//Disable the main window
		shlButterfly.setEnabled(false);
		// open new window
		eShell.open();
		//Re-Enable and make the window active
		shlButterfly.setEnabled(true);
		shlButterfly.setActive();
		return true;
	}
	
	/**
	 * Open the Account shell using the change password constructs. 
	 * @param shell 
	 * @param accName 
	 * @param group
	 * @return
	 */
	private boolean changePassword(Shell shell, String accName, Group group) {
		AccountShell aShell = new AccountShell(shell, accName, group);
	
		//Disable the main window
		shlButterfly.setEnabled(false);
		
		aShell.open(this);
		
		//Re-Enable and make the window active
		shlButterfly.setEnabled(true);
		shlButterfly.setActive();
		return true;
	}

	/**
	 * Launches Create Account window
	 * @param shell
	 * @return
	 */
	private boolean accountShell(Shell shell){
		AccountShell aShell = new AccountShell(shell);
		aShell.open(this);
		return true;
		
	}
	/**
	 * 
	 * @param a
	 */
	public void setAccount(Account a){
		account = a;
	}
	

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		final Shell shell = new Shell();
		authenticate(shell);
		createContents();
		shlButterfly.open();
		shlButterfly.layout();
		while (!shlButterfly.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlButterfly = new Shell(SWT.ON_TOP | SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlButterfly.setSize(800, 600);
		shlButterfly.setText("Butterfly - Logged in as "+ account.getName());
		shlButterfly.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CTabFolder tabFolder = new CTabFolder(shlButterfly, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		
		//-----------------------------------------------------------------
		// Status Menu Item
		//-----------------------------------------------------------------
		CTabItem tbtmStatus = new CTabItem(tabFolder, SWT.NONE);
		tbtmStatus.setText("Status");
		
		Composite statusComposite = new Composite(tabFolder, SWT.NONE);
		tbtmStatus.setControl(statusComposite);
		statusComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm statusSashForm = new SashForm(statusComposite, SWT.NONE);
		
		//Logging composite
		Composite statusLogComposite_AWT = new Composite(statusSashForm, SWT.NONE);
		statusLogComposite_AWT.setLayout(new GridLayout(2, false));
		new Label(statusLogComposite_AWT, SWT.NONE);
		new Label(statusLogComposite_AWT, SWT.NONE);
		new Label(statusLogComposite_AWT, SWT.NONE);
		
		Composite statusLogComposite_AWT_1 = new Composite(statusLogComposite_AWT, SWT.EMBEDDED);
		GridData gd_statusLogComposite_AWT_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_statusLogComposite_AWT_1.heightHint = 460;
		gd_statusLogComposite_AWT_1.widthHint = 335;
		statusLogComposite_AWT_1.setLayoutData(gd_statusLogComposite_AWT_1);
		
		Frame statusLogFrame_AWT = SWT_AWT.new_Frame(statusLogComposite_AWT_1);
		
		Panel satusLogPanel_AWT = new Panel();
		statusLogFrame_AWT.add(satusLogPanel_AWT);
		satusLogPanel_AWT.setLayout(new BorderLayout(0, 0));
		
		JRootPane statusLogRootPane_AWT = new JRootPane();
		satusLogPanel_AWT.add(statusLogRootPane_AWT);
		statusLogRootPane_AWT.getContentPane().setLayout(new java.awt.GridLayout(1, 0, 0, 0));
			
			//****************************************
			//Logging text area
			//****************************************
			//Create Black border
			Border border;
			border = BorderFactory.createLineBorder(Color.black);	
		
			JTextArea logTextArea_AWT = new JTextArea();
			statusLogRootPane_AWT.getContentPane().add(logTextArea_AWT);
			logTextArea_AWT.setBorder(border);
			
		Composite statusFilterComposite = new Composite(statusSashForm, SWT.NONE);
		statusFilterComposite.setLayout(new GridLayout(2, false));
		new Label(statusFilterComposite, SWT.NONE);
		new Label(statusFilterComposite, SWT.NONE);
		new Label(statusFilterComposite, SWT.NONE);

		//Active List Composite
		Composite statusActiveComposite = new Composite(statusFilterComposite, SWT.NONE);
		statusActiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_statusActiveComposite = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_statusActiveComposite.heightHint = 460;
		gd_statusActiveComposite.widthHint = 335;
		statusActiveComposite.setLayoutData(gd_statusActiveComposite);
		statusActiveComposite.setBounds(0, 0, 64, 64);
		formToolkit.adapt(statusActiveComposite);
		formToolkit.paintBordersFor(statusActiveComposite);
		
			//Active List Viewer
			ListViewer statusActiveListViewer = new ListViewer(statusActiveComposite, SWT.BORDER | SWT.V_SCROLL);
			List statusActiveList = statusActiveListViewer.getList();
			statusSashForm.setWeights(new int[] {1, 1});
		
		
		//-----------------------------------------------------------------
		// Filters menu item
		//-----------------------------------------------------------------
		CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Filters");
		
		Composite filterComposite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(filterComposite);
		formToolkit.paintBordersFor(filterComposite);
		filterComposite.setLayout(new GridLayout(1, false));
		
		Composite filterComposite_1 = new Composite(filterComposite, SWT.NONE);
		GridData gd_filterComposite_1 = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterComposite_1.widthHint = 768;
		gd_filterComposite_1.heightHint = 474;
		filterComposite_1.setLayoutData(gd_filterComposite_1);
		formToolkit.adapt(filterComposite_1);
		formToolkit.paintBordersFor(filterComposite_1);
		filterComposite_1.setLayout(new GridLayout(3, false));
		
		Label lblActiveFilters = new Label(filterComposite_1, SWT.NONE);
		lblActiveFilters.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblActiveFilters, true, true);
		lblActiveFilters.setText("Active Filters");
		new Label(filterComposite_1, SWT.NONE);
		
		Label lblInactiveFilters = new Label(filterComposite_1, SWT.NONE);
		lblInactiveFilters.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblInactiveFilters, true, true);
		lblInactiveFilters.setText("Inactive Filters");
		
		//Active Filter composite
		Composite filterActiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterActiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterActiveComposite.heightHint = 465;
		gd_filterActiveComposite.widthHint = 333;
		
		filterActiveComposite.setLayoutData(gd_filterActiveComposite);
		formToolkit.adapt(filterActiveComposite);
		formToolkit.paintBordersFor(filterActiveComposite);
		filterActiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
			
			
				// Active filter list viewer
				filterActiveListViewer = new ListViewer(filterActiveComposite, SWT.BORDER | SWT.V_SCROLL);
				List filterActiveList = filterActiveListViewer.getList();
			ArrayList<Filter> f = account.getActiveFilters();
			for(Filter fil: f){
				filterActiveList.add(fil.toString());
			}
			
		//Filter middle button bar
		Composite filterBtnComposite = new Composite(filterComposite_1, SWT.NONE);
		filterBtnComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData gd_filterBtnComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterBtnComposite.heightHint = 465;
		gd_filterBtnComposite.widthHint = 85;
		filterBtnComposite.setLayoutData(gd_filterBtnComposite);
		formToolkit.adapt(filterBtnComposite);
		formToolkit.paintBordersFor(filterBtnComposite);
		
		Composite filterBtnComposite_NORTH = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_NORTH);
		formToolkit.paintBordersFor(filterBtnComposite_NORTH);
		
		Composite filterBtnComposite_CENTER = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_CENTER);
		formToolkit.paintBordersFor(filterBtnComposite_CENTER);
		filterBtnComposite_CENTER.setLayout(new FillLayout(SWT.HORIZONTAL));
		
			//Add from inactive to active
			Button btnAdd = new Button(filterBtnComposite_CENTER, SWT.NONE);
			formToolkit.adapt(btnAdd, true, true);
			btnAdd.setText("<");

			//Remove from active to inactive
			Button btnRemove = new Button(filterBtnComposite_CENTER, SWT.NONE);
			formToolkit.adapt(btnRemove, true, true);
			btnRemove.setText(">");
		
		
		Composite filterBtnComposite_SOUTH = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_SOUTH);
		formToolkit.paintBordersFor(filterBtnComposite_SOUTH);
		
		//Inactive filter composite
		Composite filterInactiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterInactiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterInactiveComposite.heightHint = 465;
		gd_filterInactiveComposite.widthHint = 333;
		filterInactiveComposite.setLayoutData(gd_filterInactiveComposite);
		formToolkit.adapt(filterInactiveComposite);
		formToolkit.paintBordersFor(filterInactiveComposite);
		filterInactiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
			
				//Inactive List viewer
				filterInactiveListViewer = new ListViewer(filterInactiveComposite, SWT.BORDER | SWT.V_SCROLL);
				List filterInactiveList = filterInactiveListViewer.getList();
			ArrayList<Filter> fml = account.getInactiveFilters();
			for(Filter fil: fml){
				filterInactiveList.add(fil.toString());
			}
		//Filter Button Bar
		Composite filterBtnBarComposite = new Composite(filterComposite, SWT.NONE);
		filterBtnBarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_filterBtnBarComposite = new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 1, 1);
		gd_filterBtnBarComposite.widthHint = 771;
		gd_filterBtnBarComposite.heightHint = 28;
		filterBtnBarComposite.setLayoutData(gd_filterBtnBarComposite);
		formToolkit.adapt(filterBtnBarComposite);
		formToolkit.paintBordersFor(filterBtnBarComposite);
			//Create filter
			Button btnCreate = new Button(filterBtnBarComposite, SWT.NONE);
			formToolkit.adapt(btnCreate, true, true);
			btnCreate.setText(CREATE);
			//Create filter button listener. Open blank text area.
			btnCreate.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						btnCreateHandleEvent();
					}
				}
			}
			);
			if(!account.getPermissions().contains(Permission.CREATEFILTER)){
				btnCreate.setEnabled(false);
			}
			//Add Filter from inactive to active list
			btnAdd.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event event) {
					switch(event.type){
					case SWT.Selection:
						btnAddHandleEvent();
					}
				}
				
			});
			
			btnRemove.addListener(SWT.Selection, new Listener(){

				@Override
				public void handleEvent(Event event) {
					switch(event.type){
					case SWT.Selection:
						btnRemoveHandleEvent();
					}
				}
				
			});
			
		
			
			//Edit filter
			Button btnEdit = new Button(filterBtnBarComposite, SWT.NONE);
			formToolkit.adapt(btnEdit, true, true);
			btnEdit.setText(EDIT);
			
			//Create filter button listener. Open text area with highlighted filters text.
			btnEdit.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						btnEditHandleEvent();
					}
				}
			}
			);
			if(!account.getPermissions().contains(Permission.EDITFILTER)){
				btnEdit.setEnabled(false);
			}
				//Delete filter
				Button btnDelete = new Button(filterBtnBarComposite, SWT.NONE);
				formToolkit.adapt(btnDelete, true, true);
				btnDelete.setText("Delete");
			
				btnDelete.addListener(SWT.Selection, new Listener(){
					public void handleEvent(Event e){
						switch(e.type){
						case SWT.Selection:
							btnDeleteHandleEvent();
						
						}
					}
				});
				if(!account.getPermissions().contains(Permission.DELETEFILTER)){
					btnDelete.setEnabled(false);
				}
		
		//-----------------------------------------------------------------
		//Administrator Tab
		//-----------------------------------------------------------------
		//if(account.getGroup()==Group.ADMINISTRATOR){
		CTabItem tbtmAdministrator = new CTabItem(tabFolder, SWT.NONE);
		tbtmAdministrator.setText("Administrator");
		
		Composite admComposite = new Composite(tabFolder, SWT.NONE);
		tbtmAdministrator.setControl(admComposite);
		formToolkit.paintBordersFor(admComposite);
		admComposite.setLayout(new GridLayout(3, false));
			
			//Accounts label
			Label lblAccounts = new Label(admComposite, SWT.NONE);
			lblAccounts.setAlignment(SWT.CENTER);
			GridData gd_lblAccounts = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
			gd_lblAccounts.widthHint = 256;
			lblAccounts.setLayoutData(gd_lblAccounts);
			formToolkit.adapt(lblAccounts, true, true);
			lblAccounts.setText("Accounts");
			
			//Active Filter label
			Label lblNewLabel = new Label(admComposite, SWT.NONE);
			lblNewLabel.setAlignment(SWT.CENTER);
			GridData gd_lblNewLabel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
			gd_lblNewLabel.widthHint = 255;
			lblNewLabel.setLayoutData(gd_lblNewLabel);
			formToolkit.adapt(lblNewLabel, true, true);
			lblNewLabel.setText("Active Filters");
			
			//Inactive filter label
			Label lblNewLabel_1 = new Label(admComposite, SWT.NONE);
			lblNewLabel_1.setAlignment(SWT.CENTER);
			lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			formToolkit.adapt(lblNewLabel_1, true, true);
			lblNewLabel_1.setText("Inactive Filters");
		
		//TODO: All of this code is basically a hack until I can get TreeViewer to work
			Composite admTableTreeComposite = new Composite(admComposite, SWT.NONE);
			GridData gd_admTableTreeComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 3, 1);
			gd_admTableTreeComposite.heightHint = 484;
			gd_admTableTreeComposite.widthHint = 778;
			
			admTableTreeComposite.setLayoutData(gd_admTableTreeComposite);
			formToolkit.adapt(admTableTreeComposite);
			formToolkit.paintBordersFor(admTableTreeComposite);
			admTableTreeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
			
			// Acccount viewer
			ListViewer AccountListViewer = new ListViewer(admTableTreeComposite, SWT.BORDER | SWT.V_SCROLL);
			final List AccountList = AccountListViewer.getList();
			
			//TODO: terrible way of doing this
			AccountList.add("Administrator");
			AccountList.add("Power Users");
			AccountList.add("Standard Users");
			
			//TODO set active list to selections current active filters
			ListViewer activeViewer = new ListViewer(admTableTreeComposite, SWT.BORDER | SWT.V_SCROLL);
			List activeList = activeViewer.getList();
			
			//TODO set inactive list to selections current inactive filters
			ListViewer inactiveViewer = new ListViewer(admTableTreeComposite, SWT.BORDER | SWT.V_SCROLL);
			List inactiveList = inactiveViewer.getList();
							
									// Administrator button bar
									Composite admBtnBarComposite = formToolkit.createComposite(admComposite, SWT.NONE);
									GridData gd_admBtnBarComposite = new GridData(SWT.LEFT, SWT.TOP, true, false, 3, 1);
									gd_admBtnBarComposite.widthHint = 779;
									gd_admBtnBarComposite.heightHint = 28;
									admBtnBarComposite.setLayoutData(gd_admBtnBarComposite);
									formToolkit.paintBordersFor(admBtnBarComposite);
									admBtnBarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
									
										//Create an Account
										Button admBtnCreate = new Button(admBtnBarComposite, SWT.NONE);
										formToolkit.adapt(admBtnCreate, true, true);
										admBtnCreate.setText(CREATE);
										
										admBtnCreate.addListener(SWT.Selection, new Listener(){
											public void handleEvent(Event e){
												switch (e.type){
												case SWT.Selection:
													Shell accShell = new Shell(display);
													accountShell(accShell);
												}
											}
										}
										);
										
										//TODO implement user group edit/accounts
										//Edit User Groups/Accounts
										Button admBtnEdit = new Button(admBtnBarComposite, SWT.NONE);
										formToolkit.adapt(admBtnEdit, true, true);
										admBtnEdit.setText(EDIT);
										
											admBtnEdit.addListener(SWT.Selection, new Listener(){
												public void handleEvent(Event e){
													switch (e.type){
													case SWT.Selection:
														Accounts acc = new Accounts();
														acc.loadAccounts();
														Account a =acc.getAccount(AccountList.getSelection()[0].trim());
														if(a == null) System.err.println(AccountList.getSelection()[0]);
															//editShell(a,"Administrator");
															
															//TODO If an account is selected
															//editUserAccount(a);
															
															//TODO If a user group is selected
															//editUserGroup(group);
													}
												}
											}
											);
											//TODO:: Shouldn't be able to delete your own account
											//Delete User Groups/Accounts
											final Button admBtnDelete = new Button(admBtnBarComposite, SWT.NONE);
											formToolkit.adapt(admBtnDelete, true, true);
											admBtnDelete.setText("Delete");
											admBtnDelete.addListener(SWT.Selection, new Listener(){
												public void handleEvent(Event e){
													switch(e.type){
													case SWT.Selection:
														Accounts acc = new Accounts();
														acc.loadAccounts();
														Account a =acc.getAccount(AccountList.getSelection()[0].trim());
														if(a!=null){
															acc.removeAccount(a);
															acc.saveAccounts();
														}
														else
															System.err.println("No account selected");
													}
												}
											});
											AccountListViewer.addSelectionChangedListener(new ISelectionChangedListener(){


												@Override
												public void selectionChanged(SelectionChangedEvent e) {
													String selection = AccountList.getSelection()[0].trim();
													if(selection.equals("Administrator")||selection.equals("Power Users")||selection.equals("Standard Users")){
														
														admBtnDelete.setEnabled(false);
													}
													else
														admBtnDelete.setEnabled(true);
												}
												
											});
		
	
				Accounts a = new Accounts();
				a.loadAccounts(); 
				for(Account acc: a){
					if(acc.getGroup()==Group.ADMINISTRATOR)
						AccountList.add("\t"+acc.getName());
					
				}
				for(Account acc: a){
					if(acc.getGroup()==Group.POWER)
						AccountList.add("\t"+acc.getName());
				}
				for(Account acc: a){
					if(acc.getGroup()==Group.STANDARD)
							AccountList.add("\t"+acc.getName());
						
				}
		
	//	}
		
		//-----------------------------------------------------------------
		//Main menu bar
		//-----------------------------------------------------------------
		Menu menu = new Menu(shlButterfly, SWT.BAR);
		shlButterfly.setMenuBar(menu);
		
		// Menu Bar Main
		MenuItem mntmMain = new MenuItem(menu, SWT.CASCADE);
		mntmMain.setText("Main");
		
		Menu menu_main = new Menu(mntmMain);
		mntmMain.setMenu(menu_main);
		
		//Listen if the user is not a standard user
		if (account.getGroup() != Group.STANDARD) {
			final MenuItem mntmListen = new MenuItem(menu_main, SWT.CHECK);
			//Set Listen to default on
			mntmListen.setSelection(true);
			mntmListen.setText("Listen");
			mntmListen.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					if (mntmListen.getSelection()) {
						//TODO turn on the proxy
						System.out.println("Checked");
					} else {
						//TODO turn off the proxy
						System.out.println("Uncheck");
					}
				}
			});
		}
			//Import
			MenuItem mntmImport = new MenuItem(menu_main, SWT.NONE);
			mntmImport.setText(IMPORT);
			mntmImport.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						impExpShell(account, IMPORT);
					}
				}
			}
			);
			
			//Export
			MenuItem mntmExport = new MenuItem(menu_main, SWT.NONE);
			mntmExport.setText(EXPORT);
			
			mntmExport.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						impExpShell(account, EXPORT);
					}
				}
			}
			);
			
			//Logout
			MenuItem mntmNewItem = new MenuItem(menu_main, SWT.NONE);
			mntmNewItem.setText("Logout");
			
			mntmNewItem.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						
					}
				}
			}
			);
			//Quit
			MenuItem mntmQuit = new MenuItem(menu_main, SWT.NONE);
			mntmQuit.setText("Quit");
			mntmQuit.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						System.exit(0);
					}
				}
			});
			
		// Menu Bar Settings	
		MenuItem mntmSettings = new MenuItem(menu, SWT.CASCADE);
		mntmSettings.setText("Settings");
		
		Menu menu_settings = new Menu(mntmSettings);
		mntmSettings.setMenu(menu_settings);
		
				//Change Password
				MenuItem mntmChangePassword = new MenuItem(menu_settings, SWT.NONE);
				mntmChangePassword.setText("Change Password");
				
				mntmChangePassword.addListener(SWT.Selection, new Listener(){
					public void handleEvent(Event e) {
						Shell aShell = new Shell(display);
						changePassword(aShell, account.getName(), account.getGroup());
						
					}
				});
				
				
				//Logging - Check enabled
				final MenuItem mntmEnableLogging = new MenuItem(menu_settings, SWT.CHECK);
				mntmEnableLogging.setSelection(true);
				mntmEnableLogging.setText("Enable Logging");
				
				mntmEnableLogging.addListener(SWT.Selection, new Listener(){
					public void handleEvent(Event e) {
						if (mntmEnableLogging.getSelection() == true) {
							System.out.println("Checked");
						} else {
							System.out.println("Unchecked");
						}
					}
				});
				
//				//Set port
//				MenuItem mntmSetPort = new MenuItem(menu_settings, SWT.NONE);
//				mntmSetPort.setText("Set Port");
//				if(!account.getPermissions().contains(Permission.SETPORT)){
//					menu_settings.getItem(menu_settings.indexOf(mntmSetPort)).setEnabled(false);
//				}
							

	}
	public void setAccounts(Accounts a){
		accounts = a;
	}
	private void btnDeleteHandleEvent(){
		try{
			List activeFilters = filterActiveListViewer.getList();
			List inactiveFilters = filterInactiveListViewer.getList();
			String filter;
			if(inactiveFilters.getSelection().length!=0)
				filter = inactiveFilters.getSelection()[0];
			else
				filter = activeFilters.getSelection()[0];
			String[] fil = filter.split(":");
			String filterName = fil[0];
			account.removeFilter(filterName);
			accounts.saveAccounts();
			inactiveFilters.removeAll();
			activeFilters.removeAll();
			ArrayList<Filter> fml = account.getInactiveFilters();
			for(Filter fia: fml){
				inactiveFilters.add(fia.toString());
			}
			fml = account.getActiveFilters();
			for(Filter fia: fml){
				activeFilters.add(fia.toString());
			}
			
		}catch(ArrayIndexOutOfBoundsException exc){
			
			System.err.println("Didn't select anything");
		}
	}
	private void btnAddHandleEvent(){
		List al = filterActiveListViewer.getList();
		List il = filterInactiveListViewer.getList();
		try{
			String filter = il.getSelection()[0];
			String[] fil = filter.split(":");
			String filterName = fil[0];
			Filter removedFilter = account.removeInactiveFilter(filterName);
			account.addFilter(removedFilter);
			accounts.saveAccounts();
			il.remove(filter);
			al.add(filter);
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Didn't select anything");
		}
	}
	private void btnRemoveHandleEvent(){
		try{
			List al = filterActiveListViewer.getList();
			List il = filterInactiveListViewer.getList();
			
			String filter = al.getSelection()[0];
			String[] fil = filter.split(":");
			String filterName = fil[0];
			Filter removedFilter = account.removeActiveFilter(filterName);
			account.addInactiveFilter(removedFilter);
			accounts.saveAccounts();
			al.remove(filter);
			il.add(filter);
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Didn't select anything");
		}
	}
	private void btnCreateHandleEvent(){
		filterEdit(CREATE,null);
		//Repopulate filter lists
		List filterInactiveList = filterInactiveListViewer.getList();
		filterInactiveList.removeAll();
		ArrayList<Filter> fml = account.getInactiveFilters();
		for(Filter fia: fml){
			filterInactiveList.add(fia.toString());
		}
		List filteractiveList = filterActiveListViewer.getList();
		filteractiveList.removeAll();
		ArrayList<Filter> fma = account.getActiveFilters();
		for(Filter fia: fma){
			filteractiveList.add(fia.toString());
		}
	}
	private void btnEditHandleEvent(){
		List activeFilters = filterActiveListViewer.getList();
		List inactiveFilters = filterInactiveListViewer.getList();
		try{
			String filter;
			if(inactiveFilters.getSelection().length!=0)
				filter = inactiveFilters.getSelection()[0];
			else
				filter = activeFilters.getSelection()[0];
			String[] fil = filter.split(":");
			String filterName = fil[0];
			Filter editFilter = account.getFilter(filterName);
			filterEdit(EDIT,editFilter);
			//Repopulate filter lists
			List filterInactiveList = filterInactiveListViewer.getList();
			filterInactiveList.removeAll();
			ArrayList<Filter> fml = account.getInactiveFilters();
			for(Filter fia: fml){
				filterInactiveList.add(fia.toString());
			}
			List filteractiveList = filterActiveListViewer.getList();
			filteractiveList.removeAll();
			ArrayList<Filter> fma = account.getActiveFilters();
			for(Filter fia: fma){
				filteractiveList.add(fia.toString());
			}
		}catch(ArrayIndexOutOfBoundsException exc){
			System.err.println("Didn't select anything");
		}
	}
}
