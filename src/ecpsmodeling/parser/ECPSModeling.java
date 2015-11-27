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
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ECPSModeling extends Wizard implements IImportWizard {

	ECPSModelingPage mainPage;
	ECPSModelingPage2 page2;
	
	Mdl2Aadl mdl2Aadl;

	public ECPSModeling() {
		super();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		System.out.println("NEXT PRESSED");
		//IFile file = mainPage.createNewFile();
		try {
			// Construtor criação do processo de transformação e leitura do
			// arquivo mdl
//			System.out.println(mainPage.getFileName());
//			System.out.println(mainPage.editor.getStringValue());
//			mdl2Aadl = new Mdl2Aadl(file.getRawLocation().toString());
			mdl2Aadl = new Mdl2Aadl(mainPage.editor.getStringValue());
			// Chamada da função de marcação automatizada
			//mdl2Aadl.autoMark();
			// Geração do arquivo AADL de saída
			/*mdl2Aadl.save(file.getRawLocation().removeLastSegments(1) + "/",
					file.getRawLocation().lastSegment().substring(0, file.getRawLocation().lastSegment().length() - 4)
							+ ".aadl");
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
        return super.getNextPage(page);
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
			//Mdl2Aadl mdl2Aadl = new Mdl2Aadl(file.getRawLocation().toString());
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
		addPage(mainPage);
		addPage(page2);
		super.addPages();
	}

}
