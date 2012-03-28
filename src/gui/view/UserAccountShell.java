package gui.view;

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

import swing2swt.layout.FlowLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class UserAccountShell {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	/**
	 * Launch the application.
	 * @param args
	 */
	public UserAccountShell() {
	}


	/**
	 * Create contents of the shell.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		Display display = Display.getDefault();
		Shell UserAccountShell = new Shell(SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
		UserAccountShell.setText("Butterfly User Account Edit");
		UserAccountShell.setSize(800, 600);
		UserAccountShell.setLayout(new FormLayout());
		
		Composite composite = new Composite(UserAccountShell, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 572);
		fd_composite.right = new FormAttachment(0, 794);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite filterComposite = new Composite(composite, SWT.NONE);
		formToolkit.paintBordersFor(filterComposite);
		filterComposite.setLayout(new swing2swt.layout.BorderLayout(0, 0));
		
		Composite filterButtonComposite_SOUTH = new Composite(filterComposite, SWT.NONE);
		filterButtonComposite_SOUTH.setLayoutData(swing2swt.layout.BorderLayout.SOUTH);
		formToolkit.adapt(filterButtonComposite_SOUTH);
		formToolkit.paintBordersFor(filterButtonComposite_SOUTH);
		filterButtonComposite_SOUTH.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite usrAccBtnComposite = new Composite(filterButtonComposite_SOUTH, SWT.NONE);
		formToolkit.adapt(usrAccBtnComposite);
		formToolkit.paintBordersFor(usrAccBtnComposite);
		usrAccBtnComposite.setLayout(new GridLayout(4, false));
		
		Button btnDeleteFilter = new Button(usrAccBtnComposite, SWT.NONE);
		GridData gd_btnDeleteFilter = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_btnDeleteFilter.widthHint = 161;
		gd_btnDeleteFilter.heightHint = 53;
		btnDeleteFilter.setLayoutData(gd_btnDeleteFilter);
		formToolkit.adapt(btnDeleteFilter, true, true);
		btnDeleteFilter.setText("Delete");
		
		
		Button btnEditFilter = new Button(usrAccBtnComposite, SWT.NONE);
		GridData gd_btnEditFilter = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_btnEditFilter.widthHint = 150;
		gd_btnEditFilter.heightHint = 53;
		btnEditFilter.setLayoutData(gd_btnEditFilter);
		formToolkit.adapt(btnEditFilter, true, true);
		btnEditFilter.setText("Edit");
		
		Button btnNewButton = new Button(usrAccBtnComposite, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_btnNewButton.widthHint = 225;
		gd_btnNewButton.heightHint = 53;
		btnNewButton.setLayoutData(gd_btnNewButton);
		formToolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("Change Password");
		
		Button btnNewButton_1 = new Button(usrAccBtnComposite, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_btnNewButton_1.widthHint = 234;
		gd_btnNewButton_1.heightHint = 54;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		formToolkit.adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("Change User Group");
		
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
		GridData gd_lblActiveFilters = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblActiveFilters.heightHint = 25;
		lblActiveFilters.setLayoutData(gd_lblActiveFilters);
		formToolkit.adapt(lblActiveFilters, true, true);
		lblActiveFilters.setText("User Filters");
		
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
		lblNewLabel.setText("Administrator Filters");
		
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
				
				Button btnRemoveFromActive = new Button(filterAddRemoveComposite_CENTER, SWT.NONE);
				GridData gd_btnRemoveFromActive = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_btnRemoveFromActive.widthHint = 62;
				btnRemoveFromActive.setLayoutData(gd_btnRemoveFromActive);
				formToolkit.adapt(btnRemoveFromActive, true, true);
				btnRemoveFromActive.setText("Remove");
				new Label(filterAddRemoveComposite_CENTER, SWT.NONE);
				new Label(filterAddRemoveComposite_CENTER, SWT.NONE);
		
		UserAccountShell.open();
		UserAccountShell.layout();
		while (!UserAccountShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
