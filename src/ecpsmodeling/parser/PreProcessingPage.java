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

import org.eclipse.swt.events.MouseListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import java.util.ArrayList;

public class PreProcessingPage extends WizardPage {
	protected static final int ACTUATION = 1;
	
	private Composite container;

	protected Tree table;

	protected ArrayList<SystemFunction> actFunctions;
	//protected ArrayList<ActuationFunction> actFunctions;
	protected ArrayList<String> inputs;

	protected Label information;

	protected Button btRemoveFunction;
	protected Button btEditFunction;
	protected Button btAddFunction;

	public PreProcessingPage() {
		super("Pre-processing Specification");
		setTitle("Pre-processing Specification");
		setDescription("Detail the pre-processing system functions:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		actFunctions = new ArrayList<>();
		inputs = new ArrayList<String>();
		
		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		information = new Label(container, SWT.NONE);
		GridData ilayout = new GridData();
		ilayout.horizontalSpan = 3;
		information.setLayoutData(ilayout);
		information.setText("Define the required pre-processing functions:");

		// ---------------------- Table ---------------------------
		table = new Tree(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 3;

		table.setLayoutData(data);
		table.setSize(300, 100);

		final TreeColumn column = new TreeColumn(table, SWT.LEFT);
		column.setText("Function");
		column.setWidth(160);

		final TreeColumn column2 = new TreeColumn(table, SWT.NONE);
		column2.setText("Inputs");
		column2.setWidth(160);

		final TreeColumn column3 = new TreeColumn(table, SWT.NONE);
		column3.setText("Outputs");
		column3.setWidth(160);

		btAddFunction = new Button(container, SWT.NONE);
		btEditFunction = new Button(container, SWT.NONE);
		btRemoveFunction = new Button(container, SWT.NONE);

		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					btEditFunction.setEnabled(true);
					//btRemoveFunction.setEnabled(true);
				}				
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
				editFunctionProperties(parent.getDisplay());
			}
		});

		btAddFunction.setText("Add Function");
		btAddFunction.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					addFunction(parent.getDisplay());
				}
			}
		});
		// btAddSubsystem.setEnabled(false);

		btEditFunction.setText("Edit Function");
		btEditFunction.setEnabled(false);
		btEditFunction.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					editFunctionProperties(parent.getDisplay());
				}
			}
		});

		btRemoveFunction.setText("Remove Function");
		btRemoveFunction.setEnabled(false);

		setControl(container);
		setPageComplete(false);
	}

	public void populateSignals(Table table) {
		if(table.getItemCount() > 0)
			clearData();
		inputs.clear();
		for (int i = 0; i < table.getItemCount(); i++) {
			Label port = (Label) table.getItem(i).getData("port");
			Text size = (Text) table.getItem(i).getData("size");
			Button prewriting = (Button) table.getItem(i).getData("PreWcheck");
			if (prewriting.getSelection()) {
				for (int z = 0; z < Integer.valueOf(size.getText()); z++) {
					inputs.add(port.getText() + (z + 1));
				}
			}
		}
		checkConditions();
	}

	public void clearData(){
		table.removeAll();
		actFunctions.clear();		
	}
	
	public void addFunction(Display display) {
		ActuationFunctionShell add = new ActuationFunctionShell(display, inputs);
		if (add.isConfirm()) {
			inputs = add.getInputs();
			addFunction(add.getName(), add.getFuntion(), add.getOutputs(), add.geTemplate());
		}
	}

	public void addFunction(String name, ArrayList<String> inputs, ArrayList<String> outputs, String template) {
		SystemFunction aux = new SystemFunction(ACTUATION);
		TreeItem treeItem = new TreeItem(table, SWT.NONE);
		TreeItem subitem;
		aux.setName(name);
		aux.setInputs(inputs);
		aux.setOutputs(outputs);
		aux.setIndex(table.getItemCount() - 1);
		aux.setTemplate(template);
		actFunctions.add(aux);

		treeItem.setText(0, name);
		treeItem.setText(1, "");
		treeItem.setText(2, "");

		if (inputs.size() >= outputs.size()) {
			for (int i = 0; i < inputs.size(); i++) {
				subitem = new TreeItem(treeItem, SWT.NONE);
				if (i < outputs.size()) {
					subitem.setText(1, inputs.get(i));
					subitem.setText(2, outputs.get(i));
				} else {
					subitem.setText(1, inputs.get(i));
				}
			}
		} else {
			for (int i = 0; i < outputs.size(); i++) {
				subitem = new TreeItem(treeItem, SWT.NONE);
				if (i < inputs.size()) {
					subitem.setText(1, inputs.get(i));
					subitem.setText(2, outputs.get(i));
				} else {
					subitem.setText(2, outputs.get(i));
				}
			}
		}
		checkConditions();
	}

	public void checkConditions() {
		if (inputs.isEmpty()) {
			setPageComplete(true);
			btAddFunction.setEnabled(false);
		} else {
			setPageComplete(false);
			btAddFunction.setEnabled(true);
		}
	}

	public SystemFunction getItemByName(String name){
		if(!actFunctions.isEmpty()){
			for(int i=0; i < actFunctions.size(); i++){
				if(actFunctions.get(i).getName().equals(name))
					return actFunctions.get(i);
			}
		}
		return null;
	}
	
	public void editFunction(String name, ArrayList<String> inputs, ArrayList<String> outputs, SystemFunction item, String template) {
		TreeItem[] treeItem = table.getSelection();
		for(int i = 0; i < treeItem.length; i++){
			treeItem[i].dispose();
		}
		
		TreeItem newTreeItem = new TreeItem(table, SWT.NONE, item.index);
		TreeItem newSubItem;
		
		/*
		 * Update Array 
		 */
		item.setName(name);
		item.setInputs(inputs);
		item.setOutputs(outputs);
		item.setTemplate(template);
		actFunctions.set(item.index, item);

		newTreeItem.setText(0, name);
		newTreeItem.setText(1, "");
		newTreeItem.setText(2, "");

		if (inputs.size() >= outputs.size()) {
			for (int i = 0; i < inputs.size(); i++) {
				newSubItem = new TreeItem(newTreeItem, SWT.NONE);
				if (i < outputs.size()) {
					newSubItem.setText(1, inputs.get(i));
					newSubItem.setText(2, outputs.get(i));
				} else {
					newSubItem.setText(1, inputs.get(i));
				}
			}
		} else {
			for (int i = 0; i < outputs.size(); i++) {
				newSubItem = new TreeItem(newTreeItem, SWT.NONE);
				if (i < inputs.size()) {
					newSubItem.setText(1, inputs.get(i));
					newSubItem.setText(2, outputs.get(i));
				} else {
					newSubItem.setText(2, outputs.get(i));
				}
			}
		}
		checkConditions();
	}
	
	public void editFunctionProperties(Display display) {
		SystemFunction aux = getItemByName(table.getSelection()[0].getText(0));
		ActuationFunctionShell edit = new ActuationFunctionShell(display, inputs, aux);
		if (edit.isConfirm()) {
			inputs = edit.getInputs();
			editFunction(edit.getName(), edit.getFuntion(), edit.getOutputs(), aux, edit.geTemplate());
		}
	}
	
	public ArrayList<SystemFunction> getActFunctions(){
		return actFunctions;
	}
}