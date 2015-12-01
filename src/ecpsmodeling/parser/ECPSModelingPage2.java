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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;

public class ECPSModelingPage2 extends WizardPage {
	private Composite container;

	public static String MARK_PROCESS_THREAD = "PROCESS/THREAD";
	public static String MARK_PROCESS = "PROCESS";
	public static String MARK_SYSTEM = "SYSTEM";

	List list;

	Label information;

	public ECPSModelingPage2() {
		super("Sensing and Actuation Modeling");
		setTitle("Sensing and Actuation Modeling");
		setDescription("Define the System Mathematical Model:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		GridData gridData = new GridData();

		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		information = new Label(container, SWT.NONE);
		information.setText("Define the subsystem that represent the mathematical model:");

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
	
	public void populateList(Mdl2Aadl mdl2Aadl) {
		// System.out.println("Quantidade: " +
		// mdl2Aadl.aadl.getSubSystem().getSubSystemsCount());
		for (int i = 0; i < mdl2Aadl.aadl.getSubSystem().getSubSystemsCount(); i++) {
			// System.out.println("Marca: " +
			// mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark());
			// System.out.println("Nome: " +
			// mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			if (mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_PROCESS_THREAD)) {
				list.add(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
			if (mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_PROCESS)) {
				list.add(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
			if (mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_SYSTEM)) {
				list.add(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
				exploreSystem(mdl2Aadl.aadl.getSubSystem().getSubSystem(i));
			}
		}
	}

	public void exploreSystem(SubSystem subsystem) {
		// System.out.println("Quantidade: " + subsystem.getSubSystemsCount());
		for (int i = 0; i < subsystem.getSubSystemsCount(); i++) {
			// System.out.println("Marca: " +
			// subsystem.getSubSystem(i).getMark());
			// System.out.println("Nome: " +
			// subsystem.getSubSystem(i).getName());
			if (subsystem.getSubSystem(i).getMark().equals(MARK_PROCESS_THREAD)) {
				list.add(subsystem.getSubSystem(i).getName());
			}
			if (subsystem.getSubSystem(i).getMark().equals(MARK_PROCESS)) {
				list.add(subsystem.getSubSystem(i).getName());
			}
			if (subsystem.getSubSystem(i).getMark().equals(MARK_SYSTEM)) {
				list.add(subsystem.getSubSystem(i).getName());
				exploreSystem(subsystem.getSubSystem(i));
			}
		}
	}
}
