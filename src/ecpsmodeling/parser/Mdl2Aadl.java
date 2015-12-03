package ecpsmodeling.parser;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class Mdl2Aadl {
	private ParentArrayList lastOpen;
	private ParentArrayList mdlFile;

	private BufferedReader bfReader;

	// objeto aadl
	AadlFile aadl;

	int level = 0;

	// Construtor
	public Mdl2Aadl(String file) throws Exception {
		super();
		mdlFile = new ParentArrayList();
		aadl = new AadlFile();
		lastOpen = mdlFile;
		try {
			// Criação de um buffer de leitura para o arquivo MDL
			bfReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw e;
		}
		try {
			parse();
		} catch (Exception e) {
			throw e;
		}
		try {
			transformEngine();
		} catch (Exception e) {
			throw e;
		}
	}

	private void parse() throws Exception {
		// Função que faz a leitura do arquivo mdl para mapeamento dos seus
		// componentes
		String line;
		List<String> controlList = new ArrayList<String>();
		int ignore = 0;
		try {
			while (bfReader.ready()) {
				line = bfReader.readLine();
				line = line.trim();
				if (line.length() == 0)
					continue;
				if (line.charAt(0) == '#')
					continue;
				if (line.charAt(line.length() - 1) == '{') {
					// le a nova linha removendo o { final
					line = line.substring(0, line.length() - 2);
					controlList.add(new String(line));
					if (ignore != 0)
						continue;
					// Faz a validação se a linha verificada possui alguma das
					// tags validas
					if (!validate(line)) {
						ignore = controlList.size();
						continue;
					}
					// FG: NÂO CONSEGUI IDENTIFICAR OQ ESTAS FUNÇÔES FAZEM
					// VERIFICAR
					lastOpen.add(new ParentArrayList(lastOpen, line));
					lastOpen = (ParentArrayList) lastOpen.get(lastOpen.size() - 1);
					continue;
				}
				// Verifica se chegou ao final do objeto
				if (line.charAt(line.length() - 1) == '}') {
					controlList.remove(controlList.size() - 1);
					if ((ignore != 0) && (ignore > controlList.size())) {
						ignore = 0;
						continue;
					}
					if (ignore != 0)
						continue;
					if (lastOpen.parent != null)
						lastOpen = lastOpen.parent;
					continue;
				}
				if (ignore != 0) {
					continue;
				}
				int lastPropPosition = -1;
				int firstValuePosition = -1;
				// FG: NÂO CONSEGUI IDENTIFICAR OQ ESTE LAÇO DE REPETIÇÂO
				// REALIZA VERIFICAR
				for (int i = 0; i < line.length(); i++) {
					if (lastPropPosition == -1 && (int) line.charAt(i) <= 32)
						lastPropPosition = i;
					if (lastPropPosition != -1 && (int) line.charAt(i) > 32) {
						firstValuePosition = i;
						break;
					}
				}
				// FG: NÂO CONSEGUI OQ ESTA VERIFICAÇÃO REALIZA
				String element;
				if (lastPropPosition != -1) {
					element = line.substring(0, lastPropPosition);
				} else {
					continue;
				}
				if (!validate(element)) {
					continue;
				}
				String value = line.substring(firstValuePosition, line.length());
				lastOpen.add(new ParentArrayList(lastOpen, element, value));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void autoMark() {
		// Primeiro Nível
		for (int i = 0; i < aadl.getSubSystem().getSubSystemsCount(); i++) {
			SubSystem firstLevel = aadl.getSubSystem().getSubSystem(i);
			// Não tenta Marcar se uma marca já estiver colocada
			if (!firstLevel.getMark().isEmpty()) {
				continue;
			}
			// System.out.println(firstLevel.getFullName());
			// Segundo Nível
			for (int j = 0; j < firstLevel.getSubSystemsCount(); j++) {
				SubSystem secondLevel = firstLevel.getSubSystem(j);
				// Não tenta Marcar se uma marca já estiver colocada
				if (!secondLevel.getMark().isEmpty()) {
					continue;
				}
				// System.out.println(" " + secondLevel.getFullName());
				// Terceiro Nível
				for (int k = 0; k < secondLevel.getSubSystemsCount(); k++) {
					SubSystem thirdLevel = secondLevel.getSubSystem(k);
					// Não tenta Marcar se uma marca já estiver colocada
					if (!thirdLevel.getMark().isEmpty()) {
						continue;
					}
					// Blocos subsystem do tipo Stateflow devem receber a marca
					// thread/behavior
					if (thirdLevel.getParent().getChart() != null
							&& thirdLevel.getParent().getChart().getChartOwner().equals(thirdLevel.getName())) {
						thirdLevel.setMark(SubSystem.MARK_THREAD_BEHAVIOR);
						continue;
					}
					// Blocos subsystem do tipo Simple devem receber a marca
					// thread
					if (thirdLevel.getSubSystemsCount() == 0) {
						thirdLevel.setMark(SubSystem.MARK_THREAD);
						continue;
					} else {
						// blocos subsystem do tipo composite devem recebera
						// marca thread
						for (int l = 0; l < thirdLevel.getSubSystemsCount(); l++) {
							if (thirdLevel.getSubSystem(l).getMark().isEmpty()) {
								thirdLevel.setMark(SubSystem.MARK_THREAD);
								continue;
							}
						}
					}
				} // Fim do terceiro nível
					// Mapeamentos do Segundo Nível
					// bloco subsystem do tipo Stateflow recebe a marca modes
				if (secondLevel.getParent().getChart() != null
						&& secondLevel.getParent().getChart().getChartOwner().equals(secondLevel.getName())) {
					secondLevel.setMark(SubSystem.MARK_MODES);
					continue;
				}
				// Os blocos subsystem do tipo Simple devem receber a marca
				// "process/thread"
				if (secondLevel.getSubSystemsCount() == 0) {
					secondLevel.setMark(SubSystem.MARK_PROCESS_THREAD);
					continue;
				} else {
					// Os blocos subsystem do tipo Composite devem receber a
					// marca "process"
					for (int k = 0; k < secondLevel.getSubSystemsCount(); k++) {
						if (secondLevel.getSubSystem(k).getMark().equals(SubSystem.MARK_MODES)
								|| secondLevel.getSubSystem(k).getMark().equals(SubSystem.MARK_PROCESS)
								|| secondLevel.getSubSystem(k).getMark().equals(SubSystem.MARK_SYSTEM)
								|| secondLevel.getSubSystem(k).getMark().equals(SubSystem.MARK_THREAD)) {
							secondLevel.setMark(SubSystem.MARK_PROCESS);
							continue;
						}
					}
				}
			} // Fim do segundo nível
				// Mapeamentos do Primeiro Nível
				// bloco subsystem do tipo Stateflow recebe a marca modes
			if (firstLevel.getChart() != null && firstLevel.getChart().getChartOwner().equals(firstLevel.getName())) {
				firstLevel.setMark(SubSystem.MARK_MODES);
				continue;
			}
			// Os blocos subsystem do tipo Simple devem receber a marca
			// "process/thread"
			if (firstLevel.getSubSystemsCount() == 0) {
				firstLevel.setMark(SubSystem.MARK_PROCESS_THREAD);
				continue;
			} else {
				// Os blocos subsystem do tipo Composite devem receber a marca
				// "process"
				for (int j = 0; j < firstLevel.getSubSystemsCount(); j++) {
					if (firstLevel.getSubSystem(j).getMark().equals(SubSystem.MARK_MODES)
							|| firstLevel.getSubSystem(j).getMark().equals(SubSystem.MARK_PROCESS)
							|| firstLevel.getSubSystem(j).getMark().equals(SubSystem.MARK_SYSTEM)
							|| firstLevel.getSubSystem(j).getMark().equals(SubSystem.MARK_THREAD)) {
						firstLevel.setMark(SubSystem.MARK_SYSTEM);
						continue;
					}
				}
			}
		}
	}

	private boolean validate(String line) {
		String validTags[] = { "Block", "BlockType", "Model", "Name", "System", "Line", "Port",
				"AttributesFormatString", "SignalName", "IconDisplay", "SrcBlock", "SrcPort", "DstBlock", "DstPort",
				"Branch", "OutDataTypeStr", "Stateflow", "state", "chart", "name", "labelString", "id", "transition",
				"src", "dst", "firstTransition", "firstEvent", "SFBlockType" };
		for (int i = 0; i < validTags.length; i++) {
			if (line.equals(validTags[i])) {
				return true;
			}
		}
		return false;
	}

	private void createSubSystems(ParentArrayList subsystems, SubSystem parent) {
		/*
		 * for (int i = 0; i < subsystems.size(); i++) {
		 * System.out.println(subsystems.get(i).parent.name + " - " +
		 * subsystems.get(i).parent.value); for (int j = 0; j <
		 * subsystems.get(i).parent.size(); j++) { System.out.println("  " +
		 * subsystems.get(i).parent.get(j).value); } System.out.println("  ---"
		 * ); } System.out.println("---");
		 */
		//System.out.println(subsystems.size());
		for (int i = 0; i < subsystems.size(); i++) {
			// se for uma constante não lista no aadl
			if (subsystems.get(i).parent.getByName("Name").get(0).value.contains("Constant")) {
				continue;
			}
			// System.out.println(subsystems.get(i).parent.getByName("Name").get(0).value);
			// adiciona o subsystem
			parent.addSubSystem(new SubSystem(subsystems.get(i).parent.getByName("Name").get(0).value, aadl, parent));
			// se tem o AttributesFormatString seta o subsystem com o tipo
			if (subsystems.get(i).parent.getByName("AttributesFormatString", ParentArrayList.NO_CHILD).size() > 0) {
				// System.out.println(subsystems.get(i).parent.getByName("AttributesFormatString").get(0).value);
				parent.getLastSubSystem()
						.setMark(subsystems.get(i).parent.getByName("AttributesFormatString").get(0).value);
			} else {
				if (subsystems.get(i).parent.getByName("Name").get(0).value.contains("device"))
					parent.getLastSubSystem().setMark("device");
				else if (subsystems.get(i).parent.getByName("Name").get(0).value.contains("Constant"))
					parent.getLastSubSystem().setMark("device");
				else if (subsystems.get(i).parent.getByName("Name").get(0).value.contains("ManualSwitch"))
					parent.getLastSubSystem().setMark("ManualSwitch");
			}
			// remove os blocos MATLAB Function
			if (subsystems.get(i).parent.get(1).value.equals("\"UAV\"")) {
				for (int j = 0; j < subsystems.get(i).parent.size(); j++) {
					// System.out.println(" " +
					// subsystems.get(i).parent.get(j).name + " - " +
					// subsystems.get(i).parent.get(j).value);
				}
				// System.out.println(subsystems.get(i).parent.get(1).value);
			}
			if (subsystems.get(i).parent.getByName("SFBlockType", ParentArrayList.NO_CHILD).size() > 0) {
//				System.out.println(subsystems.get(i).parent.get(1).value);
				/*
				 * for (int j = 0; j < subsystems.get(i).parent.size(); j++) {
				 * System.out.println(subsystems.get(i).parent.get(j).name +
				 * " - " + subsystems.get(i).parent.get(j).value); }
				 * System.out.println("---");
				 */
			}
			if (parent.getLastSubSystem().getMark().isEmpty()
					&& subsystems.get(i).parent.getByName("SFBlockType", ParentArrayList.NO_CHILD).size() > 0
					&& subsystems.get(i).parent.getByName("SFBlockType", ParentArrayList.NO_CHILD).get(0).value
							.equals("\"MATLAB Function\"")) {
				// System.out.println(parent.getFullName() + " - " +
				// parent.getLastSubSystem().getFullName() + " - " +
				// subsystems.get(i).parent.getByName("name").get(0).value);
				parent.removeLastSubsystem();
				continue;
			}
			// procura todas as outports
			ParentArrayList outPorts = subsystems.get(i).parent.getByName("BlockType",
					new ArrayList<String>(Arrays.asList("Outport")), ParentArrayList.FIRST_CHILD);
			for (int j = 0; j < outPorts.size(); j++) {
				// necessário caso porta não tenha tipo de dado
				String dataType;
				if (outPorts.get(j).parent.getByName("OutDataTypeStr").size() > 0) {
					dataType = outPorts.get(j).parent.getByName("OutDataTypeStr").get(0).value;
				} else {
					dataType = "int32";
				}
				if (outPorts.get(j).parent.getByName("Port").size() > 0) {
					parent.getLastSubSystem().addOutPort(outPorts.get(j).parent.parent.getByName("Name").get(0).value,
							outPorts.get(j).parent.getByName("Name").get(0).value,
							outPorts.get(j).parent.getByName("Port").get(0).value,
							outPorts.get(j).parent.getByName("IconDisplay").get(0).value, dataType,
							parent.getLastSubSystem());
				} else {
					parent.getLastSubSystem().addOutPort(outPorts.get(j).parent.parent.getByName("Name").get(0).value,
							outPorts.get(j).parent.getByName("Name").get(0).value,
							outPorts.get(j).parent.getByName("IconDisplay").get(0).value, dataType,
							parent.getLastSubSystem());
				}
			}
			// procura todas as inports
			ParentArrayList inPorts = subsystems.get(i).parent.getByName("BlockType",
					new ArrayList<String>(Arrays.asList("Inport")), ParentArrayList.FIRST_CHILD);
			for (int j = 0; j < inPorts.size(); j++) {
				// necessário caso porta não tenha tipo de dado
				String dataType;
				if (inPorts.get(j).parent.getByName("OutDataTypeStr").size() > 0) {
					dataType = inPorts.get(j).parent.getByName("OutDataTypeStr").get(0).value;
				} else {
					dataType = "int32";
				}
				if (inPorts.get(j).parent.getByName("Port").size() > 0) {
					parent.getLastSubSystem().addInPort(inPorts.get(j).parent.parent.getByName("Name").get(0).value,
							inPorts.get(j).parent.getByName("Name").get(0).value,
							inPorts.get(j).parent.getByName("Port").get(0).value,
							inPorts.get(j).parent.getByName("IconDisplay").get(0).value, dataType,
							parent.getLastSubSystem());
				} else {
					parent.getLastSubSystem().addInPort(inPorts.get(j).parent.parent.getByName("Name").get(0).value,
							inPorts.get(j).parent.getByName("Name").get(0).value,
							inPorts.get(j).parent.getByName("IconDisplay").get(0).value, dataType,
							parent.getLastSubSystem());
				}
			}
			// procura as lines
			ParentArrayList lines = subsystems.get(i).parent.getByName("Line", ParentArrayList.NO_CHILD);
			for (int j = 0; j < lines.size(); j++) {
				if (lines.get(j).getByName("Branch", ParentArrayList.DIRECT).size() > 0) {
					for (int k = 0; k < lines.get(j).getByName("Branch", ParentArrayList.DIRECT).size(); k++) {
						ParentArrayList branch = lines.get(j).getByName("Branch", ParentArrayList.DIRECT).get(k);
						if (branch.getByName("Branch", ParentArrayList.DIRECT).size() > 0) {
							for (int l = 0; l < branch.getByName("Branch", ParentArrayList.DIRECT).size(); l++) {
								parent.getLastSubSystem().addLine(lines.get(j).getByName("SrcBlock").get(0).value,
										lines.get(j).getByName("SrcPort").get(0).value,
										branch.getByName("Branch", ParentArrayList.DIRECT).get(l).getByName("DstBlock")
												.get(0).value,
										branch.getByName("Branch", ParentArrayList.DIRECT).get(l).getByName("DstPort")
												.get(0).value);
							}
						} else {
							parent.getLastSubSystem().addLine(lines.get(j).getByName("SrcBlock").get(0).value,
									lines.get(j).getByName("SrcPort").get(0).value,
									branch.getByName("DstBlock").get(0).value,
									branch.getByName("DstPort").get(0).value);
						}
					}
				} else {
					parent.getLastSubSystem().addLine(lines.get(j).getByName("SrcBlock").get(0).value,
							lines.get(j).getByName("SrcPort").get(0).value,
							lines.get(j).getByName("DstBlock").get(0).value,
							lines.get(j).getByName("DstPort").get(0).value);
				}
			}
			ParentArrayList internals = subsystems.get(i).parent.getByName("BlockType",
					new ArrayList<String>(Arrays.asList("SubSystem")), ParentArrayList.FIRST_CHILD);
			if (internals.size() > 1) {
				// necessarios para remover o primeiro, que é o próprio
				// subsystem e não seus filhos
				internals.remove(0);
				createSubSystems(internals, parent.getLastSubSystem());
			}
		}
	}

	private void operationModes() {
		// Procura os modos de operacao, soh executando daqui pra frente se
		// houver um stateflow no arquivo
		if (mdlFile.getByName("Stateflow").size() > 0) {
			// procura todos os charts
			ParentArrayList chart = mdlFile.getByName("chart", ParentArrayList.NO_CHILD);
			for (int i = 0; i < chart.size(); i++) {
				// encontra os SubSystems
				SubSystem subsystemsWithChart = aadl.searchSubSystem(chart.get(i).getByName("name").get(0).value,
						false);
				if (subsystemsWithChart != null) {
					// adciona o chart ao subsystem
					SubSystem chartOwner = aadl.searchSubSystem(chart.get(i).getByName("name").get(0).value, true);
					subsystemsWithChart.setChart(chart.get(i).getByName("name").get(0).value,
							(chartOwner != null ? chartOwner.getName() : null),
							chart.get(i).getByName("id").get(0).value);
					// procura todos os states
					ParentArrayList modes = mdlFile.getByName("state", ParentArrayList.NO_CHILD);
					for (int j = 0; j < modes.size(); j++) {
						// verifica se o id do modo eh igual ao do chart
						if (modes.get(j).getByName("chart").get(0).value
								.equals(subsystemsWithChart.getChart().getId())) {
							int start = 1;
							int stop = start;
							for (int k = start; k < modes.get(j).getByName("labelString").get(0).value.length(); k++) {
								if (modes.get(j).getByName("labelString").get(0).value.charAt(k) == ';'
										|| modes.get(j).getByName("labelString").get(0).value.charAt(k) == '\\'
										|| modes.get(j).getByName("labelString").get(0).value.charAt(k) == '"') {
									stop = k;
									break;
								}
							}
							// remove os blocos matlab
							subsystemsWithChart.getChart().addMode(modes.get(j).getByName("labelString").get(0).value
									.substring(start, stop).toLowerCase(), modes.get(j).getByName("id").get(0).value);
							// System.out.println(" " +
							// subsystemsWithChart.getFullName() + " - " +
							// subsystemsWithChart.getChart().getLastMode().getFullName());
							if (modes.get(j).getByName("labelString").get(0).value.indexOf("ndu:") != -1) {
								String label = modes.get(j).getByName("labelString").get(0).value;
								do {
									start = label.indexOf("ndu:") + 4;
									stop = start;
									for (int k = start; k < label.length(); k++) {
										if (label.charAt(k) == ';' || label.charAt(k) == '\\'
												|| label.charAt(k) == '"') {
											stop = k;
											break;
										}
									}
									subsystemsWithChart.getChart().getLastMode()
											.addComponent(label.substring(start, stop).trim().toLowerCase());
									label = label.substring(stop, label.length());
								} while (label.indexOf("ndu:") != -1);
							}
						}
					}
					ParentArrayList transition = mdlFile.getByName("transition", ParentArrayList.NO_CHILD);
					for (int j = 0; j < transition.size(); j++) {
						if (transition.get(j).getByName("id").get(0).value
								.equals(chart.getByName("firstTransition").get(0).value)) {
							Mode temp = subsystemsWithChart.getChart()
									.getMode(transition.get(j).getByName("dst").get(0).getByName("id").get(0).value);
							if (temp != null) {
								temp.setInitial(true);
								temp.setInitialMode(transition.get(j).getByName("labelString").get(0).value);
							}
						}
						if (transition.get(j).getByName("src").get(0).getByName("id").size() > 0) {
							// substitui o | por ,
							transition.get(j).getByName("labelString")
									.get(0).value = transition.get(j).getByName("labelString").get(0).value.replace("|",
											",");
							subsystemsWithChart.getChart().addTransition(
									transition.get(j).getByName("src").get(0).getByName("id").get(0).value,
									transition.get(j).getByName("dst").get(0).getByName("id").get(0).value,
									transition.get(j).getByName("labelString").get(0).value);
						}
					}
				}
				// encontra as threads para o modo comportamental
				SubSystem thread = aadl.searchSubSystem(chart.get(i).getByName("name").get(0).value, true);
				if (thread != null) {
					if (thread.getMark().equals(SubSystem.MARK_THREAD)) {
						thread.setBehavior(chart.get(i).getByName("name").get(0).value,
								chart.get(i).getByName("id").get(0).value);
						ParentArrayList states = mdlFile.getByName("state", ParentArrayList.NO_CHILD);
						for (int j = 0; j < states.size(); j++) {
							if (states.get(j).getByName("chart").get(0).value.equals(thread.getBehavior().getId())) {
								int start = 1;
								int stop = start;
								for (int k = start; k < states.get(j).getByName("labelString").get(0).value
										.length(); k++) {
									if (states.get(j).getByName("labelString").get(0).value.charAt(k) == ';'
											|| states.get(j).getByName("labelString").get(0).value.charAt(k) == '\\'
											|| states.get(j).getByName("labelString").get(0).value.charAt(k) == '"') {
										stop = k;
										break;
									}
								}
								thread.getBehavior()
										.addMode(
												states.get(j).getByName("labelString").get(0).value
														.substring(start, stop).toLowerCase(),
										states.get(j).getByName("id").get(0).value);
								// coloca uma anotação no mode se ele tiver a
								// linha 'en:'
								if (states.get(j).getByName("labelString").get(0).value.contains("en:")) {
									start = states.get(j).getByName("labelString").get(0).value.indexOf("en:") + 3;
									stop = states.get(j).getByName("labelString").get(0).value.length() - 2;
									thread.getBehavior().getLastMode()
											.setAnnotation(" {" + states.get(j).getByName("labelString").get(0).value
													.substring(start, stop).trim().toLowerCase() + "!}");
								}
							}
						}
						ParentArrayList transition = mdlFile.getByName("transition", ParentArrayList.NO_CHILD);
						for (int j = 0; j < transition.size(); j++) {
							// só executa se a transiction é do chart
							if (!transition.get(j).getByName("chart").get(0).value
									.equals(thread.getBehavior().getId())) {
								continue;
							}
							// se o ID da transição foi igual ao firstTransition
							// do chart seta ela como a inicial
							if (transition.get(j).getByName("id").get(0).value
									.equals(chart.get(i).getByName("firstTransition").get(0).value)) {
								Mode temp = thread.getBehavior().getMode(
										transition.get(j).getByName("dst").get(0).getByName("id").get(0).value);
								if (temp != null) {
									temp.setInitial(true);
								}
							}
							// adiciona as transitions que não são iniciais
							if (transition.get(j).getByName("src").get(0).getByName("id").size() > 0) {
								// os caracteres de operações onde um estado
								// intermediario será necessário
								String[] characters = { " ", "<", ">", "+", "-", "/", "*", "=" };
								String labelString = transition.get(j).getByName("labelString").get(0).value;
								String destiny = transition.get(j).getByName("dst").get(0).getByName("id").get(0).value;
								String label = labelString;
								String threadName = "";
								boolean needIntermediate = false;
								for (int k = 0; k < characters.length; k++) {
									if (labelString.contains(characters[k])) {
										label = labelString.substring(labelString.lastIndexOf("[") + 1,
												labelString.indexOf(characters[k]));
										threadName = labelString.substring(0, labelString.lastIndexOf("["));
										String newMode = "intermediate_" + thread.getBehavior().getMode(
												transition.get(j).getByName("dst").get(0).getByName("id").get(0).value)
												.getName();
										// adiciona os estados intermediarios
										// com o ID 10000 + j
										if (thread.getBehavior().addMode(newMode, "" + (10000 + j))) {
											// System.out.println("-" +
											// newMode);
											thread.getBehavior().getLastMode().setComplete(false);
											destiny = thread.getBehavior().getLastMode().getId();
										} else {
											ArrayList<Mode> temp = thread.getBehavior().getModes();
											for (int l = 0; l < temp.size(); l++) {
												if (temp.get(l).getName().equals(newMode)) {
													destiny = temp.get(l).getId();
													break;
												}
											}
										}
										thread.addDataSubcomponents(label);
										thread.getBehavior().getLastMode()
												.setAnnotation("{" + thread.getLastDataSubcomponent() + "?(dt_"
														+ thread.getLastDataSubcomponent() + ")}");
										label = "dt_" + thread.getLastDataSubcomponent() + labelString.substring(
												labelString.indexOf(characters[k]), labelString.length() - 2);
										needIntermediate = true;
										break;
									}
								}
								// adiciona a transição do intermediario para o
								// destino verdadeiro
								label = label.replace("==", "=");
								if (!needIntermediate) {
									thread.getBehavior().addTransition(
											transition.get(j).getByName("src").get(0).getByName("id").get(0).value,
											destiny, "on dispatch " + label);
								} else {
									thread.getBehavior().addTransition(
											transition.get(j).getByName("src").get(0).getByName("id").get(0).value,
											destiny, "on dispatch " + threadName);
									thread.getBehavior().addTransition(destiny,
											transition.get(j).getByName("dst").get(0).getByName("id").get(0).value,
											label);
								}
							}
						}
					}
				}
			}
		}
	}

	private void transformEngine() {
		// o nome do pacote
		aadl.setPackageName(mdlFile.get(0).get(0).value);
		// cria o system raiz
		aadl.getSubSystem().setName(mdlFile.get(0).get(1).get(0).value);
		// cria as conecções da raiz
		ParentArrayList lines = mdlFile.getByName("Line");
		for (int j = 0; j < lines.size(); j++) {
			if (lines.get(j).getByName("Branch").size() > 0) {
				for (int k = 0; k < lines.get(j).getByName("Branch").size(); k++) {
					aadl.getSubSystem().addLine(lines.get(j).getByName("SrcBlock").get(0).value,
							lines.get(j).getByName("SrcPort").get(0).value,
							lines.get(j).getByName("Branch").get(k).getByName("DstBlock").get(0).value,
							lines.get(j).getByName("Branch").get(k).getByName("DstPort").get(0).value);
				}
			} else {
				aadl.getSubSystem().addLine(lines.get(j).getByName("SrcBlock").get(0).value,
						lines.get(j).getByName("SrcPort").get(0).value, lines.get(j).getByName("DstBlock").get(0).value,
						lines.get(j).getByName("DstPort").get(0).value);
			}
		}
		// cria os subsystems da raiz
		createSubSystems(
				mdlFile.getByName("BlockType",
						new ArrayList<String>(Arrays.asList(
								"SubSystem"/* , "Constant", "ManualSwitch" */)),
						ParentArrayList.FIRST_CHILD),
				aadl.getSubSystem());
		operationModes();
	}

	public void save(String filePath, String fileName) {
		try {
			BufferedWriter aadlFile = new BufferedWriter(new FileWriter(filePath + fileName));
			aadlFile.write(aadl.toString());
			aadlFile.close();
			BufferedWriter baseTypesSimulink = new BufferedWriter(
					new FileWriter(filePath + "Base_Types_Simulink.aadl"));
			baseTypesSimulink.write(AadlFile.BASETYPESSIMULINK);
			baseTypesSimulink.close();
			aadl.programsSimulink += "end programs_simulink;";
			BufferedWriter programsSimulink = new BufferedWriter(new FileWriter(filePath + "programs_simulink.aadl"));
			programsSimulink.write(aadl.programsSimulink);
			programsSimulink.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void mdlToMdl(String filePath, boolean clean) {
		String line;
		List<String> elements = new ArrayList<String>();
		String file = "";
		// int linha = 0;
		try {
			BufferedReader buffIn = new BufferedReader(new FileReader(filePath));
			while (buffIn.ready()) {
				// linha++;
				line = buffIn.readLine();
				line = line.trim();

				// rack da linha em branco
				if (line.length() == 0)
					continue;

				// Retira comentarios do arquivo
				line = line.trim();
				if (line.charAt(0) == '#') {
					continue;
				}
				if (line.charAt(line.length() - 1) == '{') {
					line = line.substring(0, line.length());
					for (int i = 0; i < elements.size(); i++) {
						file = file + "  ";
					}
					file = file + line + "\n";
					elements.add(new String(line));
					continue;
				}
				if (line.charAt(line.length() - 1) == '}') {
					elements.remove(elements.size() - 1);
					for (int i = 0; i < elements.size(); i++) {
						file = file + "  ";
					}
					file = file + "}\n";
					continue;
				}
				int lastPropPosition = -1;
				int firstValuePosition = -1;
				for (int i = 0; i < line.length(); i++) {
					if (lastPropPosition == -1 && (int) line.charAt(i) <= 32) {
						lastPropPosition = i;
					}
					if (lastPropPosition != -1 && (int) line.charAt(i) > 32) {
						firstValuePosition = i;
						break;
					}
				}
				String element = line.substring(0, lastPropPosition);
				for (int i = 0; i < elements.size(); i++) {
					if (validate(element))
						file = file + "  ";
				}
				// bug das chaves nos valores
				String value = line.substring(firstValuePosition, line.length());
				if (validate(element))
					file = file + element + "	" + value + "\n";
			}
			buffIn.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		try {
			BufferedWriter buffOut = new BufferedWriter(new FileWriter(filePath + ".bkp"));
			buffOut.write(file);
			buffOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
