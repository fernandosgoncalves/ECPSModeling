package ecpsmodeling.parser;

public class Line {
	private String destSystem;
	private String srcSystem;
	private String destPort;
	private String srcPort;
	
	public Line(String srcSystem, String srcPort, String destSystem, String destPort) {
		setSrcSystem(srcSystem);
		setSrcPort(srcPort);
		setDestSystem(destSystem);
		setDestPort(destPort);
	}

	public String getSrcSystem() {
		return srcSystem;
	}

	public void setSrcSystem(String srcSystem) {
		srcSystem = srcSystem.replace("\\n1", "");
		srcSystem = srcSystem.replace("\\n", "");
		this.srcSystem = srcSystem.toLowerCase();
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public String getDestSystem() {
		return destSystem;
	}

	public void setDestSystem(String destSystem) {
		destSystem = destSystem.replace("\\n1", "");
		destSystem = destSystem.replace("\\n", "");
		this.destSystem = destSystem.toLowerCase();
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String toString() {
		return "srcSystem: " + srcSystem + " - " + "srcPort: " + srcPort + " -- " + "destSystem: " + destSystem + " - "
				+ "destPort: " + destPort;
	}
}
