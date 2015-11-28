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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ECPSModelingPage3 extends WizardPage {
	private Composite container;
		
	public static String MARK_PROCESS_THREAD = "PROCESS/THREAD";
	public static String MARK_PROCESS = "PROCESS";
	public static String MARK_SYSTEM = "SYSTEM";
		
	List list;
	
	public ECPSModelingPage3() {
	    super("Sensing and Actuation Modeling");
	    setTitle("Sensing and Actuation Modeling");
	    setDescription("Define the System Mathematical Model:");
	  }

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		container.setSize(300, 200);
		//Display display = new Display();
		//Shell shell = new Shell(display);
	    //shell.setText("List Example");
	    //shell.setSize(300, 200);
	    //shell.setLayout(new FillLayout(SWT.VERTICAL));
	    list = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    list.setSize(300, 200);
	    list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageComplete(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});	    
		setControl(container);
		setPageComplete(false);
	}
	
	
	
	public void populateList(Mdl2Aadl mdl2Aadl){
		for (int i = 0; i < mdl2Aadl.aadl.getSubSystem().getSubSystemsCount(); i++) {
			if(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_PROCESS_THREAD)){
				list.add(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
			if(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_PROCESS)){
				list.add(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
			if(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_SYSTEM)){			
				list.add(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
		}
	}
}
