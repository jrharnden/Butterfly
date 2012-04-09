package storage;
import java.io.Serializable;

public class Filter implements Serializable {
	private static final long serialVersionUID = -3172281602532772481L;
	static int ids = 0;
	private int id;
	private String regex, name, replaceWith;
	//TODO should probably have flags for the Pattern class and some way to set them in UI
	public Filter(String name, String regex, String replaceWith){
		id = ids++;
		this.regex = regex;
		this.name = name;
		this.replaceWith = replaceWith;
	}
	public Filter(String r){
		id = ids++;
		regex = name = r;
	}	
	public String getRegex(){
		return regex;
	}	
	public String getReplaceWith(){
		return replaceWith;
	}	
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public void changeName(String n){
		name = n;
	}
	public void changeId(int i){
		id = i;
	}
	public void changeRegex(String r){
		regex = r;
	}
	public void changeReplaceWith(String r){
		replaceWith = r;
	}
	@Override
	public String toString(){
		return name+":"+regex;
	}
}
