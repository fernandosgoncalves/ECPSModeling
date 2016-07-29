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

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ECPSModeling extends Wizard implements IImportWizard {

	// Variables that represent the names of system pages
	protected static final String ACTUATIORNTHREADS = "Actuation Threads Specification";
	protected static final String ACTSPECIFICATION = "Actuator Specification";
	protected static final String SENSPECIFICATION = "Sensor Specification";
	protected static final String SENSINGTHREADS = "Sensing Threads Specification";
	protected static final String SIMULINKMODEL = "Simulink Model";
	protected static final String POSTREADING = "Post-reading Specification";
	protected static final String PREWRITING = "Pre-processing Specification";
	protected static final String SAMODELING = "Sensing and Actuation Modeling";
	protected static final String ACTUATION = "Actuation Analyze";
	protected static final String MAIN_PAGE = "ECPSModeling Import File";
	protected static final String AADLMODEL = "AADL Model";
	protected static final String SENSING = "Sensing Analyze";
	protected static final String MAIN = "ECPSModeling Import File";

	// Objects of the system pages
	ActuationThreadsPage actuatorsThreadsPage;
	SensingThreadsPage sensingThreadsPage;
	PostReadingPage postReadingPage;
	PreProcessingPage prewritingPage;
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
		/*
		 * Override the function getNextPage in order to provide the wizard
		 * manipulation between their pages
		 */
		if (page.isPageComplete()) {
			switch (page.getName()) {
			case MAIN:
				performMainPage();
				break;
			case SAMODELING:
				/*
				 * Selected the mathematical model sybsystem, this function read
				 * their input ports and populate a list to analyze it.
				 */
				// if (subsysPage.table.getSelectionIndex() > 0)
				// inputsPage.populateInputList(mdl2Aadl.aadl.getSubSystem().searchSubSystem(
				// subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0)));
				break;
			case ACTUATION:
				/*
				 * Perform the instructions to populate the table with the
				 * output ports of the selected subsystem
				 */
				prewritingPage.populateSignals(inputsPage.getTable());
				break;
			case PREWRITING:
				/*
				 * Perform the instructions to populate the table with the list
				 * of system actuator
				 */
				actuatorsPage.populateActuatorsTable(inputsPage.getTable(), prewritingPage.getActFunctions());
				break;
			case ACTSPECIFICATION:
				if (subsysPage.getOutputModel().equals(AADLMODEL))
					/*
					 * This function reads the specified actuators and populate
					 * the list with a periodic and sporadic actuators
					 */
					actuatorsThreadsPage.populateThreadsTable(actuatorsPage.getActuators(),
							prewritingPage.getActFunctions());
				else
					/*
					 * If the output model is the Simulink model, the threads
					 * specification it is not performed
					 */
					actuatorsThreadsPage.nextStep();
				break;
			case ACTUATIORNTHREADS:
				/*
				 * This function reads the output ports and populate the list to
				 * analyze this information
				 */
				outputsPage.populateOutputList(mdl2Aadl.aadl.getSubSystem()
						.searchSubSystem(subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0)));
				break;
			case SENSING:
				/*
				 * Perform the instruction to populate the table with the input
				 * ports of the selected subsystem
				 */
				postReadingPage.populateSignals(outputsPage.getTable());
				break;
			case POSTREADING:
				/*
				 * Perform the instructions to populate the table with the list
				 * of system sensors
				 */
				sensorsPage.populateSensorsTable(outputsPage.getTable(), postReadingPage.getSenFunctions());
				break;
			case SENSPECIFICATION:
				if (subsysPage.getOutputModel().equals(AADLMODEL))
					/*
					 * This function reads the specified actuators and functions
					 * and populate the list of periodic and sporadic sensors
					 * and functions
					 */
					sensingThreadsPage.populateThreadsTable(sensorsPage.getSensors(),
							postReadingPage.getSenFunctions());
				else
					/*
					 * If the output model is the Simulink model, the threads
					 * specification it is not performed
					 */
					sensingThreadsPage.nextStep();
				break;
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
	 * Search recursively and remove the selected device block
	 */
	private void removeSelectedDevice(ArrayList<SubSystem> subsystems, String selected) {
		int t = 0;
		while (t < subsystems.size()) {
			if (subsystems.get(t).getName().equals(selected))
				subsystems.remove(t);
			else {
				if (subsystems.get(t).getAllSubSystem().size() > 0)
					removeSelectedDevice(subsystems.get(t).getAllSubSystem(), selected);
				t++;
			}
		}
	}

	/*
	 * Search recursively and remove the connected lines to the selected device
	 * block
	 */
	private void removeDeviceLines(SubSystem subsystem, String selected) {
		int w = 0;
		while (w < subsystem.getAllLines().size()) {
			if (subsystem.getAllLines().get(w).getSrcSystem().equals(selected)
					|| subsystem.getAllLines().get(w).getDestSystem().equals(selected))
				subsystem.getAllLines().remove(w);
			else
				w++;
		}
		if(subsystem.getAllSubSystem().size() > 0){
			int t = 0;
			while (t < subsystem.getAllSubSystem().size()){
				removeDeviceLines(subsystem.getSubSystem(t),selected);
				t++;
			}
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
			/*
			 * Remove the selected subsystem that will be replaced by the
			 * sensing and actuation subsystems
			 */
			removeSelectedDevice(mdl2Aadl.aadl.getSubSystem().getAllSubSystem(),
					subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0));

			/*
			 * Remove all the lines that are connected to the selected subsystem
			 */
			removeDeviceLines(mdl2Aadl.aadl.getSubSystem(),
					subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0));

//			int w = 0;
//			while (w < mdl2Aadl.aadl.getSubSystem().getAllLines().size()) {
//				if (mdl2Aadl.aadl.getSubSystem().getAllLines().get(w).getSrcSystem()
//						.equals(subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0))
//						|| mdl2Aadl.aadl.getSubSystem().getAllLines().get(w).getDestSystem()
//								.equals(subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0)))
//					mdl2Aadl.aadl.getSubSystem().getAllLines().remove(w);
//				else
//					w++;
//
//			}

			/*
			 * Function that generate the system sensors and actuators on the
			 * AADL Model
			 */
			// mdl2Aadl.sensingActuationTransformation(prewritingPage.getActFunctions(),
			// actuatorsPage.getActuators(),
			// postReadingPage.getSenFunctions(), sensorsPage.getSensors(),
			// mdl2Aadl.aadl.getSubSystem().searchSubSystem(
			// subsysPage.table.getItem(subsysPage.table.getSelectionIndex()).getText(0)));
			// mdl2Aadl.aadl.getSubSystem()

			/* Generation of the AADL file */
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
		/* Adtition of the process pages in the process wizard */
		actuatorsThreadsPage = new ActuationThreadsPage();
		sensingThreadsPage = new SensingThreadsPage();
		postReadingPage = new PostReadingPage();
		prewritingPage = new PreProcessingPage();
		actuatorsPage = new ActuatorsPage();
		outputsPage = new OutputsPage();
		sensorsPage = new SensorsPage();
		subsysPage = new SubsysPage();
		inputsPage = new InputsPage();

		addPage(mainPage);
		addPage(subsysPage);
		// addPage(inputsPage);
		// addPage(prewritingPage);
		// addPage(actuatorsPage);
		// addPage(actuatorsThreadsPage);
		// addPage(outputsPage);
		// addPage(postReadingPage);
		// addPage(sensorsPage);
		// addPage(sensingThreadsPage);
		super.addPages();
	}
}
