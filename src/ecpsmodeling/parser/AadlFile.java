package ecpsmodeling.parser;

public class AadlFile {
	public static String BASETYPESSIMULINK = "package Base_Types_Simulink\n" + "public\n" + "  with Data_Model;\n\n"
			+ "  data boolean\n" + "  properties\n" + "    Data_Model::Data_Representation => Boolean;\n"
			+ "  end boolean;\n\n" + "  data integer\n" + "  properties\n"
			+ "    Data_Model::Data_Representation => Integer;\n" + "  end integer;\n\n" + "end Base_Types_Simulink;";

	public String programsSimulink = "PACKAGE programs_simulink\npublic\n\n";
	
	private SubSystem rootSubsystem;
	
	private String packageName;

	public AadlFile() {
		rootSubsystem = new SubSystem("", this, null);
	}

	public void setPackageName(String packageName) {
		if (packageName.startsWith("\""))
			this.packageName = packageName.substring(1, packageName.length() - 1);
		else
			this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void addSubSystem(String name) {
		if (name.startsWith("\""))
			this.rootSubsystem = new SubSystem(name.substring(1, name.length() - 1), this, null);
		else
			this.rootSubsystem = new SubSystem(name, this, null);
	}
	
	public SubSystem getSubSystem() {
		return this.rootSubsystem;
	}

	// melhorar o codigo.
	public SubSystem searchSubSystem(String name, boolean searchType) {
		// retira as aspas
		if (name.contains("\""))
			name = name.substring(1, name.length() - 1).toLowerCase();
		else
			name = name.toLowerCase();

		// o nome do subsytem que deve ser procurado agora
		String search = "";

		if (searchType) {
			// para o nome da thread
			search = name.substring(name.lastIndexOf("/") + 1, name.length());
		} else {
			// para o nome do subsystem
			String temp = name.substring(0, name.lastIndexOf("/"));
			search = name.substring(temp.lastIndexOf("/") + 1, temp.length());
		}
		// melhorar...
		for (int i = 0; i < rootSubsystem.getSubSystemsCount(); i++) {
			SubSystem level1 = rootSubsystem.getSubSystem(i);
			if (level1.getName().equals(search)) {
				return level1;
			}
			for (int j = 0; j < level1.getSubSystemsCount(); j++) {
				SubSystem level2 = level1.getSubSystem(j);
				if (level2.getName().equals(search)) {
					return level2;
				}
				// só vai ao terceiro nivel se estiver procurando o nome da
				// thread
				if (searchType) {
					for (int k = 0; k < level2.getSubSystemsCount(); k++) {
						SubSystem level3 = level2.getSubSystem(k);
						if (level3.getName().equals(search)) {
							return level3;
						}
					}
				}
			}
		}
		return null;
	}

	public String toString() {
		String aadl = "";
		aadl += "PACKAGE " + packageName + "\n";
		aadl += "PUBLIC\n";
		aadl += "  with Base_Types_Simulink;\n";
		aadl += "  with programs_simulink;\n";
		aadl += "SYSTEM " + rootSubsystem.getName() + "\n";
		aadl += "END " + rootSubsystem.getName() + ";\n\n";
		aadl += "SYSTEM IMPLEMENTATION " + rootSubsystem.getName() + ".impl\n";
		aadl += "SUBCOMPONENTS\n";
		for (int i = 0; i < rootSubsystem.getSubSystemsCount(); i++) {
			aadl += "  " + rootSubsystem.getSubSystem(i).getFullName() + ": " + rootSubsystem.getSubSystem(i).getMark()
					+ " " + rootSubsystem.getSubSystem(i).getFullName() + ".impl;\n";
		}
		aadl += "CONNECTIONS\n";
		int c = 1;
		for (int i = 0; i < rootSubsystem.getAllLines().size(); i++) {
			Port outPort = rootSubsystem.findOutPort(rootSubsystem.getAllLines().get(i).getSrcSystem(),
					rootSubsystem.getAllLines().get(i).getSrcPort());
			if (outPort != null)
				aadl += "    C" + c + ": PORT "
						+ rootSubsystem.getFullName(rootSubsystem.getAllLines().get(i).getSrcSystem()) + "."
						+ outPort.getName() + " -> ";
			else
				aadl += "    C" + c + ": PORT "
						+ rootSubsystem.getFullName(rootSubsystem.getAllLines().get(i).getSrcSystem()) + " -> ";
			Port inPort = rootSubsystem.findInPort(rootSubsystem.getAllLines().get(i).getDestSystem(),
					rootSubsystem.getAllLines().get(i).getDestPort());
			if (inPort != null)
				aadl += rootSubsystem.getFullName(rootSubsystem.getAllLines().get(i).getDestSystem()) + "."
						+ inPort.getName() + ";\n";
			else
				aadl += rootSubsystem.getFullName(rootSubsystem.getAllLines().get(i).getDestSystem()) + ";\n";
			c++;
		}
		aadl += "END " + rootSubsystem.getName() + ".impl;\n\n";
		for (int i = 0; i < rootSubsystem.getSubSystemsCount(); i++) {
			aadl += rootSubsystem.getSubSystem(i).toString();
		}
		aadl += "END " + packageName + ";";
		return aadl;
	}
}
