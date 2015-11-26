package ecpsmodeling.parser;

public class Transition {
	private Mode destiny;
	private Mode origin;

	private String label;

	public Transition(Mode origin, Mode destiny, String label) {
		this.origin = origin;
		this.destiny = destiny;
		if (label.startsWith("\""))
			label = label.substring(1, label.length() - 1);
		this.label = label.toLowerCase();
	}

	public Mode getOrigin() {
		return origin;
	}

	public Mode getDestiny() {
		return destiny;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
