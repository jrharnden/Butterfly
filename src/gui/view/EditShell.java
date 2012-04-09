package gui.view;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import storage.Account;
import storage.Filter;
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class EditShell {
	protected Shell shell;
	protected Object result;
	protected Display display;
	protected Account account;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public EditShell(Display d) {
		display = d;
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
		shell = new Shell(display, SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		shell.setText("Filter Edit");
		shell.setSize(786, 510);
		shell.setLayout(new FormLayout());
		
		Composite filterComposite = new Composite(shell, SWT.NONE);
		filterComposite.setLayoutData(new FormData());
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
		
		//Active Filer Composite
		Composite filterActiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterActiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterActiveComposite.heightHint = 465;
		gd_filterActiveComposite.widthHint = 333;
		
		filterActiveComposite.setLayoutData(gd_filterActiveComposite);
		formToolkit.adapt(filterActiveComposite);
		formToolkit.paintBordersFor(filterActiveComposite);
		filterActiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
			
			//Active List
			ListViewer filterActiveListViewer = new ListViewer(filterActiveComposite, SWT.BORDER | SWT.V_SCROLL);
			List filterActiveList = filterActiveListViewer.getList();
			ArrayList<Filter> f = account.getActiveFilters();
			for(Filter fil: f){
				filterActiveList.add(fil.toString());
			}
		
		//Button composite
		Composite filterBtnComposite = new Composite(filterComposite_1, SWT.NONE);
		filterBtnComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData gd_filterBtnComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterBtnComposite.heightHint = 465;
		gd_filterBtnComposite.widthHint = 85;
		filterBtnComposite.setLayoutData(gd_filterBtnComposite);
		formToolkit.adapt(filterBtnComposite);
		formToolkit.paintBordersFor(filterBtnComposite);
		
		//North button composite
		Composite filterBtnComposite_NORTH = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_NORTH);
		formToolkit.paintBordersFor(filterBtnComposite_NORTH);
		filterBtnComposite_NORTH.setLayout(new FillLayout(SWT.HORIZONTAL));
		
			//Select new import/export file, or new user/group
			Button btnSelectNew = new Button(filterBtnComposite_NORTH, SWT.NONE);
			formToolkit.adapt(btnSelectNew, true, true);
			btnSelectNew.setText("Select New");
		
		//Center Button Composite
		Composite filterBtnComposite_CENTER = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_CENTER);
		formToolkit.paintBordersFor(filterBtnComposite_CENTER);
		filterBtnComposite_CENTER.setLayout(new FillLayout(SWT.HORIZONTAL));
		
			//Add items from inactive to active <
			Button btnAdd = new Button(filterBtnComposite_CENTER, SWT.NONE);
			formToolkit.adapt(btnAdd, true, true);
			btnAdd.setText("ADD");
			
			//Remove items from active to inactive >
			Button btnRemove = new Button(filterBtnComposite_CENTER, SWT.NONE);
			formToolkit.adapt(btnRemove, true, true);
			btnRemove.setText("REMOVE");
		
		//South Button Composite
		Composite filterBtnComposite_SOUTH = new Composite(filterBtnComposite, SWT.NONE);
		formToolkit.adapt(filterBtnComposite_SOUTH);
		formToolkit.paintBordersFor(filterBtnComposite_SOUTH);
		filterBtnComposite_SOUTH.setLayout(new FillLayout(SWT.HORIZONTAL));
			
			//Submit active/inactive filters
			Button btnNewButton = new Button(filterBtnComposite_SOUTH, SWT.NONE);
			formToolkit.adapt(btnNewButton, true, true);
			btnNewButton.setText("Submit");
		
		//Inactive filter composite
		Composite filterInactiveComposite = new Composite(filterComposite_1, SWT.NONE);
		GridData gd_filterInactiveComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterInactiveComposite.heightHint = 465;
		gd_filterInactiveComposite.widthHint = 333;
		filterInactiveComposite.setLayoutData(gd_filterInactiveComposite);
		formToolkit.adapt(filterInactiveComposite);
		formToolkit.paintBordersFor(filterInactiveComposite);
		filterInactiveComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
			//Inactive filter list
			ListViewer filterInactiveListViewer = new ListViewer(filterInactiveComposite, SWT.BORDER | SWT.V_SCROLL);
			List filterInactiveList = filterInactiveListViewer.getList();
	}
}
