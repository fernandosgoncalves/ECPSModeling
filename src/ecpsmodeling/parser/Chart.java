package ecpsmodeling.parser;

import java.util.ArrayList;

public class Chart {
	private ArrayList<Transition> transmissions;

	private boolean permitEqualNameModes;// true se modes puderem ter nomes iguais
	private boolean isMode;// true se é um mode, false se é um behavior
	
	private String name;
	private String id;
	
	private ArrayList<Mode> modes;

	private String chartOwner;
	
	public Chart(String name, String id, String chartOwner, boolean isMode) {
		this.name = name;// getSubsystemRealName(name);
		this.id = id;
		this.modes = new ArrayList<Mode>();
		this.chartOwner = chartOwner;
		this.transmissions = new ArrayList<Transition>();
		this.isMode = isMode;
		this.permitEqualNameModes = false;
	}

	public static boolean compareSubsystemName(String chart, String subsystem) {
		if (chart.startsWith("\""))
			chart = chart.substring(1, chart.length() - 1);
		if (chart.toLowerCase().contains(subsystem.toLowerCase()))
			return true;
		else
			return false;
	}

	public String getName() {
		return this.name;
	}

	public String getId() {
		return this.id;
	}

	public String getChartOwner() {
		if (chartOwner != null) {
			return chartOwner;
		} else {
			return "";
		}
	}

	public boolean addMode(String mode, String id) {
		String letter = "";
		if (mode.startsWith("\""))
			mode = mode.substring(1, mode.length() - 1);
		if (mode.indexOf("\\") > 0)
			mode = mode.substring(0, mode.indexOf("\\")).toLowerCase();
		if (isMode) {
			letter = "m_";
		} else {
			letter = "s_";
		}
		if (permitEqualNameModes == false) {
			for (int i = 0; i < modes.size(); i++) {
				if (modes.get(i).getName().equals(mode)) {
					return false;
				}
			}
		}
		this.modes.add(new Mode(mode, letter, id));
		return true;
	}

	public void addTransition(String origin, String destiny, String label) {
		label = label.replace("\"", "");
		this.transmissions.add(new Transition(this.getMode(origin), this.getMode(destiny), label));
	}

	public ArrayList<Mode> getModes() {
		return this.modes;
	}

	public ArrayList<Transition> getTransitions() {
		return this.transmissions;
	}

	public Mode getLastMode() {
		return this.modes.get(this.modes.size() - 1);
	}

	public Transition getLastTransition() {
		return this.transmissions.get(this.transmissions.size() - 1);
	}

	public Mode getMode(String id) {
		for (int i = 0; i < modes.size(); i++) {
			if (modes.get(i).getId().equals(id)) {
				return modes.get(i);
			}
		}
		return null;
	}
}
