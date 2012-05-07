package gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ErrorMessageBox {
	MessageBox mb;
	
	public ErrorMessageBox(Shell parent, String message) {
		mb = new MessageBox(parent, SWT.ICON_ERROR|SWT.OK | SWT.CANCEL);
		mb.setText("Error");
		mb.setMessage(message);
	}

	public void open() {
		mb.open();
	}
}
