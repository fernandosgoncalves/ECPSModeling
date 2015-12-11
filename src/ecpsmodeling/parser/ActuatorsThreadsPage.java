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
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import java.util.ArrayList;
import org.eclipse.swt.SWT;

public class ActuatorsThreadsPage extends WizardPage {
	protected ArrayList<Actuator> actuatorsList;
	
	protected ArrayList<AADLThread> threads;
	
	private Composite container;

	protected Label pinformation;
	protected Label sinformation;

	protected Tree periodicTable;
	protected Tree sporadicTable;
	
	public ActuatorsThreadsPage() {
		super("Actuators Threads Specification");
		setTitle("Actuators Threads Specification");
		setDescription("Organize the System Threads that Manage the Actuators:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		pinformation = new Label(container, SWT.NONE);
		GridData ilayout = new GridData();
		ilayout.horizontalSpan = 3;
		pinformation.setLayoutData(ilayout);
		pinformation.setText("Periodic Threads:");

		// ---------------------- Table ---------------------------
		periodicTable = new Tree(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		periodicTable.setLinesVisible(true);
		periodicTable.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 3;

		periodicTable.setLayoutData(data);
		periodicTable.setSize(300, 50);

		final TreeColumn column = new TreeColumn(periodicTable, SWT.LEFT);
		column.setText("Thread");
		column.setWidth(150);

		final TreeColumn column1 = new TreeColumn(periodicTable, SWT.NONE);
		column1.setText("Period");
		column1.setWidth(70);

		final TreeColumn column2 = new TreeColumn(periodicTable, SWT.NONE);
		column2.setText("Priority");
		column2.setWidth(80);

		final TreeColumn column3 = new TreeColumn(periodicTable, SWT.NONE);
		column3.setText("Actuators");
		column3.setWidth(100);
		
		Button btAddPeriodicThread = new Button(container, SWT.NONE);
		Button btEditPeriodicThread = new Button(container, SWT.NONE);
		Button btRemovePeriodicThread = new Button(container, SWT.NONE);

		periodicTable.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btEditPeriodicThread.setEnabled(true);
				//btRemovePeriodicThread.setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stubperiodicTable
			}
		});

		periodicTable.addMouseListener(new MouseListener() {
			
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
				editPeriodicThread(parent.getDisplay());
			}
		});

		btAddPeriodicThread.setText("Add Periodic Thread");
		btAddPeriodicThread.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					addPeriodicThread(parent.getDisplay());
				}
			}
		});
		
		btEditPeriodicThread.setText("Edit Periodic Thread");
		btEditPeriodicThread.setEnabled(false);
		btEditPeriodicThread.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					editPeriodicThread(parent.getDisplay());
				}
			}
		});

		btRemovePeriodicThread.setText("Remove Periodic Thread");
		btRemovePeriodicThread.setEnabled(false);
				
		sinformation = new Label(container, SWT.NONE);
		ilayout.horizontalSpan = 3;
		sinformation.setLayoutData(ilayout);
		sinformation.setText("Sporadic Threads:");
		
		// ---------------------- Table ---------------------------
		sporadicTable = new Tree(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sporadicTable.setLinesVisible(true);
		sporadicTable.setHeaderVisible(true);

		GridData data2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		data2.horizontalSpan = 3;

		sporadicTable.setLayoutData(data2);
		sporadicTable.setSize(300, 50);

		final TreeColumn scolumn = new TreeColumn(sporadicTable, SWT.LEFT);
		scolumn.setText("Thread");
		scolumn.setWidth(150);

		final TreeColumn scolumn1 = new TreeColumn(sporadicTable, SWT.NONE);
		scolumn1.setText("Period");
		scolumn1.setWidth(70);

		final TreeColumn scolumn2 = new TreeColumn(sporadicTable, SWT.NONE);
		scolumn2.setText("Priority");
		scolumn2.setWidth(80);

		final TreeColumn scolumn3 = new TreeColumn(sporadicTable, SWT.NONE);
		scolumn3.setText("Actuators");
		scolumn3.setWidth(100);
		
		Button btAddSporadicThread = new Button(container, SWT.NONE);
		Button btEditSporadicThread = new Button(container, SWT.NONE);
		Button btRemoveSporadicThread = new Button(container, SWT.NONE);

		sporadicTable.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btEditSporadicThread.setEnabled(true);
				//btRemoveSporadicThread.setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		sporadicTable.addMouseListener(new MouseListener() {
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
				editSporadicThread(parent.getDisplay());
			}
		});

		btAddSporadicThread.setText("Add Sporadic Thread");
		btAddSporadicThread.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					addSporadicThread(parent.getDisplay());
				}
			}
		});
		
		btEditSporadicThread.setText("Edit Sporadic Thread");
		btEditSporadicThread.setEnabled(false);
		btEditSporadicThread.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					editSporadicThread(parent.getDisplay());
				}
			}
		});

		btRemoveSporadicThread.setText("Remove Sporadic Thread");
		btRemoveSporadicThread.setEnabled(false);
		
		threads = new ArrayList<>();
		actuatorsList = new ArrayList<>();

		setControl(container);
		setPageComplete(false);
	}

	private void addPeriodicThread(Display display) {
		//System.out.println(actuatorsList.size());
		ActuationThreadShell add = new ActuationThreadShell(display, actuatorsList, "Add Periodic Thread", true);
		
		if(add.isConfirm()){
			actuatorsList = add.getActuators();
			System.out.println(add.getThreadActuators().size());
			addThread(add.getName(), add.getPeriod(), add.getPriority(), add.getThreadActuators(), add.geTemplate(), true);
		}
		
		checkSpecified();
		
	}
		
	private void addThread(String name, int period, int priority, ArrayList<Actuator> threadActuators, String sTemplate, boolean bperiodic) {
		AADLThread aux = new AADLThread();
		
		if(bperiodic){
			TreeItem treeItem = new TreeItem(periodicTable, SWT.NONE);
			TreeItem subitem;
			aux.setName(name);
			aux.setPeriod(period);
			aux.setPeriodic(bperiodic);
			aux.setActuators(threadActuators);
			aux.setPriority(priority);
			aux.setTemplate(sTemplate);
			aux.setIndex(periodicTable.getItemCount()-1);
			//aux.setFunctions(threadFunctions);
			
			threads.add(aux);
			
			treeItem.setText(0, name);
			treeItem.setText(1, String.valueOf(period));
			treeItem.setText(2, String.valueOf(priority));

			//if (inputs.size() >= outputs.size()) {
			for (int i = 0; i < threadActuators.size(); i++) {
				subitem = new TreeItem(treeItem, SWT.NONE);
				//if (i < outputs.size()) {
				//	subitem.setText(1, inputs.get(i));
				//	subitem.setText(2, outputs.get(i));
				//} else {
					subitem.setText(3, threadActuators.get(i).getName());
				//}
			}
			/*} else {
			for (int i = 0; i < outputs.size(); i++) {
				subitem = new TreeItem(treeItem, SWT.NONE);
				if (i < inputs.size()) {
					subitem.setText(1, inputs.get(i));
					subitem.setText(2, outputs.get(i));
				} else {
					subitem.setText(2, outputs.get(i));
				}
			}*/
		}else{
			TreeItem treeItem = new TreeItem(sporadicTable, SWT.NONE);
			TreeItem subitem;
			aux.setName(name);
			aux.setPeriod(period);
			aux.setPeriodic(bperiodic);
			aux.setActuators(threadActuators);
			aux.setPriority(priority);
			aux.setTemplate(sTemplate);
			aux.setIndex(sporadicTable.getItemCount()-1);
			//aux.setFunctions(threadFunctions);
			
			threads.add(aux);
			
			treeItem.setText(0, name);
			treeItem.setText(1, String.valueOf(period));
			treeItem.setText(2, String.valueOf(priority));

			//if (inputs.size() >= outputs.size()) {
			for (int i = 0; i < threadActuators.size(); i++) {
				subitem = new TreeItem(treeItem, SWT.NONE);
				//if (i < outputs.size()) {
				//	subitem.setText(1, inputs.get(i));
				//	subitem.setText(2, outputs.get(i));
				//} else {
					subitem.setText(3, threadActuators.get(i).getName());
				//}
			}
			/*} else {
			for (int i = 0; i < outputs.size(); i++) {
				subitem = new TreeItem(treeItem, SWT.NONE);
				if (i < inputs.size()) {
					subitem.setText(1, inputs.get(i));
					subitem.setText(2, outputs.get(i));
				} else {
					subitem.setText(2, outputs.get(i));
				}
			}*/
		}
	}

	// Edit the properties of the specified sensor
	public void editPeriodicThread(Display display) {
		AADLThread thread = getPeriodicThread(periodicTable.getSelection()[0].getText(0));

		ActuationThreadShell edit = new ActuationThreadShell(display, actuatorsList, thread, "Edit Periodic Thread", true);

		if (edit.isConfirm()) {
			actuatorsList = edit.getActuators();
			editThread(edit.getName(), edit.geTemplate(), edit.getThreadActuators(), true);

			checkSpecified();
		}
	}
	
	private void editThread(String name, String geTemplate, ArrayList<Actuator> threadActuators, boolean bperiodic) {
		// TODO Auto-generated method stub
		
	}

	protected void addSporadicThread(Display display) {
		ActuationThreadShell add = new ActuationThreadShell(display, actuatorsList, "Add Sporadic Thread", false);
		
		if(add.isConfirm()){
			actuatorsList = add.getActuators();
			addThread(add.getName(), add.getPeriod(), add.getPriority(), add.getThreadActuators(), add.geTemplate(), false);
		}
		
		checkSpecified();
		
	}

	protected void editSporadicThread(Display display) {
		// TODO Auto-generated method stub
		
	}

	private AADLThread getPeriodicThread(String tname) {
		if(!threads.isEmpty()){
			for(int i = 0; i < threads.size(); i++){
				if(threads.get(i).getName().equals(tname) && threads.get(i).isPeriodic())
					return threads.get(i);
			}
		}
		return null;
	}

	/*
	 * Verify the input table and according the vector amount of each input port
	 * the signals are inserted into the sensors list specification
	 */
	public void populateThreadsTable(ArrayList<Actuator> actuators) {
		// If table have data clear it
		if (periodicTable.getItemCount() > 0 || sporadicTable.getItemCount() > 0)
			clearData();

		actuatorsList.clear();
		
		for(int i = 0; i < actuators.size(); i++){
			actuatorsList.add(actuators.get(i));
		}
		
		checkSpecified();
	}

	// clear all data of actuators
	private void clearData() {
		periodicTable.removeAll();
		sporadicTable.removeAll();
		threads.clear();
	}

	private void checkSpecified() {
		/*Boolean check = true;
		for (int i = 0; i < periodicTable.getItemCount(); i++) {
			for (int z = 0; z < periodicTable.getColumnCount(); z++) {
				if (periodicTable.getItem(i).getText(z).isEmpty() && z != 4)
					check = false;
			}
		}
		if (check)
			setPageComplete(true);
		else
			setPageComplete(false);*/
	}
	
	protected ArrayList<AADLThread> getThreads(){
		return threads;
	}

}
