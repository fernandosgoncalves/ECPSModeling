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
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import java.util.ArrayList;
import org.eclipse.swt.SWT;

public class ActuatorsPage extends WizardPage {
	protected ArrayList<Actuator> actuators;	
	
	private Composite container;
	
	protected Label information;

	protected Table table;
	
	public ActuatorsPage() {
		super("Actuator Specification");
		setTitle("Actuator Specification");
		setDescription("Detail the system Actuators:");
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
		table.setSize(290, 100);

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Signal");
		column.setWidth(130);

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Actuator");
		column2.setWidth(100);

		final TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText("Protocol");
		column3.setWidth(70);

		final TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setText("Priority");
		column4.setWidth(70);

		final TableColumn column5 = new TableColumn(table, SWT.NONE);
		column5.setText("Periodic");
		column5.setWidth(70);

		final TableColumn column6 = new TableColumn(table, SWT.NONE);
		column6.setText("Period (ms)");
		column6.setWidth(70);
		
		Button btEditActuator = new Button(container, SWT.NONE);
		
		table.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btEditActuator.setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editActuatorProperties(parent.getDisplay());
			}
		});

		btEditActuator.setText("Edit Actuator");
		btEditActuator.setEnabled(false);
		btEditActuator.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					if(table.getSelectionIndex() > -1)
						editActuatorProperties(parent.getDisplay());
				}
			}
		});

		actuators = new ArrayList<>();
		
		setControl(container);
		setPageComplete(false);
	}

	//Edit the properties of the specified actuator
	public void editActuatorProperties(Display display) {
		ActuatorsPropertiesShell edit = new ActuatorsPropertiesShell(table.getItem(table.getSelectionIndex()), display);
		
		if (edit.isConfirm()) {
			Actuator auxActuator = actuators.get(table.getSelectionIndex());
			
			TableItem aux = table.getItem(table.getSelectionIndex());
			aux.setText(1, edit.getActuator());
			aux.setText(2, edit.getProtocol());
			aux.setText(3, edit.getPriority());
			Button bt = (Button) aux.getData("periodic");
			bt.setSelection(edit.getPeriodic());
			aux.setText(5, edit.getPeriod());
			
			auxActuator.setName(edit.getActuator());
			auxActuator.setProtocol(edit.getProtocol());
			auxActuator.setPriority(Integer.valueOf(edit.getPriority()));
			auxActuator.setPeriodic(edit.getPeriodic());
			auxActuator.setPeriod(Integer.valueOf(edit.getPeriod()));
			auxActuator.inputs.set(0, edit.getSignal());
			
			actuators.set(table.getSelectionIndex(), auxActuator);
			
			checkSpecified();
		}
	}

	/*
	 * Verify the input table and according the vector amount of each input port
	 * the signals are inserted into the sensors list specification
	 */
	public void populateActuatorsTable(Table inputTable, ArrayList<ActuationFunction> functions) {
		//If table have data clear it
		if(table.getItemCount() > 0)
			clearData();
		
		Actuator actuator;
		TableItem item;
		
		TableEditor teEditor;
		
		Button preWriting;
		//Button bperiodic;

		Combo cActuator;
		
		//Insert the inputs that not need of pre-writing
		for (int i = 0; i < inputTable.getItemCount(); i++) {
			preWriting = (Button) inputTable.getItem(i).getData("PreWcheck");
			cActuator = (Combo) inputTable.getItem(i).getData("actuator");
			if (!preWriting.getSelection()) {
				Label port = (Label) inputTable.getItem(i).getData("port");
				Text size = (Text) inputTable.getItem(i).getData("size");
				for (int z = 0; z < Integer.valueOf(size.getText()); z++) {
					item = new TableItem(table, SWT.NONE);
					item.setText(0, port.getText() + (z + 1));
				
					if(!cActuator.getText().isEmpty())
						item.setText(1, cActuator.getText());
					//insert item components
					teEditor = new TableEditor(table);
					Button bperiodic = new Button(table, SWT.CHECK);
					bperiodic.setEnabled(false);
					bperiodic.pack();
					teEditor.minimumWidth = bperiodic.getSize().x;
					teEditor.horizontalAlignment = SWT.LEFT;
					teEditor.setEditor(bperiodic, item, 4);
					item.setData("periodic", bperiodic);
				}
			}
		}

		//insert the inputs that are specified in the pre-writing process
		for (int i = 0; i < functions.size(); i++) {
			for (int z = 0; z < functions.get(i).getOutputs().size(); z++) {
				item = new TableItem(table, SWT.NONE);
				item.setText(0, functions.get(i).getOutputs().get(z));
				
				teEditor = new TableEditor(table);
				Button bperiodic = new Button(table, SWT.CHECK);
				bperiodic.setEnabled(false);
				bperiodic.pack();
				teEditor.minimumWidth = bperiodic.getSize().x;
				teEditor.horizontalAlignment = SWT.LEFT;
				teEditor.setEditor(bperiodic, item, 4);
				item.setData("periodic", bperiodic);
			}
		}
		
		//populate Array of actuators
		for(int i = 0; i < table.getItemCount(); i++){
			actuator = new Actuator();
			actuator.setIndex(i);
			actuator.inputs.add(table.getItem(i).getText(0));
			if(table.getItem(i).getText(1).isEmpty())
				actuator.setName(table.getItem(i).getText(1));
			
			actuators.add(actuator);
		}
		
		checkSpecified();
	}

	//clear all data of actuators
	private void clearData(){
		for(int i = 0; i < table.getItemCount(); i++){
			Button check = (Button) table.getItem(i).getData("periodic");
			check.dispose();
		}
		table.removeAll();
		actuators.clear();
	}
	
	//Verify that the all conditions are satisfied to enable the next button 	
	public void checkSpecified() {
		Boolean check = true;
		for (int i = 0; i < table.getItemCount(); i++) {
			for (int z = 0; z < table.getColumnCount(); z++) {
				if (table.getItem(i).getText(z).isEmpty() && z != 4)
					check = false;
			}
		}
		if (check)
			setPageComplete(true);
		else
			setPageComplete(false);
	}
	
	protected ArrayList<Actuator> getActuators(){
		//System.out.println(actuators.size());
		return actuators;
	}
}
