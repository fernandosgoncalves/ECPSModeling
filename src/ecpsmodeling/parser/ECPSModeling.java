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

	public static String MAIN_PAGE = "ECPSModeling Import File";
	public static String PAGE2 = "Sensing and Actuation Modeling";
	public static String PAGE3 = "Sensing Specification";
	public static String PAGE4 = "Actuation Specification";

	ECPSModelingPage mainPage;
	ECPSModelingPage2 page2;
	ECPSModelingPage3 page3;
	ECPSModelingPage4 page4;

	Mdl2Aadl mdl2Aadl;

	protected boolean performedPage3 = false;
	protected boolean performedPage2 = false;
	protected boolean performed = false;

	public ECPSModeling() {
		super();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// System.out.println("AQUI");
		if (performed == false && page.isPageComplete()){
			//System.out.println("PAGE1");
			performMainPage();
		}
		else {
			if (page.isPageComplete() && page.getName().equals(PAGE2) && performedPage2 == false){
				//System.out.println("PAGE2");
				performPage2();
			}
			else {
				if (page.isPageComplete() && page.getName().equals(PAGE3) && performedPage3 == false){
					//System.out.println("PAGE3");
					performPage3();
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
			performed = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Selected the mathematical model sybsystem, this function read their input
	 * ports and populate a list to analyze it.
	 */
	public void performPage2() {
		SubSystem subsys;
		SubSystem auxsubsys;
		//System.out.println("INIT PERFORM PAGE2");
		try {
			//System.out.println(page2.list.getItem(page2.list.getSelectionIndex()).toString());
			subsys = mdl2Aadl.aadl.getSubSystem();
			//System.out.println(subsys.getName());
			//First Level
			if (subsys.getName().equals(page2.list.getItem(page2.list.getSelectionIndex()).toString())){
				//System.out.println("GET IN");
				page3.populateInputList(subsys);
			}
			else{
				if(subsys.getSubSystemsCount() > 0)
					searchInNextLevel(subsys);
			}
			performedPage2 = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void performPage3() {
		SubSystem subsys;
		SubSystem auxsubsys;
		//System.out.println("INIT PERFORM PAGE3");
		try {
			//System.out.println(page2.list.getItem(page2.list.getSelectionIndex()).toString());
			subsys = mdl2Aadl.aadl.getSubSystem();
			//System.out.println(subsys.getName());
			//First Level
			if (subsys.getName().equals(page2.list.getItem(page2.list.getSelectionIndex()).toString())){
				//System.out.println("GET IN");
				page4.populateOutputList(subsys);
			}
			else{
				if(subsys.getSubSystemsCount() > 0)
					searchOutNextLevel(subsys);
			}
			performedPage3 = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchInNextLevel(SubSystem subsystem){
		SubSystem auxSubsystem;
		//Second Level Level
		//System.out.println("SEARCH NEXT LEVEL");
		for (int i = 0; i < subsystem.getSubSystemsCount(); i++) {
			auxSubsystem = subsystem.getSubSystem(i);
			//System.out.println(auxSubsystem.getName());
			if (auxSubsystem.getName().equals(page2.list.getItem(page2.list.getSelectionIndex()).toString())){
				//System.out.println("GET IN");
				page3.populateInputList(auxSubsystem);
			}else{
				if(auxSubsystem.getSubSystemsCount() > 0){
					searchInNextLevel(auxSubsystem);
				}
			}
		}
	}
	
	public void searchOutNextLevel(SubSystem subsystem){
		SubSystem auxSubsystem;
		//Second Level Level
		//System.out.println("SEARCH NEXT LEVEL");
		for (int i = 0; i < subsystem.getSubSystemsCount(); i++) {
			auxSubsystem = subsystem.getSubSystem(i);
			//System.out.println(auxSubsystem.getName());
			if (auxSubsystem.getName().equals(page2.list.getItem(page2.list.getSelectionIndex()).toString())){
				//System.out.println("GET IN");
				page4.populateOutputList(auxSubsystem);
			}else{
				if(auxSubsystem.getSubSystemsCount() > 0){
					searchOutNextLevel(auxSubsystem);
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		System.out.println("FINISH PRESSED");
		IFile file = mainPage.createNewFile();
		try {
			// Construtor criação do processo de transformação e leitura do
			// arquivo mdl
			// Mdl2Aadl mdl2Aadl = new
			// Mdl2Aadl(file.getRawLocation().toString());
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
		page2 = new ECPSModelingPage2();
		page3 = new ECPSModelingPage3();
		page4 = new ECPSModelingPage4();
		addPage(mainPage);
		addPage(page2);
		addPage(page3);
		addPage(page4);
		super.addPages();
	}
}
