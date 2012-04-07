import org.eclipse.swt.widgets.Shell;

import gui.view.*;
import storage.*;


public class Backbone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Shell shell = new Shell();
			LoginShell lw = new LoginShell(shell);
			ApplicationWindow ap = new ApplicationWindow();
			lw.open(ap);
			ap.open();
	}

}
