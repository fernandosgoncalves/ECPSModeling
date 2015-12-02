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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ECPSModeling extends Wizard implements IImportWizard {

	public static String MAIN_PAGE = "ECPSModeling Import File";
	public static String PAGE2 = "Sensing and Actuation Modeling";
	public static String PAGE3 = "Sensing Analyze";
	public static String PAGE4 = "Sensors Specification";
	public static String PAGE5 = "Actuation Analyze";

	ECPSModelingPage mainPage;
	ECPSModelingSubsysPage page2;
	ECPSModelingInputsPage page3;
	ECPSModelingSensorsPage page4;
	ECPSModelingOutputsPage page5;

	Mdl2Aadl mdl2Aadl;

	protected boolean performedSensorsPage = false;
	protected boolean performedOutputsPage = false;
	protected boolean performedInputsPage = false;
	protected boolean performedSubsysPage = false;
	protected boolean performedMainPage = false;

	public ECPSModeling() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 * Overide the function getNextPage in order to provide the code execution when the next button is presssed
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// System.out.println("AQUI");
		if (performedMainPage == false && page.isPageComplete()){
			//System.out.println("PAGE1");
			performMainPage();
		}
		else {
			if (page.isPageComplete() && page.getName().equals(PAGE2) && performedInputsPage == false){
				//System.out.println("PAGE2");
				performInputsPage();
			}
			else {
				if (page.isPageComplete() && page.getName().equals(PAGE3) && performedOutputsPage == false){
					//System.out.println("PAGE3");
					performOutputsPage();
				}else{
					if (page.isPageComplete() && page.getName().equals(PAGE4) && performedSensorsPage == false){
						System.out.println("PAGE4");
						performSensorsPage();
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
	 * Selected the mathematical model sybsystem, this function read their input
	 * ports and populate a list to analyze it.
	 */
	public void performInputsPage() {
		SubSystem subsys;
		//System.out.println("INIT PERFORM PAGE2");
		try {
			subsys = mdl2Aadl.aadl.getSubSystem();
			//First Level
			if (subsys.getName().equals(page2.table.getItem(page2.table.getSelectionIndex()).getText(0))){
				//System.out.println("GET IN");
				page3.populateInputList(subsys);
			}
			else{
				if(subsys.getSubSystemsCount() > 0){
					//Recursive search in the subsystems of the main system  
					page3.populateInputList(subsys.searchSubSystem(page2.table.getItem(page2.table.getSelectionIndex()).getText(0)));
				}
			}
			performedInputsPage = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Perform the instruction to populate the table with the input ports of the selected subsystem 
	 */
	public void performOutputsPage() {
		SubSystem subsys;
		try {
			subsys = mdl2Aadl.aadl.getSubSystem();
			//First Level
			if (subsys.getName().equals(page2.table.getItem(page2.table.getSelectionIndex()).getText(0))){				
				//System.out.println("GET IN");
				page5.populateOutputList(subsys);
			}
			else{
				if(subsys.getSubSystemsCount() > 0){
					//Recursive search in the subsystems of the main system  
					page5.populateOutputList(subsys.searchSubSystem(page2.table.getItem(page2.table.getSelectionIndex()).getText(0)));
				}
			}
			performedOutputsPage = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void performSensorsPage(){
		System.out.println("Inside Function");
		page4.populateSensorsTable(page3.table);
		performedSensorsPage = true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 * Function performed when the finish button is pressed
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
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		page2 = new ECPSModelingSubsysPage();
		page3 = new ECPSModelingInputsPage();
		page4 = new ECPSModelingSensorsPage();
		page5 = new ECPSModelingOutputsPage();
		addPage(mainPage);
		addPage(page2);
		addPage(page3);
		addPage(page4);
		addPage(page5);
		super.addPages();
	}
}
