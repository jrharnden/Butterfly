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
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import javax.swing.JTextArea;
import org.eclipse.swt.widgets.List;
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ListViewer;

public class ApplicationWindow {

	protected Shell shlButterfly;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ApplicationWindow window = new ApplicationWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
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
		shlButterfly = new Shell();
		shlButterfly.setSize(800, 600);
		shlButterfly.setText("Butterfly");
		shlButterfly.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CTabFolder tabFolder = new CTabFolder(shlButterfly, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmStatus = new CTabItem(tabFolder, SWT.NONE);
		tbtmStatus.setText("Status");
		
		Composite statusComposite = new Composite(tabFolder, SWT.NONE);
		tbtmStatus.setControl(statusComposite);
		statusComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm statusSashForm = new SashForm(statusComposite, SWT.NONE);
		
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
		
		JTextArea logTextArea_AWT = new JTextArea();
		statusLogRootPane_AWT.getContentPane().add(logTextArea_AWT);
		
		Composite statusFilterComposite = new Composite(statusSashForm, SWT.NONE);
		statusFilterComposite.setLayout(new GridLayout(2, false));
		new Label(statusFilterComposite, SWT.NONE);
				new Label(statusFilterComposite, SWT.NONE);
						new Label(statusFilterComposite, SWT.NONE);
				
						
						Composite statusActiveComposite = new Composite(statusFilterComposite, SWT.NONE);
						statusActiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
						GridData gd_statusActiveComposite = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
						gd_statusActiveComposite.heightHint = 460;
						gd_statusActiveComposite.widthHint = 335;
						statusActiveComposite.setLayoutData(gd_statusActiveComposite);
						statusActiveComposite.setBounds(0, 0, 64, 64);
						formToolkit.adapt(statusActiveComposite);
						formToolkit.paintBordersFor(statusActiveComposite);
						
						ListViewer statusActiveListViewer = new ListViewer(statusActiveComposite, SWT.BORDER | SWT.V_SCROLL);
						List statusActiveList = statusActiveListViewer.getList();
		statusSashForm.setWeights(new int[] {1, 1});
		
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
		
		Composite filterActiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterActiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterActiveComposite.heightHint = 465;
		gd_filterActiveComposite.widthHint = 333;
		
		filterActiveComposite.setLayoutData(gd_filterActiveComposite);
		formToolkit.adapt(filterActiveComposite);
		formToolkit.paintBordersFor(filterActiveComposite);
		filterActiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ListViewer filterActiveListViewer = new ListViewer(filterActiveComposite, SWT.BORDER | SWT.V_SCROLL);
		List filterActiveList = filterActiveListViewer.getList();
		
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
		
		Button btnAdd = new Button(filterBtnComposite_CENTER, SWT.NONE);
		formToolkit.adapt(btnAdd, true, true);
		btnAdd.setText("ADD");
		
		Button btnRemove = new Button(filterBtnComposite_CENTER, SWT.NONE);
		formToolkit.adapt(btnRemove, true, true);
		btnRemove.setText("REMOVE");
		
		Composite filterBtnComposite_SOUTH = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_SOUTH);
		formToolkit.paintBordersFor(filterBtnComposite_SOUTH);
		
		Composite filterInactiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterInactiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterInactiveComposite.heightHint = 465;
		gd_filterInactiveComposite.widthHint = 333;
		filterInactiveComposite.setLayoutData(gd_filterInactiveComposite);
		formToolkit.adapt(filterInactiveComposite);
		formToolkit.paintBordersFor(filterInactiveComposite);
		filterInactiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ListViewer filterInactiveListViewer = new ListViewer(filterInactiveComposite, SWT.BORDER | SWT.V_SCROLL);
		List filterInactiveList = filterInactiveListViewer.getList();
					
		Composite filterBtnBarComposite = new Composite(filterComposite, SWT.NONE);
		filterBtnBarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_filterBtnBarComposite = new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 1, 1);
		gd_filterBtnBarComposite.widthHint = 771;
		gd_filterBtnBarComposite.heightHint = 28;
		filterBtnBarComposite.setLayoutData(gd_filterBtnBarComposite);
		formToolkit.adapt(filterBtnBarComposite);
		formToolkit.paintBordersFor(filterBtnBarComposite);
		
		Button btnCreate = new Button(filterBtnBarComposite, SWT.NONE);
		formToolkit.adapt(btnCreate, true, true);
		btnCreate.setText("Create");
		
		Button btnEdit = new Button(filterBtnBarComposite, SWT.NONE);
		formToolkit.adapt(btnEdit, true, true);
		btnEdit.setText("Edit");
		
		Button btnDelete = new Button(filterBtnBarComposite, SWT.NONE);
		formToolkit.adapt(btnDelete, true, true);
		btnDelete.setText("Delete");
		
		CTabItem tbtmAdministrator = new CTabItem(tabFolder, SWT.NONE);
		tbtmAdministrator.setText("Administrator");
		
		Composite admComposite = new Composite(tabFolder, SWT.NONE);
		tbtmAdministrator.setControl(admComposite);
		formToolkit.paintBordersFor(admComposite);
		admComposite.setLayout(new GridLayout(1, false));
		
		Composite admTableTreeComposite = new Composite(admComposite, SWT.NONE);
		GridData gd_admTableTreeComposite = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_admTableTreeComposite.widthHint = 767;
		gd_admTableTreeComposite.heightHint = 477;
		admTableTreeComposite.setLayoutData(gd_admTableTreeComposite);
		formToolkit.adapt(admTableTreeComposite);
		formToolkit.paintBordersFor(admTableTreeComposite);
		admTableTreeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TreeViewer admTableTreeViewer = new TreeViewer(admTableTreeComposite, SWT.BORDER);
		Tree admTableTree = admTableTreeViewer.getTree();
		formToolkit.paintBordersFor(admTableTree);
		
		Composite admBtnBarComposite = formToolkit.createComposite(admComposite, SWT.NONE);
		GridData gd_admBtnBarComposite = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_admBtnBarComposite.widthHint = 769;
		gd_admBtnBarComposite.heightHint = 28;
		admBtnBarComposite.setLayoutData(gd_admBtnBarComposite);
		formToolkit.paintBordersFor(admBtnBarComposite);
		admBtnBarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button admBtnCreate = new Button(admBtnBarComposite, SWT.NONE);
		formToolkit.adapt(admBtnCreate, true, true);
		admBtnCreate.setText("Create");
		
		Button admBtnEdit = new Button(admBtnBarComposite, SWT.NONE);
		formToolkit.adapt(admBtnEdit, true, true);
		admBtnEdit.setText("Edit");
		
		Button admBtnDelete = new Button(admBtnBarComposite, SWT.NONE);
		formToolkit.adapt(admBtnDelete, true, true);
		admBtnDelete.setText("Delete");
		
		Menu menu = new Menu(shlButterfly, SWT.BAR);
		shlButterfly.setMenuBar(menu);
		
		MenuItem mntmMain = new MenuItem(menu, SWT.CASCADE);
		mntmMain.setText("Main");
		
		Menu menu_main = new Menu(mntmMain);
		mntmMain.setMenu(menu_main);
		
		MenuItem mntmImport = new MenuItem(menu_main, SWT.NONE);
		mntmImport.setText("Import");
		
		MenuItem mntmExport = new MenuItem(menu_main, SWT.NONE);
		mntmExport.setText("Export");
		
		MenuItem mntmNewItem = new MenuItem(menu_main, SWT.NONE);
		mntmNewItem.setText("Logout");
		
		MenuItem mntmSettings = new MenuItem(menu, SWT.CASCADE);
		mntmSettings.setText("Settings");
		
		Menu menu_settings = new Menu(mntmSettings);
		mntmSettings.setMenu(menu_settings);
		
		MenuItem mntmChangePassword = new MenuItem(menu_settings, SWT.NONE);
		mntmChangePassword.setText("Change Password");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");
		
		Menu menu_1 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_1);
		
		MenuItem mntmAbout = new MenuItem(menu_1, SWT.NONE);
		mntmAbout.setText("About");
		
		MenuItem mntmBrowserSetup = new MenuItem(menu_1, SWT.NONE);
		mntmBrowserSetup.setText("Browser Setup");
		
		MenuItem mntmFilterExample = new MenuItem(menu_1, SWT.NONE);
		mntmFilterExample.setText("Filter Example");

	}
}
