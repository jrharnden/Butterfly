package gui.view;

import java.io.File;
import java.util.ArrayList;

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
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;


public class EditShell {
	private String sName = "";
	JFileChooser chooser;
	protected Shell shell;
	protected Object result;
	protected Display display;
	protected Account account;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text textHandle;
	
	/**
	 * Filter edit shell [Active <-> Inactive]
	 * @param args
	 */
	public EditShell(Display d) {
		display = d;
		sName = "";
	}
	
	/**
	 * 
	 * @param d
	 * @param s
	 */
	public EditShell(Display d, String s){
		display =d;
		sName = s;
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
		shell.setText(sName + " Filters");
		shell.setSize(786, 600);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite filterComposite = new Composite(shell, SWT.NONE);
		formToolkit.paintBordersFor(filterComposite);
		filterComposite.setLayout(new GridLayout(1, false));
		
		Composite filterComposite_1 = new Composite(filterComposite, SWT.NONE);
		GridData gd_filterComposite_1 = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_filterComposite_1.heightHint = 525;
		filterComposite_1.setLayoutData(gd_filterComposite_1);
		formToolkit.adapt(filterComposite_1);
		formToolkit.paintBordersFor(filterComposite_1);
		filterComposite_1.setLayout(new GridLayout(3, false));
		
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
			List filterActiveList = filterActiveListViewer.getList();
			ArrayList<Filter> f = account.getActiveFilters();
			for(Filter fil: f){
				filterActiveList.add(fil.toString());
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
		filterBtnComposite_NORTH.setLayout(new FillLayout(SWT.HORIZONTAL));
		
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
			List filterInactiveList = filterInactiveListViewer.getList();
			f = account.getInactiveFilters();
			for(Filter fil: f){
				filterInactiveList.add(fil.toString());
			}			
			Composite btnBarComposite = new Composite(filterComposite, SWT.NONE);
			btnBarComposite.setLayout(new GridLayout(4, false));
			GridData gd_btnBarComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
			gd_btnBarComposite.heightHint = 37;
			gd_btnBarComposite.widthHint = 771;
			btnBarComposite.setLayoutData(gd_btnBarComposite);
			btnBarComposite.setBounds(0, 0, 64, 64);
			formToolkit.adapt(btnBarComposite);
			formToolkit.paintBordersFor(btnBarComposite);
			
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
						  File file = new File(path);
						  if (file.isFile()){
							  displayFiles(new String[] { file.toString()});
						  	  Accounts accounts = new Accounts();
						  	  accounts.loadAccounts();
						  	  accounts.importFilters(account, file);
						  	//Active List
								List filterActiveList = filterActiveListViewer.getList();
								ArrayList<Filter> f = account.getActiveFilters();
								for(Filter fil: f){
									filterActiveList.add(fil.toString());
								}
						  	//Inactive filter list
								List filterInactiveList = filterInactiveListViewer.getList();
								f = account.getInactiveFilters();
								for(Filter fil: f){
									filterInactiveList.add(fil.toString());
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
			
			//Save the changes
			Button btnSave = new Button(btnBarComposite, SWT.NONE);
			GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btnSave.widthHint = 75;
			btnSave.setLayoutData(gd_btnSave);
			formToolkit.adapt(btnSave, true, true);
			btnSave.setText("Save");
			
			btnSave.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){
					switch (e.type){
					case SWT.Selection:
						//TODO add save functionality for backend filter save
						System.out.println("You wish you could save");
					}
				}
			});
			//Cancel the changes
			Button btnCancel = new Button(btnBarComposite, SWT.NONE);
			GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btnCancel.widthHint = 75;
			btnCancel.setLayoutData(gd_btnCancel);
			formToolkit.adapt(btnCancel, true, true);
			btnCancel.setText("Cancel");
			
			btnCancel.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event e){  
					switch(e.type) {
						case SWT.Selection:
							shell.close();
							shell.dispose();
						
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
