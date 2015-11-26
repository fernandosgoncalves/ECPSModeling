package ecpsmodeling.parser;

import java.util.ArrayList;

public class Mode {
	private String initialMode;
	private String annotation;
	private String letter;
	private String name;
	private String id;

	private boolean isComplete;
	private boolean isInitial;

	private ArrayList<String> components;

	public Mode(String name, String letter, String id) {
		this.name = name;
		this.letter = letter;
		this.id = id;
		this.isComplete = true;
		this.components = new ArrayList<String>();
		this.annotation = "";
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return letter + name;
	}

	public String getId() {
		return id;
	}

	public void addComponent(String component) {
		this.components.add(component);
	}

	public ArrayList<String> getComponents() {
		return this.components;
	}

	public boolean isInitial() {
		return isInitial;
	}

	public void setInitial(boolean isInitial) {
		this.isInitial = isInitial;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getInitialMode() {
		return initialMode;
	}

	public void setInitialMode(String initialMode) {
		if (initialMode.startsWith("\""))
			initialMode = initialMode.substring(1, initialMode.length() - 1);
		initialMode = initialMode.toLowerCase();
		this.initialMode = initialMode;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
}
