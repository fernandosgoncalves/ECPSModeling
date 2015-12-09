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
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
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
import org.eclipse.swt.custom.TableEditor;

public class SensorsPage extends WizardPage {
	protected ArrayList<Sensor> sensors;

	private Composite container;

	protected Label information;

	protected Table table;

	public SensorsPage() {
		super("Sensor Specification");
		setTitle("Sensor Specification");
		setDescription("Detail the Sensing Subsystem:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		information = new Label(container, SWT.NONE);
		GridData ilayout = new GridData();
		ilayout.horizontalSpan = 3;
		information.setLayoutData(ilayout);
		information.setText("Specify the set of sensors that compose the system:");

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
		column.setWidth(130);

		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Sensor");
		column1.setWidth(100);

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Protocol");
		column2.setWidth(70);
		
		final TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText("Priority");
		column3.setWidth(70);

		final TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setText("Periodic");
		column4.setWidth(70);

		final TableColumn column5 = new TableColumn(table, SWT.NONE);
		column5.setText("Period");
		column5.setWidth(70);

		
		Button btEditSensor = new Button(container, SWT.NONE);

		table.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btEditSensor.setEnabled(true);
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
				if (table.getSelectionIndex() > -1)
					editSensorProperties(parent.getDisplay());
			}
		});

		btEditSensor.setText("Edit Sensor");
		btEditSensor.setEnabled(false);
		btEditSensor.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					editSensorProperties(parent.getDisplay());
				}
			}
		});

		sensors = new ArrayList<>();

		setControl(container);
		setPageComplete(false);
	}

	// Edit the properties of the specified sensor
	public void editSensorProperties(Display display) {
		EditSensorShell edit = new EditSensorShell(table.getItem(table.getSelectionIndex()), display);

		if (edit.isConfirm()) {
			Sensor auxSensor = sensors.get(table.getSelectionIndex());

			TableItem aux = table.getItem(table.getSelectionIndex());
			aux.setText(1, edit.getSensor());
			aux.setText(2, edit.getProtocol());
			aux.setText(3, edit.getPriority());
			Button bt = (Button) aux.getData("periodic");
			//bt.setSelection(edit.getPeriodic());
			//aux.setText(5, edit.getPeriod());

			auxSensor.setName(edit.getSensor());
			auxSensor.setProtocol(edit.getProtocol());
			auxSensor.setPriority(Integer.valueOf(edit.getPriority()));
			//auxSensor.setPeriod(Integer.valueOf(edit.getPeriod()));
			//auxSensor.outputs.add(edit.getSignal());

			sensors.set(table.getSelectionIndex(), auxSensor);

			checkSpecified();
		}
	}

	/*
	 * Verify the input table and according the vector amount of each input port
	 * the signals are inserted into the sensors list specification
	 */
	public void populateSensorsTable(Table inputTable, ArrayList<Sensing> subsystems) {
		// If table have data clear it
		if (table.getItemCount() > 0)
			clearData();

		Sensor sensor;
		TableItem item;
		
		TableEditor teEditor;
		
		Button postReading;
		//Button bperiodic;

		Combo cSensor;
		
		// Insert the outputs that not need of post-reading
		for (int i = 0; i < inputTable.getItemCount(); i++) {
			postReading = (Button) inputTable.getItem(i).getData("postReading");
			cSensor = (Combo) inputTable.getItem(i).getData("sensor");
			if (!postReading.getSelection()) {
				Label port = (Label) inputTable.getItem(i).getData("port");
				Text size = (Text) inputTable.getItem(i).getData("size");
				for (int z = 0; z < Integer.valueOf(size.getText()); z++) {
					item = new TableItem(table, SWT.NONE);
					item.setText(0, port.getText() + (z + 1));
					
					if(!cSensor.getText().isEmpty())
						item.setText(1, cSensor.getText());
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

		// insert the outputs that are specified in the post-reading process
		for (int i = 0; i < subsystems.size(); i++) {
			for (int z = 0; z < subsystems.get(i).getInputs().size(); z++) {
				item = new TableItem(table, SWT.NONE);
				item.setText(0, subsystems.get(i).getInputs().get(z));
				
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

		// populate Array of sensors
		for (int i = 0; i < table.getItemCount(); i++) {
			sensor = new Sensor();
			sensor.setIndex(i);
			sensor.outputs.add(table.getItem(i).getText(0));
			if(table.getItem(i).getText(1).isEmpty())
				sensor.setName(table.getItem(i).getText(1));
			
			sensors.add(sensor);
		}

		checkSpecified();
	}

	// clear all data of actuators
	private void clearData() {
		for(int i = 0; i < table.getItemCount(); i++){
			Button check = (Button) table.getItem(i).getData("periodic");
			check.dispose();
		}
		table.removeAll();
		sensors.clear();
	}

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
	
	protected ArrayList<Sensor> getSensors(){
		return sensors;
	}
}
