package storage;
import java.io.Serializable;

public class Filter implements Serializable {
	private static final long serialVersionUID = -3172281602532772481L;
	static int ids = 0;
	private int id;
	private String regex;
	public Filter(String r){
		id = ids++;
		regex = r;
	}
	public String getRegex(){
		return regex;
	}
	public int getId(){
		return id;
	}
	public void changeId(int i){
		id = i;
	}
	public void changeRegex(String r){
		regex = r;
	}
}
