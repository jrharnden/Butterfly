import org.eclipse.swt.widgets.Shell;
import gui.view.*;


public class Backbone {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
			Backbone b = new Backbone();
			b.startDisplay();
	}
	
	public void startDisplay(){
		Shell shell = new Shell();
		LoginShell lw = new LoginShell(shell);
		ApplicationWindow ap = new ApplicationWindow();
		//lw.open(ap);
		ap.open();
	}
	

}

