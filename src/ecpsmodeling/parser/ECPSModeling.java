/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package ecpsmodeling.parser;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ECPSModeling extends Wizard implements IImportWizard {

	// Variables that represent the names of system pages
	public static String SAMODELING = "Sensing and Actuation Modeling";
	public static String ACTSPECIFICATION = "Actuator Specification";
	public static String POSTREADING = "Post-reading Specification";
	public static String SENSPECIFICATION = "Sensor Specification";
	public static String PREWRITING = "Prewriting Specification";
	public static String MAIN_PAGE = "ECPSModeling Import File";
	public static String ACTUATION = "Actuation Analyze";
	public static String SENSING = "Sensing Analyze";
	public static String MAIN = "ECPSModeling Import File";

	// Objects of the system pages
	PostReadingPage postReadingPage;
	PreWritingPage prewritingPage;
	ActuatorsPage actuatorsPage;
	OutputsPage outputsPage;
	SensorsPage sensorsPage;
	SubsysPage subsysPage;
	InputsPage inputsPage;
	MainPage mainPage;

	Mdl2Aadl mdl2Aadl;

	public ECPSModeling() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.
	 * IWizardPage) Overide the function getNextPage in order to provide the
	 * code execution when the next button is presssed
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page.isPageComplete()) {
			if (page.getName().equals(MAIN)) {
				performMainPage();
			} else {
				if (page.getName().equals(SAMODELING)) {
					// Selected the mathematical model sybsystem, this function read their input ports and populate a list to analyze it.
					inputsPage.populateInputList(mdl2Aadl.aadl.getSubSystem().searchSubSystem(
							subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0)));
				} else {
					if (page.getName().equals(ACTUATION)) {
						// Perform the instruction to populate the table with the output ports of the selected subsystem 
						prewritingPage.populateSignals(inputsPage.getTable());
					} else {
						if (page.getName().equals(PREWRITING)) {
							// Perform the instructions to populate the table with the list of system actuator
							actuatorsPage.populateActuatorsTable(inputsPage.getTable(),
									prewritingPage.getActFunction());
						} else {
							if (page.getName().equals(ACTSPECIFICATION)) {
								// This function reads the output ports and populate the list to analyze this information
								outputsPage.populateOutputList(mdl2Aadl.aadl.getSubSystem().searchSubSystem(
										subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0)));
							} else {
								if (page.getName().equals(SENSING)) {
									// Perform the instruction to populate the table with the input ports of the selected subsystem									
									postReadingPage.populateSignals(outputsPage.getTable());
								} else {
									if (page.getName().equals(POSTREADING)) {
										// Perform the instructions to populate the table with the list of system sensors
										sensorsPage.populateSensorsTable(outputsPage.getTable(), postReadingPage.getSenSubsystems());
									}
								}
							}
						}
					}
				}
			}
		}

		return super.getNextPage(page);
	}

	/*
	 * Perform Actions that read the and mark the Simulink model, and populate
	 * the list with the systems and process, in order to identify the
	 * mathematical model subsystem
	 */
	private void performMainPage() {
			try {
				mdl2Aadl = new Mdl2Aadl(mainPage.editor.getStringValue());
				// Chamada da função de marcação automatizada
				mdl2Aadl.autoMark();
				// Carrega Lista
				subsysPage.populateList(mdl2Aadl);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish() Function performed
	 * when the finish button is pressed
	 */
	public boolean performFinish() {
		// System.out.println("FINISH PRESSED");
		IFile file = mainPage.createNewFile();
		try {
			//Construtor criação do processo de transformação e leitura do arquivo mdl
			//Mdl2Aadl mdl2Aadl = new Mdl2Aadl(file.getRawLocation().toString());
			// Chamada da função de marcação automatizada
			//mdl2Aadl.autoMark();
			// Chamada da função de transformação de sensores e atuadores
			mdl2Aadl.sensingActuationTransformation(prewritingPage.getActFunction(), actuatorsPage.getActuators(), postReadingPage.getSenSubsystems(), sensorsPage.getSensors(), mdl2Aadl.aadl.getSubSystem().searchSubSystem(
					subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0)));
			// Geração do arquivo AADL de saída
			mdl2Aadl.save(file.getRawLocation().removeLastSegments(1) + "/",
					file.getRawLocation().lastSegment().substring(0, file.getRawLocation().lastSegment().length() - 4)
							+ ".aadl");
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (file == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("File Import Wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new MainPage("ECPSModeling Import File", selection); // NON-NLS-1
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages() Adding the wizard
	 * process pages
	 */
	public void addPages() {
		postReadingPage = new PostReadingPage();
		prewritingPage = new PreWritingPage();
		actuatorsPage = new ActuatorsPage();
		outputsPage = new OutputsPage();
		sensorsPage = new SensorsPage();
		subsysPage = new SubsysPage();
		inputsPage = new InputsPage();

		addPage(mainPage);
		addPage(subsysPage);
		addPage(inputsPage);
		addPage(prewritingPage);
		addPage(actuatorsPage);
		addPage(outputsPage);
		addPage(postReadingPage);
		addPage(sensorsPage);
		super.addPages();
	}
}
