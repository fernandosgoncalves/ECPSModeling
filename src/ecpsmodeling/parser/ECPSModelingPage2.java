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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ECPSModelingPage2 extends WizardPage {
	private Composite container;
	
	private Text text1;
		
	public ECPSModelingPage2() {
	    super("Sensing and Actuation Modeling");
	    setTitle("Sensing and Actuation Modeling");
	    setDescription("Define the System Mathematical Model:");
	  }

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		//Display display = new Display();
		//Shell shell = new Shell(display);
	    //shell.setText("List Example");
	    //shell.setSize(300, 200);
	    //shell.setLayout(new FillLayout(SWT.VERTICAL));
	    final List list = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    	
	    
	    
	    for (int loopIndex = 0; loopIndex < 10; loopIndex++) {
	      list.add("Item " + loopIndex);
	    }
	    
		//Label label1 = new Label(container, SWT.NONE);
		//label1.setText("Put a value here.");

		//text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
		//text1.setText("");
		/*text1.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!text1.getText().isEmpty()) {
					setPageComplete(true);

				}
			}

		});*/
		
		//GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		//text1.setLayoutData(gd);
		// required to avoid an error in the system
				
		setControl(container);
		setPageComplete(false);

	}

	public String getText1() {
		return text1.getText();
	}
}
