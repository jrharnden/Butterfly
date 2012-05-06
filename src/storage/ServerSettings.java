package storage;

import java.io.Serializable;

/**
 * Encapsulates server settings, all public to make easy access
 * 
 * @author Zong Xiong
 */
public class ServerSettings implements Serializable{
	private static final long serialVersionUID = -5076353292105300183L;
	public boolean 	logEnabled = false;
	public boolean 	dialogEnabled = true;
	public boolean 	connectionListEnabled = true;
	public int		serverPort = 8080;
}
