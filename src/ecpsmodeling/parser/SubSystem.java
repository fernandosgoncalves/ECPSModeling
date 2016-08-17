package ecpsmodeling.parser;

import java.util.ArrayList;

public class SubSystem {
	private ArrayList<String> dataSubcomponents;

	private ArrayList<SubSystem> subSystems;

	private ArrayList<Port> outPorts;
	private ArrayList<Port> inPorts;

	private ArrayList<Line> lines;

	private AadlFile aadlFile;

	private SubSystem parent;

	private Chart behavior;
	private Chart chart;

	private String name;
	private String mark;

	public static String MARK_THREAD_BEHAVIOR = "THREAD/BEHAVIOR";
	public static String MARK_PROCESS_THREAD = "PROCESS/THREAD";
	public static String MARK_INTERMEDIATE = "INTERMEDIATE";
	public static String MARK_PROCESS = "PROCESS";
	public static String MARK_SYSTEM = "SYSTEM";
	public static String MARK_THREAD = "THREAD";
	public static String MARK_DEVICE = "DEVICE";
	public static String MARK_MODES = "MODES";

	public SubSystem(String name, AadlFile aadlFile, SubSystem parent) {
		setName(name);
		this.mark = "";
		this.outPorts = new ArrayList<Port>();
		this.inPorts = new ArrayList<Port>();
		this.subSystems = new ArrayList<SubSystem>();
		this.lines = new ArrayList<Line>();
		this.aadlFile = aadlFile;
		this.chart = null;
		this.behavior = null;
		this.dataSubcomponents = new ArrayList<String>();
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (name.startsWith("\""))
			name = name.substring(1, name.length() - 1);
		if (name.contains("device"))
			name = name.substring(name.indexOf("device") + 7, name.length());
		this.name = name.toLowerCase();
	}

	public String getMark() {
		if (mark.equals(MARK_PROCESS_THREAD)) {
			return MARK_PROCESS;
		}
		if (mark.equals(MARK_THREAD_BEHAVIOR)) {
			return MARK_THREAD;
		}
		return mark;
	}

	public void setMark(String mark) {
		if (mark.startsWith("\""))
			mark = mark.substring(1, mark.length() - 1);
		mark = mark.toUpperCase();
		this.mark = mark;
	}

	public String getLetter() {
		if (this.mark.equals(MARK_PROCESS))
			return "p_";
		if (this.mark.equals(MARK_SYSTEM))
			return "s_";
		if (this.mark.equals(MARK_THREAD))
			return "t_";
		if (this.mark.equals(MARK_PROCESS_THREAD))
			return "p_";
		if (this.mark.equals(MARK_THREAD_BEHAVIOR))
			return "t_";
		if (this.mark.equals(MARK_DEVICE))
			return "d_";
		if (this.mark.equals(MARK_INTERMEDIATE))
			return "dt_";
		return "";
	}

	public String getFullName() {
		return this.getLetter() + this.getName();
	}

	public String getFullName(String name) {
		for (int i = 0; i < subSystems.size(); i++) {
			if (name.equals(subSystems.get(i).name))
				return subSystems.get(i).getFullName();
		}
		return name;
	}

	public String getSubSystemMark(String name) {
		for (int i = 0; i < subSystems.size(); i++) {
			if (name.equals(subSystems.get(i).name))
				return subSystems.get(i).getMark();
		}
		return name;
	}

	public Chart getChart() {
		return chart;
	}

	public void setChart(String name, String chartOwner, String id) {
		this.chart = new Chart(name, id, chartOwner, true);
	}

	public void removeChart() {
		this.chart = null;
	}

	public Chart getBehavior() {
		return this.behavior;
	}

	public void setBehavior(String name, String id) {
		this.behavior = new Chart(name, id, null, false);
	}

	public void addDataSubcomponents(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < dataSubcomponents.size(); i++) {
			if (dataSubcomponents.get(i).equals(name)) {
				return;
			}
		}
		this.dataSubcomponents.add(name);
	}

	public String getLastDataSubcomponent() {
		if (this.dataSubcomponents.size() > 0) {
			return this.dataSubcomponents.get(this.dataSubcomponents.size() - 1);
		}
		return null;
	}

	public SubSystem getParent() {
		return parent;
	}

	public void setParent(SubSystem parent) {
		this.parent = parent;
	}

	public void addOutPort(String system, String name, String type, String dataType, SubSystem parent) {
		this.addOutPort(system, name, "1", type, dataType, parent);
	}

	public void addOutPort(String system, String name, String number, String type, String dataType, SubSystem parent) {
		if (system.startsWith("\""))
			system = system.substring(1, system.length() - 1);
		if (system.contains("device"))
			system = system.substring(system.indexOf("device") + 7, system.length());
		if (name.startsWith("\""))
			name = name.substring(1, name.length() - 1);
		if (number.startsWith("\""))
			this.outPorts.add(new Port(system, name, number.substring(1, number.length() - 1), type, dataType, parent));
		else
			this.outPorts.add(new Port(system, name, number, type, dataType, parent));
	}

	public void addInPort(String system, String name, String type, String dataType, SubSystem parent) {
		this.addInPort(system, name, "1", type, dataType, parent);
	}

	public void addInPort(String system, String name, String number, String type, String dataType, SubSystem parent) {
		if (system.startsWith("\""))
			system = system.substring(1, system.length() - 1);
		if (system.contains("device"))
			system = system.substring(system.indexOf("device") + 7, system.length());
		if (name.startsWith("\""))
			name = name.substring(1, name.length() - 1);
		if (number.startsWith("\""))
			this.inPorts.add(new Port(system, name, number.substring(1, number.length() - 1), type, dataType, parent));
		else
			this.inPorts.add(new Port(system, name, number, type, dataType, parent));
	}

	public ArrayList<Port> getOutPorts() {
		return this.outPorts;
	}

	public Port getOutPort(int position) {
		// se for uma posição valida retorna o system
		if (this.outPorts.size() > position)
			return this.outPorts.get(position);
		return null;
	}

	/*
	 * FG: Return the amount of output ports
	 */
	public int getOutPortsCount() {
		return this.outPorts.size();
	}

	public ArrayList<Port> getInPorts() {
		return this.inPorts;
	}

	/*
	 * FG: Return the amount of input ports
	 */
	public int getInPortsCount() {
		return this.inPorts.size();
	}

	public Port getInPort(int position) {
		// se for uma posição valida retorna o system
		if (this.inPorts.size() > position)
			return this.inPorts.get(position);
		return null;
	}

	public Port findOutPort(String system, String port) {
		for (int i = 0; i < subSystems.size(); i++) {
			if (system.equals(subSystems.get(i).name)) {
				ArrayList<Port> ports = subSystems.get(i).outPorts;
				for (int j = 0; j < ports.size(); j++) {
					if (port.equals(ports.get(j).getNumber()))
						return ports.get(j);
				}
			}
		}
		return null;
	}

	public Port findOutPort(String portName) {
		//System.out.println(outPorts.size());
		for (int i = 0; i < outPorts.size(); i++) {
			//System.out.println(portName + " - " + outPorts.get(i).getName());
			if (portName.equals(outPorts.get(i).getName())) {
				return outPorts.get(i);
			}
		}
		return null;
	}

	public Port findInPort(String system, String port) {
		for (int i = 0; i < subSystems.size(); i++) {
			if (system.equals(subSystems.get(i).name)) {
				ArrayList<Port> ports = subSystems.get(i).inPorts;
				for (int j = 0; j < ports.size(); j++) {
					if (port.equals(ports.get(j).getNumber()))
						return ports.get(j);
				}
			}
		}
		return null;
	}

	public Port findInPort(String portName) {
		for (int i = 0; i < inPorts.size(); i++) {
			if (portName.equals(inPorts.get(i).getName())) {
				return inPorts.get(i);
			}
		}
		return null;
	}

	public ArrayList<SubSystem> getAllSubSystem() {
		return this.subSystems;
	}

	public SubSystem getSubSystem(int position) {
		// se for uma posição valida retorna o system
		if (this.subSystems.size() > position)
			return this.subSystems.get(position);
		return null;
	}

	public int getSubSystemsCount() {
		return this.subSystems.size();
	}

	public void addSubSystem(SubSystem subsystem) {
		this.subSystems.add(subsystem);
	}

	public SubSystem getLastSubSystem() {
		// se for vazio cria um novo
		if (this.subSystems.size() == 0)
			this.subSystems.add(new SubSystem("", aadlFile, null));
		return this.subSystems.get(this.subSystems.size() - 1);
	}

	public void removeLastSubsystem() {
		this.subSystems.remove(this.subSystems.size() - 1);
	}

	public void removeLastPort() {
		this.lines.remove(this.lines.size() - 1);
	}

	public void addLine(String srcSystem, String srcPort, String dstSystem, String dstPort) {
		if (srcSystem.startsWith("\"")) {
			srcSystem = srcSystem.substring(1, srcSystem.length() - 1).trim();
		}
		if (srcSystem.contains("device")) {
			srcSystem = srcSystem.substring(srcSystem.indexOf("device") + 7, srcSystem.length());
		}
		if (dstSystem.contains("device")) {
			dstSystem = dstSystem.substring(dstSystem.indexOf("device") + 7, dstSystem.length());
		}
		if (srcPort.startsWith("\"")) {
			srcPort = srcPort.substring(1, srcPort.length() - 1);
		}
		if (dstSystem.startsWith("\"")) {
			dstSystem = dstSystem.substring(1, dstSystem.length() - 1).trim();
		}
		if (dstPort.startsWith("\"")) {
			dstPort = dstPort.substring(1, dstPort.length() - 1);
		}

		// retira as lines que sejam SFunction, Constant, Demux, sum
		String invalidSources[] = { "SFunction", "Demux", "Sum" };
		for (int i = 0; i < invalidSources.length; i++) {
			if (srcSystem.equals(invalidSources[i])) {
				return;
			}
			if (dstSystem.equals(invalidSources[i])) {
				return;
			}
			for (int j = 0; j < 10; j++) {
				if (srcSystem.equals(invalidSources[i] + j)) {
					return;
				}
				if (dstSystem.equals(invalidSources[i] + j)) {
					return;
				}
			}
		}
		this.lines.add(new Line(srcSystem, srcPort, dstSystem, dstPort));
	}

	public ArrayList<Line> getAllLines() {
		return this.lines;
	}

	/*
	 * FG: Updated the search function in order to verify also the child node of
	 * each subsystem
	 */
	public SubSystem searchSubSystem(String name) {
		SubSystem local;
		local = null;
		for (SubSystem child : this.getAllSubSystem()){
			if (child.name.equals(name)) 
				return child;
			else {
				if (child.getSubSystemsCount() > 0) {
					local = child.searchSubSystem(name);
					if(local != null)
						return local; 
				}
			}
		}
		return local;
	}
	
	public String toString() {
		String ret = "";
		if (this.getMark().equals("")) {
			return "";
		}
		// nao pode imprimir os modes
		if (!this.getMark().equals(MARK_MODES)) {
			ret += this.getMark() + " " + this.getFullName() + "\n";
			ret += "  FEATURES\n";
			if (this.getOutPorts().size() == 0 && this.getInPorts().size() == 0)
				ret += "    none;\n";
			else {
				// lista os outports
				for (int j = 0; j < this.getOutPorts().size(); j++) {
					// se for data não tem tipo de valor
					if (this.getOutPorts().get(j).getType().equals(Port.DATA))
						ret += "    " + this.getOutPorts().get(j).getName() + ": OUT "
								+ this.getOutPorts().get(j).getType() + ";\n";
					else
						ret += "    " + this.getOutPorts().get(j).getName() + ": OUT "
								+ this.getOutPorts().get(j).getType() + " PORT Base_Types_Simulink::"
								+ this.getOutPorts().get(j).getDataType() + ";\n";
				}
				// lista os inports
				for (int j = 0; j < this.getInPorts().size(); j++) {
					// se for data não tem tipo de valor
					if (this.getInPorts().get(j).getType().equals(Port.DATA))
						ret += "    " + this.getInPorts().get(j).getName() + ": OUT "
								+ this.getInPorts().get(j).getType() + ";\n";
					else
						ret += "    " + this.getInPorts().get(j).getName() + ": IN "
								+ this.getInPorts().get(j).getType() + " PORT Base_Types_Simulink::"
								+ this.getInPorts().get(j).getDataType() + ";\n";
				}
			}
			ret += "END " + this.getFullName() + ";\n\n";
			ret += this.getMark() + " IMPLEMENTATION " + this.getFullName() + ".impl\n";
			// Imprime a chamada do subsystem se o pai do pai tiver um chart, o
			// que indica que essa thread foi criada por um process/thread
			if (this.mark.equals(
					MARK_THREAD)/* && this.parent.parent.getChart() != null */) {
				ret += "  calls\n";
				ret += "    Mycalls: {\n";
				ret += "    P_Spg : subprogram programs_simulink::" + this.getName() + ";\n";
				ret += "    };\n";
				aadlFile.programsSimulink += "  subprogram " + this.getName() + "\n";
				aadlFile.programsSimulink += "  end " + this.getName() + ";\n\n";
			}
			// não imprime a implementação se for um device ou uma thread
			if (!(this.mark.equals(MARK_DEVICE) || this.mark.equals(MARK_THREAD))) {
				// Se for um process/thread cria uma thread.
				if (this.mark.equals(MARK_PROCESS_THREAD)) {
					this.addSubSystem(new SubSystem(this.getName(), aadlFile, this));
					this.getLastSubSystem().setMark(MARK_THREAD);
					this.getLastSubSystem().inPorts = this.inPorts;
					this.getLastSubSystem().outPorts = this.outPorts;
					for (int i = 0; i < this.getLastSubSystem().getInPorts().size(); i++) {
						if (this.getLastSubSystem().name
								.equals(this.getLastSubSystem().getInPorts().get(i).getName())) {
							addLine(this.getLastSubSystem().getInPorts().get(i).getName(), "0",
									this.getLastSubSystem().getName(), "" + (i + 1));
						} else {
							addLine(this.getLastSubSystem().getInPorts().get(i).getName(), "1",
									this.getLastSubSystem().getName(), "" + (i + 1));
						}
					}
					for (int i = 0; i < this.getLastSubSystem().getOutPorts().size(); i++) {
						this.addLine(this.getLastSubSystem().getName(), "" + (i + 1),
								this.getLastSubSystem().getOutPorts().get(i).getName(), "1");
					}
				}
				// executa aqui se tiver subsystems internos....
				if (this.getSubSystemsCount() > 0) {
					ret += "  SUBCOMPONENTS\n";
					for (int i = 0; i < this.getSubSystemsCount(); i++) {
						// nao imprime o MODES no aadl
						if (this.getSubSystem(i).getMark().equals(MARK_MODES)) {
							continue;
						}
						ret += "    " + this.getSubSystem(i).getFullName() + ": " + this.getSubSystem(i).getMark() + " "
								+ this.getSubSystem(i).getFullName() + ".impl";
						// imprime os modes nos subcomponents
						boolean close = false;
						if (this.getChart() != null && this.getBehavior() == null) {
							boolean needSlash = false;
							for (int j = 0; j < this.getChart().getModes().size(); j++) {
								for (int k = 0; k < this.getChart().getModes().get(j).getComponents().size(); k++) {
									// System.out.println(this.getChart().getModes().get(j).getComponents().get(k)
									// + " - " +
									// this.getSubSystem(i).getName());
									if (this.getChart().getModes().get(j).getComponents().get(k)
											.equals(this.getSubSystem(i).getName())) {
										close = true;
										if (needSlash) {
											ret += ", " + this.getChart().getModes().get(j).getFullName();
										} else {
											ret += " in modes(" + this.getChart().getModes().get(j).getFullName();
											needSlash = true;
										}
									}
								}
							}
							if (close)
								ret += ")";
						}
						ret += ";\n";
					}
					if (this.getAllLines().size() > 0)
						ret += "  CONNECTIONS\n";
//					System.out.println("Connections " + this.getFullName());
					int c = 1;
					for (int i = 0; i < this.getAllLines().size(); i++) {
//						System.out.println("C" + c);
						// variaveis auxiliares para
						boolean canWrite = true;
						String connection = "";
//						System.out.println(this.getAllLines().get(i).getSrcSystem() + " - "
//								+ this.getAllLines().get(i).getSrcPort() + " - "
//								+ this.getAllLines().get(i).getDestSystem() + " - "
//								+ this.getAllLines().get(i).getDestPort());
						Port outPort = this.findOutPort(this.getAllLines().get(i).getSrcSystem(),
								this.getAllLines().get(i).getSrcPort());
						if (this.getSubSystemMark(this.getAllLines().get(i).getSrcSystem()).equals(MARK_MODES)) {
							continue;
						}
						if (outPort != null) {
							connection += "    C" + c + ": PORT "
									+ this.getFullName(this.getAllLines().get(i).getSrcSystem()) + "."
									+ outPort.getName() + " -> ";
						} else {
							// retira as portas inexistentes como o bus creator
							if (this.findInPort(this.getAllLines().get(i).getSrcSystem()) != null) {
								if (this.getAllLines().get(i).getSrcPort().equals("0")) {
									connection += "    C" + c + ": PORT " + this.getAllLines().get(i).getSrcSystem()
											+ " -> ";
								} else {
									connection += "    C" + c + ": PORT "
											+ this.getFullName(this.getAllLines().get(i).getSrcSystem()) + " -> ";
								}
							} else {
								canWrite = false;
							}
						}
						Port inPort = this.findInPort(this.getAllLines().get(i).getDestSystem(),
								this.getAllLines().get(i).getDestPort());
						if (inPort != null) {
							connection += this.getFullName(this.getAllLines().get(i).getDestSystem()) + "."
									+ inPort.getName() + ";\n";
						} else {
//							System.out.println(this.getAllLines().get(i).getDestSystem());
							if (this.findOutPort(this.getAllLines().get(i).getDestSystem()) != null) {
								connection += this.getFullName(this.getAllLines().get(i).getDestSystem()) + ";\n";
							} else {
								canWrite = false;
							}
						}
						// caso não achou erro nas marcas de input e output
						if (canWrite) {
							ret += connection;
						}
						
//						if(inPort != null)
//							System.out.println("InPort " + inPort.getName());
//						else
//							System.out.println("InPort " + inPort);
//						if(outPort != null)
//							System.out.println("OutPort " + outPort.getName());
//						else
//							System.out.println("OutPort " + outPort);
							
//						System.out.println(connection);
						c++;
					}
					if (!this.getMark().equals(MARK_DEVICE)) {
						// imprime se o chart não for nulo e tiver modes válidos
						if (this.mark.equals(SubSystem.MARK_SYSTEM) && this.getChart() != null
								&& this.getChart().getModes().size() > 0) {
							// System.out.println(this.getBehavior() + " - " +
							// this.getChart() + " - " + this.mark);
							ret += "  MODES\n";
							for (int j = 0; j < this.getChart().getModes().size(); j++) {
								ret += "    " + this.getChart().getModes().get(j).getFullName() + ": "
										+ (this.getChart().getModes().get(j).isInitial() ? "initial " : "") + "mode;\n";
							}
							ret += "\n";
							// cria a condição inicial
							for (int j = 0; j < this.getChart().getModes().size(); j++) {
								if (this.getChart().getModes().get(j).isInitial()) {
									for (int i = 0; i < this.getSubSystemsCount(); i++) {
										Port temp = this.getSubSystem(i)
												.findInPort(this.getChart().getTransitions().get(j).getLabel());
										if (temp != null) {
											ret += "    " + this.getChart().getModes().get(j).getFullName() + " -["
													+ this.getSubSystem(i).getFullName() + "."
													+ this.getChart().getModes().get(j).getInitialMode() + "]-> "
													+ this.getChart().getModes().get(j).getFullName() + ";\n";
										}
									}
								}
							}
						}
						if (this.mark.equals(SubSystem.MARK_SYSTEM) && this.getChart() != null) {
							// se for device não aparece no aadl
							for (int j = 0; j < this.getChart().getTransitions().size(); j++) {
								if (this.getChart().getTransitions().get(j).getOrigin() != null
										&& this.getChart().getTransitions().get(j).getDestiny() != null) {
									// Se houver um inport com o mesmo nome da
									// transição
									Port temp = findInPort(this.getChart().getTransitions().get(j).getLabel());
									if (temp != null) {
										ret += "    "
												+ this.getChart().getTransitions().get(j).getOrigin().getFullName()
												+ " -";
										ret += "[" + this.getChart().getTransitions().get(j).getLabel() + "]-> ";
										ret += this.getChart().getTransitions().get(j).getDestiny().getFullName()
												+ ";\n";
										continue;
									}
									temp = null;
									// Se houver um inport em um irmão do
									// stateflow
									for (int i = 0; i < this.getSubSystemsCount(); i++) {
										temp = this.getSubSystem(i)
												.findInPort(this.getChart().getTransitions().get(j).getLabel());
										if (temp != null) {
											ret += "    "
													+ this.getChart().getTransitions().get(j).getOrigin().getFullName()
													+ " -";
											ret += "[" + this.getSubSystem(i).getFullName() + "."
													+ this.getChart().getTransitions().get(j).getLabel() + "]-> ";
											ret += this.getChart().getTransitions().get(j).getDestiny().getFullName()
													+ ";\n";
											continue;
										}
									}
									temp = null;
									// Se houver um outport em um irmão do
									// stateflow
									for (int i = 0; i < this.getSubSystemsCount(); i++) {
										temp = this.getSubSystem(i)
												.findOutPort(this.getChart().getTransitions().get(j).getLabel());
										if (temp != null) {
											ret += "    "
													+ this.getChart().getTransitions().get(j).getOrigin().getFullName()
													+ " -";
											ret += "[" + this.getSubSystem(i).getFullName() + "."
													+ this.getChart().getTransitions().get(j).getLabel() + "]-> ";
											ret += this.getChart().getTransitions().get(j).getDestiny().getFullName()
													+ ";\n";
											continue;
										}
									}
								}
							}
						}
					}
				}
				// lista os dataSubcomponents
				if (this.dataSubcomponents.size() > 0) {
					ret += "  SUBCOMPONENTS\n";
					for (int i = 0; i < this.dataSubcomponents.size(); i++) {
						ret += "    dt_" + this.dataSubcomponents.get(i) + ": data Base_Types_Simulink::integer;\n";
					}
				}
				if (this.mark.equals(MARK_THREAD_BEHAVIOR)) {
					if (this.getBehavior() != null) {
						ret += "  annex behavior_specification {**\n";
						ret += "    STATES\n";
						for (int i = 0; i < this.getBehavior().getModes().size(); i++) {
							ret += "      " + this.getBehavior().getModes().get(i).getFullName() + ": "
									+ (this.getBehavior().getModes().get(i).isInitial() ? "initial " : "")
									+ (this.getBehavior().getModes().get(i).isComplete() ? "complete " : "")
									+ "state;\n";
						}
						if (this.getBehavior().getTransitions().size() > 0) {
							ret += "    TRANSITIONS\n";
							for (int i = 0; i < this.getBehavior().getTransitions().size(); i++) {
								if (this.getBehavior().getTransitions().get(i).getOrigin() != null) {
									ret += "      "
											+ this.getBehavior().getTransitions().get(i).getOrigin().getFullName()
											+ " -";
									ret += "[" + this.getBehavior().getTransitions().get(i).getLabel() + "]-> ";
									ret += this.getBehavior().getTransitions().get(i).getDestiny().getFullName();
									if (!this.getBehavior()
											.getMode(this.getBehavior().getTransitions().get(i).getDestiny().getId())
											.getAnnotation().equals("")) {
										ret += " " + this.getBehavior()
												.getMode(
														this.getBehavior().getTransitions().get(i).getDestiny().getId())
												.getAnnotation();
									}
									ret += ";\n";
								}
							}
						}
						ret += "  **};\n";
					}
				}
			}
			ret += "END " + this.getFullName() + ".impl;\n\n";
			// se for um device não lista no mapeamento
			if (!this.mark.equals(MARK_DEVICE)) {
				for (int i = 0; i < this.getAllSubSystem().size(); i++) {
					ret += this.getAllSubSystem().get(i).toString();
				}
			}
		}
		return ret;
	}

	public String searchOutPort(String name) {
		for (int i = 0; i < outPorts.size(); i++)
			if (outPorts.get(i).getName().equals(name))
				return outPorts.get(i).getNumber();

		return "-1";
	}

	public String searchInPort(String name) {
		for (int i = 0; i < inPorts.size(); i++)
			if (inPorts.get(i).getName().equals(name))
				return inPorts.get(i).getNumber();

		return "-1";
	}
}
