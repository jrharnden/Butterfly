package gui.view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.SashForm;
import swing2swt.layout.BoxLayout;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import java.awt.CardLayout;
import javax.swing.JTextArea;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CLabel;

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

		
		Composite composite_1 = new Composite(statusFilterComposite, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 460;
		gd_composite_1.widthHint = 335;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setBounds(0, 0, 64, 64);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		statusSashForm.setWeights(new int[] {1, 1});
		
		CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Filters");
		
		Composite filterComposite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(filterComposite);
		formToolkit.paintBordersFor(filterComposite);
		filterComposite.setLayout(new swing2swt.layout.BorderLayout(0, 0));
		
		Composite filterButtonComposite_SOUTH = new Composite(filterComposite, SWT.NONE);
		filterButtonComposite_SOUTH.setLayoutData(swing2swt.layout.BorderLayout.SOUTH);
		formToolkit.adapt(filterButtonComposite_SOUTH);
		formToolkit.paintBordersFor(filterButtonComposite_SOUTH);
		filterButtonComposite_SOUTH.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Button btnCreateFilter = new Button(filterButtonComposite_SOUTH, SWT.NONE);
		formToolkit.adapt(btnCreateFilter, true, true);
		btnCreateFilter.setText("Create");
		
		Button btnEditFilter = new Button(filterButtonComposite_SOUTH, SWT.NONE);
		formToolkit.adapt(btnEditFilter, true, true);
		btnEditFilter.setText("Edit");
		
		Button btnDeleteFilter = new Button(filterButtonComposite_SOUTH, SWT.NONE);
		formToolkit.adapt(btnDeleteFilter, true, true);
		btnDeleteFilter.setText("Delete");
		
		Button btnImport = new Button(filterButtonComposite_SOUTH, SWT.NONE);
		formToolkit.adapt(btnImport, true, true);
		btnImport.setText("Import");
		
		Button btnExport = new Button(filterButtonComposite_SOUTH, SWT.NONE);
		formToolkit.adapt(btnExport, true, true);
		btnExport.setText("Export");
		
		Composite filterActiveComposite_WEST = new Composite(filterComposite, SWT.NONE);
		filterActiveComposite_WEST.setLayoutData(swing2swt.layout.BorderLayout.WEST);
		formToolkit.adapt(filterActiveComposite_WEST);
		formToolkit.paintBordersFor(filterActiveComposite_WEST);
		filterActiveComposite_WEST.setLayout(new GridLayout(1, false));
		
		List filterActiveList = new List(filterActiveComposite_WEST, SWT.BORDER);
		GridData gd_filterActiveList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_filterActiveList.widthHint = 310;
		gd_filterActiveList.heightHint = 460;
		filterActiveList.setLayoutData(gd_filterActiveList);
		formToolkit.adapt(filterActiveList, true, true);
		
		Label lblActiveFilters = new Label(filterActiveComposite_WEST, SWT.NONE);
		lblActiveFilters.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblActiveFilters, true, true);
		lblActiveFilters.setText("Active Filters");
		
		Composite filterInactiveComposite_EAST = new Composite(filterComposite, SWT.NONE);
		filterInactiveComposite_EAST.setLayoutData(swing2swt.layout.BorderLayout.EAST);
		formToolkit.adapt(filterInactiveComposite_EAST);
		formToolkit.paintBordersFor(filterInactiveComposite_EAST);
		filterInactiveComposite_EAST.setLayout(new GridLayout(1, false));
		
		List filterInactiveList = new List(filterInactiveComposite_EAST, SWT.BORDER);
		GridData gd_filterInactiveList = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_filterInactiveList.widthHint = 310;
		gd_filterInactiveList.heightHint = 460;
		filterInactiveList.setLayoutData(gd_filterInactiveList);
		formToolkit.adapt(filterInactiveList, true, true);
		
		Label lblNewLabel = new Label(filterInactiveComposite_EAST, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("Inactive Filters");
		
		Composite filterAddRemoveComposite_CENTER = new Composite(filterComposite, SWT.NONE);
		filterAddRemoveComposite_CENTER.setLayoutData(swing2swt.layout.BorderLayout.CENTER);
		formToolkit.adapt(filterAddRemoveComposite_CENTER);
		formToolkit.paintBordersFor(filterAddRemoveComposite_CENTER);
		filterAddRemoveComposite_CENTER.setLayout(new GridLayout(2, false));
		
		
		Button btnAddToActive = new Button(filterAddRemoveComposite_CENTER, SWT.CENTER);
		GridData gd_btnAddToActive = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnAddToActive.widthHint = 59;
		btnAddToActive.setLayoutData(gd_btnAddToActive);
		formToolkit.adapt(btnAddToActive, true, true);
		btnAddToActive.setText("Add");
		new Label(filterAddRemoveComposite_CENTER, SWT.NONE);
		new Label(filterAddRemoveComposite_CENTER, SWT.NONE);
		new Label(filterAddRemoveComposite_CENTER, SWT.NONE);
		new Label(filterAddRemoveComposite_CENTER, SWT.NONE);
		new Label(filterAddRemoveComposite_CENTER, SWT.NONE);
		
		Button btnRemoveFromActive = new Button(filterAddRemoveComposite_CENTER, SWT.NONE);
		GridData gd_btnRemoveFromActive = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRemoveFromActive.widthHint = 62;
		btnRemoveFromActive.setLayoutData(gd_btnRemoveFromActive);
		formToolkit.adapt(btnRemoveFromActive, true, true);
		btnRemoveFromActive.setText("Remove");
		
		CTabItem tbtmSettings = new CTabItem(tabFolder, SWT.NONE);
		tbtmSettings.setText("Settings");
		
		Composite settingsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmSettings.setControl(settingsComposite);
		formToolkit.paintBordersFor(settingsComposite);
		
		CTabItem tbtmAdministrator = new CTabItem(tabFolder, SWT.NONE);
		tbtmAdministrator.setText("Administrator");
		
		Composite administratorComposite = new Composite(tabFolder, SWT.NONE);
		tbtmAdministrator.setControl(administratorComposite);
		administratorComposite.setLayout(new GridLayout(1, false));
		
		Composite admUserAccountComposite = new Composite(administratorComposite, SWT.NONE);
		admUserAccountComposite.setLayout(new GridLayout(13, false));
		GridData gd_admUserAccountComposite = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 21);
		gd_admUserAccountComposite.heightHint = 486;
		gd_admUserAccountComposite.widthHint = 767;
		admUserAccountComposite.setLayoutData(gd_admUserAccountComposite);
		formToolkit.adapt(admUserAccountComposite);
		formToolkit.paintBordersFor(admUserAccountComposite);
		
		Composite composite = new Composite(admUserAccountComposite, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.LEFT, SWT.TOP, true, true, 11, 1);
		gd_composite.heightHint = 417;
		gd_composite.widthHint = 313;
		composite.setLayoutData(gd_composite);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		
		List list = new List(composite, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.TOP, true, true, 2, 1);
		gd_list.widthHint = 305;
		gd_list.heightHint = 373;
		list.setLayoutData(gd_list);
		formToolkit.adapt(list, true, true);
		
		CLabel lblUserGroupsAnd = new CLabel(composite, SWT.NONE);
		lblUserGroupsAnd.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		formToolkit.adapt(lblUserGroupsAnd);
		formToolkit.paintBordersFor(lblUserGroupsAnd);
		lblUserGroupsAnd.setText("User Groups and Accounts");
		
		Composite composite_2 = new Composite(admUserAccountComposite, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_composite_2.heightHint = 420;
		gd_composite_2.widthHint = 96;
		composite_2.setLayoutData(gd_composite_2);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		
		Button admEditBtn = new Button(composite_2, SWT.NONE);
		admEditBtn.setBounds(10, 29, 75, 25);
		formToolkit.adapt(admEditBtn, true, true);
		admEditBtn.setText("Edit");
		
		Composite composite_3 = new Composite(admUserAccountComposite, SWT.NONE);
		composite_3.setLayout(new GridLayout(1, false));
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_composite_3.heightHint = 416;
		gd_composite_3.widthHint = 338;
		composite_3.setLayoutData(gd_composite_3);
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		
		ListViewer listViewer = new ListViewer(composite_3, SWT.BORDER | SWT.V_SCROLL);
		List list_1 = listViewer.getList();
		GridData gd_list_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_list_1.widthHint = 317;
		gd_list_1.heightHint = 376;
		list_1.setLayoutData(gd_list_1);
		
		CLabel lblUserAccountAnd = new CLabel(composite_3, SWT.NONE);
		lblUserAccountAnd.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblUserAccountAnd);
		formToolkit.paintBordersFor(lblUserAccountAnd);
		lblUserAccountAnd.setText("User Account and Group Settings");
		
		Composite admSettingsComposite = new Composite(administratorComposite, SWT.NONE);
		GridData gd_admSettingsComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_admSettingsComposite.heightHint = 96;
		gd_admSettingsComposite.widthHint = 768;
		admSettingsComposite.setLayoutData(gd_admSettingsComposite);
		admSettingsComposite.setBounds(0, 0, 64, 64);
		formToolkit.adapt(admSettingsComposite);
		formToolkit.paintBordersFor(admSettingsComposite);

	}
}
