package storage;
import java.io.Serializable;

public class Filter implements Serializable {
	private static final long serialVersionUID = -3172281602532772481L;
	static int ids = 0;
	private int id;
	private String regex, name;
	public Filter(String n, String r){
		id = ids++;
		regex = r;
		name = n;
	}
	public Filter(String r){
		id = ids++;
		regex = name = r;
	}
	public String getRegex(){
		return regex;
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
	@Override
	public String toString(){
		return name+":"+regex;
	}
}
