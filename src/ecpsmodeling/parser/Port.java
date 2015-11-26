package ecpsmodeling.parser;

public class Port {
	private String system;
	private String name;
	private String number;
	private String type;
	private String dataType;
	private SubSystem parent;
	public static String DATA = "Port number";
	public static String EVENT = "Signal name";
	public static String EVENT_DATA = "Port number and signal name";

	public Port(String system, String name, String number, String type, String dataType, SubSystem parent) {
		this.system = system.toLowerCase();
		setName(name);
		this.number = number;
		setType(type);
		setDataType(dataType);
		this.parent = parent;
	}

	public void setDataType(String dataType) {
		if (dataType.startsWith("\""))
			dataType = dataType.substring(1, dataType.length() - 1);
		if (dataType.equals("int32"))
			dataType = "integer";
		if (dataType.equals("boolean"))
			dataType = "boolean";
		this.dataType = dataType;

	}

	public String getDataType() {
		return this.dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toLowerCase();
		this.name = this.name.replace("\\n1", "");
		this.name = this.name.replace("\\n", "");
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type.toUpperCase();
	}

	public void setType(String type) {
		/*
		 * if(type.startsWith("\"")) type = type.substring(1, type.length() -
		 * 1); if(type.equals(DATA)) type = "data"; if(type.equals(EVENT)) type
		 * = "event"; if(type.equals(EVENT_DATA)) type = "event data"; this.type
		 * = type;
		 */
		// a partir de 24-06-14 todas as portas serão "event data"
		this.type = "event data";
	}

	public String getSystem() {
		return system;
	}

	public SubSystem getParent() {
		return parent;
	}
}
