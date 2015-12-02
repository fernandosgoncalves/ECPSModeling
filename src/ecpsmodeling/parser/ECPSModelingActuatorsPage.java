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
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.swt.SWT;

public class ECPSModelingActuatorsPage extends WizardPage {
	private Composite container;

	protected Table table;

	protected Label information;

	public ECPSModelingActuatorsPage() {
		super("Actuator Specification");
		setTitle("Actuator Specification");
		setDescription("Detail the Actuation subsystem:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();		
		layout.numColumns = 3;
		
		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 300);
		
		information = new Label(container, SWT.NONE);
		GridData ilayout = new GridData();
		ilayout.horizontalSpan = 3;
		information.setLayoutData(ilayout);
		information.setText("Specify the system actuator that compose the application:");
		
		// ---------------------- Table ---------------------------
		table = new Table(container, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 3;

		table.setLayoutData(data);
		table.setSize(300, 100);

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Signal");
		column.setWidth(150);

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Actuator");
		column2.setWidth(110);

		final TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText("Sampling");
		column3.setWidth(70);

		final TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setText("Protocol");
		column4.setWidth(70);

		final TableColumn column5 = new TableColumn(table, SWT.NONE);
		column5.setText("Priority");
		column5.setWidth(70);
		
		Button btAddActuator = new Button(container, SWT.NONE);
		Button btEditActuator = new Button(container, SWT.NONE);
		Button btRemoveActuator = new Button(container, SWT.NONE);
		
		table.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btAddActuator.setEnabled(true);
				btEditActuator.setEnabled(true);
				btRemoveActuator.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//Button btAddActuator = new Button(container, SWT.NONE);
		btAddActuator.setText("Add Actuator");
		btAddActuator.setEnabled(false);
		//Button btEditActuator = new Button(container, SWT.NONE);
		btEditActuator.setText("Edit Actuator");
		btEditActuator.setEnabled(false);
		btEditActuator.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch(e.type){
				case SWT.Selection:
					EditActuatorShell edit = new EditActuatorShell(table.getItem(table.getSelectionIndex()),parent.getDisplay());
					//Display display = new Display();
				    /*Shell dialog = new Shell(shell);
				    dialog.setText("Dialog");
				    dialog.setSize(200, 200);
				    dialog.open();*/
				}
			}
		});
		
		//Button btRemoveActuator = new Button(container, SWT.NONE);
		btRemoveActuator.setText("Remove Actuator");
		btRemoveActuator.setEnabled(false);
				
		setControl(container);
		setPageComplete(true);
	}

	/*
	 * Verify the input table and according the vector amount of each input port
	 * the signals are inserted into the sensors list specification
	 */
	public void populateSensorsTable(Table table2) {
		TableItem item;
		// System.out.println("Begin Function");
		// System.out.println(table2.getItemCount());
		for (int i = 0; i < table2.getItemCount(); i++) {
			Label port = (Label) table2.getItem(i).getData("port");
			Text size = (Text) table2.getItem(i).getData("size");
			// System.out.println(size.getText());
			for (int z = 0; z < Integer.valueOf(size.getText()); z++) {
				// System.out.println(port.getText() + (z + 1));
				item = new TableItem(table, SWT.NONE);
				item.setText(0, port.getText() + (z + 1));
			}
		}
	}
}
