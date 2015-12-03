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
	public static String MAIN_PAGE = "ECPSModeling Import File";
	public static String PAGE2 = "Sensing and Actuation Modeling";
	public static String PAGE3 = "Actuation Analyze";
	public static String PAGE4 = "Actuator Specification";
	public static String PAGE5 = "Sensor Analyze";

	// Objects of the system pages
	ECPSModelingPage mainPage;
	ECPSModelingSubsysPage page2;
	ECPSModelingInputsPage page3;
	ECPSModelingActuatorsPage page4;
	ECPSModelingOutputsPage page5;
	ECPSModelingSensorsPage page6;

	Mdl2Aadl mdl2Aadl;

	// Validation of the population lists are performed
	protected boolean performedActuatorsPage = false;
	protected boolean performedOutputsPage = false;
	protected boolean performedSensorsPage = false;
	protected boolean performedInputsPage = false;
	protected boolean performedSubsysPage = false;
	protected boolean performedMainPage = false;

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
		// System.out.println("AQUI");
		if (performedMainPage == false && page.isPageComplete()) {
			// System.out.println("PAGE1");
			performMainPage();
		} else {
			if (page.isPageComplete() && page.getName().equals(PAGE2) && performedInputsPage == false) {
				/*
				 * Selected the mathematical model sybsystem, this function read
				 * their input ports and populate a list to analyze it.
				 */
				page3.populateInputList(mdl2Aadl.aadl.getSubSystem()
						.searchSubSystem(page2.table.getItem(page2.table.getSelectionIndex()).getText(0)));
				performedInputsPage = true;
			} else {
				if (page.isPageComplete() && page.getName().equals(PAGE3) && performedOutputsPage == false) {
					/*
					 * Perform the instruction to populate the table with the
					 * output ports of the selected subsystem
					 */
					page5.populateOutputList(mdl2Aadl.aadl.getSubSystem()
							.searchSubSystem(page2.table.getItem(page2.table.getSelectionIndex()).getText(0)));
					performedOutputsPage = true;
				} else {
					if (page.isPageComplete() && page.getName().equals(PAGE4) && performedActuatorsPage == false) {
						/*
						 * Perform the instructions to populate the table witj
						 * the list of system actuator
						 */
						page4.populateSensorsTable(page3.getTable());
						performedActuatorsPage = true;
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
			page2.populateList(mdl2Aadl);
			performedMainPage = true;
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
		System.out.println("FINISH PRESSED");
		IFile file = mainPage.createNewFile();
		try {
			// Chamada da função de marcação automatizada
			mdl2Aadl.autoMark();
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
		mainPage = new ECPSModelingPage("ECPSModeling Import File", selection); // NON-NLS-1
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages() Adding the wizard
	 * process pages
	 */
	public void addPages() {
		page2 = new ECPSModelingSubsysPage();
		page3 = new ECPSModelingInputsPage();
		page4 = new ECPSModelingActuatorsPage();
		page5 = new ECPSModelingOutputsPage();
		page6 = new ECPSModelingSensorsPage();
		addPage(mainPage);
		addPage(page2);
		addPage(page3);
		addPage(page4);
		addPage(page5);
		addPage(page6);
		super.addPages();
	}
}
