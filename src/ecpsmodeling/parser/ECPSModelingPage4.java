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

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;

public class ECPSModelingPage4 extends WizardPage {
	private Composite container;

	List list;

	protected Label information;

	public ECPSModelingPage4() {
		super("Actuation Specification");
		setTitle("Actuation Specification");
		setDescription("Detail the Actuation subsystem:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		GridData gridData = new GridData();
		
		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);
		
		information = new Label(container, SWT.NONE);
		information.setText("Analyze the output ports of the mathematical model:");
		
		list = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		gridData.widthHint = 300;
		gridData.heightHint = 200;
		list.setLayoutData(gridData);
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

	public void populateOutputList(SubSystem subsystem) {
		//System.out.println("Quantidade: " + subsystem.getOutPortsCount());
		for (int i = 0; i < subsystem.getOutPortsCount(); i++) {
			//System.out.println("Porta: " + subsystem.getOutPort(i).getName());
			list.add(subsystem.getOutPort(i).getName());
		}
	}
}
