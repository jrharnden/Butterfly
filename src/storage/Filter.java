package storage;

import java.io.Serializable;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Filter implements Serializable {
	private static final long serialVersionUID = -3172281602532772481L;
	public static int ids = 0;
	private int id;
	private String regex, name, replaceWith;

	// TODO should probably have flags for the Pattern class and some way to set them in UI
	public Filter(String name, String regex, String replaceWith) throws PatternSyntaxException {
		Pattern.compile(regex);
		id = ids++;
		this.regex = regex;
		this.name = name;
		this.replaceWith = replaceWith;
	}

	public Filter(Filter other) {
		id = other.getId();
		regex = other.getRegex();
		name = other.getName();
		replaceWith = other.getReplaceWith();
	}
	
	public Filter makeCopyWithNewId() {
		Filter fil = new Filter(this);
		fil.changeId(ids++);
		return fil;
	}

	public Filter(String r) {
		id = ids++;
		regex = name = r;
	}

	public String getRegex() {
		return regex;
	}

	public String getReplaceWith() {
		return replaceWith;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void changeName(String n) {
		name = n;
	}

	public void changeId(int i) {
		id = i;
	}

	public void changeRegex(String r) throws PatternSyntaxException {
		Pattern.compile(r);
		regex = r;
	}

	public void changeReplaceWith(String r) {
		replaceWith = r;
	}

	@Override
	public String toString() {
		return id+": "+name;
	}
}
