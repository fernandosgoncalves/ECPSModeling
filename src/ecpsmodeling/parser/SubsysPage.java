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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;

public class SubsysPage extends WizardPage {
	private Composite container;

	public static String MARK_PROCESS_THREAD = "PROCESS/THREAD";
	public static String MARK_PROCESS = "PROCESS";
	public static String MARK_SYSTEM = "SYSTEM";
	public static String MARK_DEVICE = "DEVICE";

	protected Table table;
	
	Label information;

	public SubsysPage() {
		super("Sensing and Actuation Modeling");
		setTitle("Sensing and Actuation Modeling");
		setDescription("Define the System Mathematical Model:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();

		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		information = new Label(container, SWT.NONE);
		information.setText("Define the subsystem that represent the mathematical model:");

		// ---------------------- Table ---------------------------
		table = new Table(container, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);

		table.setLayoutData(data);

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Subsystems");
		column.setWidth(200);

		table.addSelectionListener(new SelectionListener() {
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
		for (int i = 0; i < mdl2Aadl.aadl.getSubSystem().getSubSystemsCount(); i++) {
			if (mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_PROCESS_THREAD)) {
				addItemTable(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
			if (mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_PROCESS)) {
				addItemTable(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
			if (mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_DEVICE)) {
				addItemTable(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
			}
			if (mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getMark().equals(MARK_SYSTEM)) {
				addItemTable(mdl2Aadl.aadl.getSubSystem().getSubSystem(i).getName());
				exploreSystem(mdl2Aadl.aadl.getSubSystem().getSubSystem(i));
			}
		}
	}

	public void addItemTable(String name){
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(name);
	}
	
	public void exploreSystem(SubSystem subsystem) {
		for (int i = 0; i < subsystem.getSubSystemsCount(); i++) {
			if (subsystem.getSubSystem(i).getMark().equals(MARK_PROCESS_THREAD)) {
				addItemTable(subsystem.getSubSystem(i).getName());
			}
			if (subsystem.getSubSystem(i).getMark().equals(MARK_PROCESS)) {
				addItemTable(subsystem.getSubSystem(i).getName());
			}
			if (subsystem.getSubSystem(i).getMark().equals(MARK_DEVICE)) {
				addItemTable(subsystem.getSubSystem(i).getName());
			}			
			if (subsystem.getSubSystem(i).getMark().equals(MARK_SYSTEM)) {
				addItemTable(subsystem.getSubSystem(i).getName());
				exploreSystem(subsystem.getSubSystem(i));
			}
		}
	}
}
