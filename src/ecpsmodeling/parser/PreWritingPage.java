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

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class PreWritingPage extends WizardPage {
	private Composite container;

	protected Tree table;

	protected ArrayList<String> inputs;

	protected Label information;

	public PreWritingPage() {
		super("Prewriting Specification");
		setTitle("Prewriting Specification");
		setDescription("Detail the subsystems:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		inputs = new ArrayList<String>();

		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		information = new Label(container, SWT.NONE);
		GridData ilayout = new GridData();
		ilayout.horizontalSpan = 3;
		information.setLayoutData(ilayout);
		information.setText("Analyze the input ports of the mathematical model:");

		// ---------------------- Table ---------------------------
		table = new Tree(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 3;

		table.setLayoutData(data);
		table.setSize(300, 100);

		final TreeColumn column = new TreeColumn(table, SWT.LEFT);
		column.setText("Process");
		column.setWidth(150);

		final TreeColumn column2 = new TreeColumn(table, SWT.NONE);
		column2.setText("Inputs");
		column2.setWidth(150);

		final TreeColumn column3 = new TreeColumn(table, SWT.NONE);
		column3.setText("Actuators");
		column3.setWidth(150);

		Button btAddSubsystem = new Button(container, SWT.NONE);
		Button btEditSubsystem = new Button(container, SWT.NONE);
		Button btRemoveSubsystem = new Button(container, SWT.NONE);

		table.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btEditSubsystem.setEnabled(true);
				btRemoveSubsystem.setEnabled(true);
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
				editSubsystemProperties(parent.getDisplay());
			}
		});

		btAddSubsystem.setText("Add Subsystem");
		btAddSubsystem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					addSubsystem(parent.getDisplay());
				}
			}
		});
		// btAddSubsystem.setEnabled(false);

		btEditSubsystem.setText("Edit Subsystem");
		btEditSubsystem.setEnabled(false);
		btEditSubsystem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					editSubsystemProperties(parent.getDisplay());
				}
			}
		});

		btRemoveSubsystem.setText("Remove Subsystem");
		btRemoveSubsystem.setEnabled(false);

		setControl(container);
		setPageComplete(true);
	}

	public void populateSignals(Table table) {
		// System.out.println("Begin");
		for (int i = 0; i < table.getItemCount(); i++) {
			// System.out.println("Item "+i);
			Label port = (Label) table.getItem(i).getData("port");
			Text size = (Text) table.getItem(i).getData("size");
			Button prewriting = (Button) table.getItem(i).getData("PreWcheck");
			// prewriting.is
			// System.out.println("Pre IF");
			if (prewriting.getSelection()) {
				// System.out.println("IF");
				// System.out.println("Size "+size.getText());
				for (int z = 0; z < Integer.valueOf(size.getText()); z++) {
					// System.out.println(port.getText() + (z + 1));
					inputs.add(port.getText() + (z + 1));
				}
			}
		}

	}

	public void addSubsystem(Display display) {
		AddSubsystemShell add = new AddSubsystemShell(display, inputs);
		if (add.isConfirm()) {
			inputs = add.getInputs();
			addSubsys(add.getName(), add.getSubsys(), add.getOutputs());
		}
	}

	public void addSubsys(String name, ArrayList<String> inputs, ArrayList<String> outputs){
		TreeItem treeItem = new TreeItem(table, SWT.NONE);
		TreeItem subitem;
		treeItem.setText(0, name);
		treeItem.setText(1, "inputs");
		treeItem.setText(2, "outputs");
		System.out.println("IS: " + inputs.size() + " OS: " + outputs.size());
		if(inputs.size() >= outputs.size()){
			System.out.println("IS >= OS");
			for(int i = 0; i < inputs.size(); i++){
				subitem = new TreeItem(treeItem, SWT.NONE);
				if(i < outputs.size()){
					System.out.println("I: " + inputs.get(i));
					subitem.setText(1, inputs.get(i));
					System.out.println("O: " + outputs.get(i));
					subitem.setText(2, outputs.get(i));
				}else{
					System.out.println("Else");
					System.out.println("I: " + inputs.get(i));
					subitem.setText(1, inputs.get(i));
				}
			}
		}else{
			System.out.println("IS < OS");
			for(int i = 0; i < outputs.size(); i++){
				subitem = new TreeItem(treeItem, SWT.NONE);
				if(i < inputs.size()){
					System.out.println("I: " + inputs.get(i));
					subitem.setText(1, inputs.get(i));
					System.out.println("O: " + outputs.get(i));
					subitem.setText(2, outputs.get(i));
				}else{
					System.out.println("Else");
					System.out.println("O: " + outputs.get(i));					
					subitem.setText(2, outputs.get(i));					
				}
			}
		}
	}
	
	public void editSubsystemProperties(Display display) {
		/*
		 * EditSubsystemShell edit = new
		 * EditSubsystemShell(table.getSelection()), display); if
		 * (edit.isConfirm()) { TableItem aux =
		 * table.getItem(table.getSelectionIndex()); aux.setText(0,
		 * edit.getSignal()); aux.setText(1, edit.getActuator()); aux.setText(2,
		 * edit.getSampling()); aux.setText(3, edit.getProtocol());
		 * aux.setText(4, edit.getPriority()); }
		 */
	}

	/*
	 * public void populateInputList(SubSystem subsystem) { for (int i = 0; i <
	 * subsystem.getInPortsCount(); i++) { TableItem item = new TableItem(table,
	 * SWT.NONE);
	 * 
	 * TableEditor editor = new TableEditor(table);
	 * 
	 * Button PreWcheck = new Button(table, SWT.CHECK); Button check = new
	 * Button(table, SWT.CHECK);
	 * 
	 * Label port = new Label(table, SWT.NONE);
	 * 
	 * Text size = new Text(table, SWT.NONE);
	 * 
	 * port.setText(subsystem.getInPort(i).getName());
	 * 
	 * editor.grabHorizontal = true; editor.setEditor(port, item, 0);
	 * item.setData("port", port);
	 * 
	 * editor = new TableEditor(table); check.addSelectionListener(new
	 * SelectionListener() {
	 * 
	 * @Override public void widgetSelected(SelectionEvent e) { // TODO
	 * Auto-generated method stub if(size.isEnabled()) size.setEnabled(false);
	 * else size.setEnabled(true); }
	 * 
	 * @Override public void widgetDefaultSelected(SelectionEvent e) { // TODO
	 * Auto-generated method stub
	 * 
	 * } }); check.pack(); editor.minimumWidth = check.getSize().x;
	 * editor.horizontalAlignment = SWT.LEFT; editor.setEditor(check, item, 1);
	 * item.setData("check", check);
	 * 
	 * editor = new TableEditor(table); size.setText("1");
	 * size.addListener(SWT.Verify, new Listener() {
	 * 
	 * @Override public void handleEvent(Event e) { String string = e.text;
	 * char[] chars = new char[string.length()]; string.getChars(0,
	 * chars.length, chars, 0); for (int i = 0; i < chars.length; i++) { if
	 * (!('0' <= chars[i] && chars[i] <= '9')) { e.doit = false; return; } }
	 * 
	 * } }); size.setEnabled(false); editor.grabHorizontal = true;
	 * editor.setEditor(size, item, 2); item.setData("size", size);
	 * 
	 * PreWcheck = new Button(table, SWT.CHECK); //PreWcheck.setEnabled(false);
	 * 
	 * editor = new TableEditor(table);
	 * 
	 * PreWcheck.pack(); editor.minimumWidth = PreWcheck.getSize().x;
	 * editor.horizontalAlignment = SWT.LEFT; editor.setEditor(PreWcheck, item,
	 * 3); item.setData("PreWcheck", PreWcheck);
	 * 
	 * } }
	 */

	/*
	 * public Table getTable(){ return table; }
	 */
}
