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

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

public class InputsPage extends WizardPage {
	private Composite container;

	protected Table table;
	
	protected Label information;

	public InputsPage() {
		super("Actuation Analyze");
		setTitle("Actuation Analyze");
		setDescription("Detail the Actuation subsystem:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();

		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		information = new Label(container, SWT.NONE);
		information.setText("Analyze the input ports of the mathematical model:");
		
		//---------------------- Table ---------------------------
		table = new Table(container, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);

		table.setLayoutData(data);
		table.setSize(300, 100);
				
		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Input");
		column.setWidth(200);

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Vector");
		column2.setWidth(100);

		final TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText("Inputs Size");
		column3.setWidth(100);

		final TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setText("Pre-writing");
		column4.setWidth(65);

		
		setControl(container);
		setPageComplete(true);
	}

	public void populateInputList(SubSystem subsystem) {
		for (int i = 0; i < subsystem.getInPortsCount(); i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			
			TableEditor editor = new TableEditor(table);
					
			Button PreWcheck = new Button(table, SWT.CHECK);
			Button check = new Button(table, SWT.CHECK);
					
			Label port = new Label(table, SWT.NONE);
			
			Text size = new Text(table, SWT.NONE);
			
			port.setText(subsystem.getInPort(i).getName());
			
			editor.grabHorizontal = true;
			editor.setEditor(port, item, 0);
			item.setData("port", port);

			editor = new TableEditor(table);
			check.addSelectionListener(new SelectionListener() {				
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(size.isEnabled())
						size.setEnabled(false);
					else
						size.setEnabled(true);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			check.pack();
			editor.minimumWidth = check.getSize().x;
			editor.horizontalAlignment = SWT.LEFT;
			editor.setEditor(check, item, 1);
			item.setData("check", check);
			
			editor = new TableEditor(table);
			size.setText("1");
			size.addListener(SWT.Verify, new Listener() {	
				@Override
				public void handleEvent(Event e) {
					String string = e.text;
					char[] chars = new char[string.length()];
					string.getChars(0, chars.length, chars, 0);
					for (int i = 0; i < chars.length; i++) {
						if (!('0' <= chars[i] && chars[i] <= '9')) {
							e.doit = false;
						return;
					}
					}
					
				}
			});
			size.setEnabled(false);
			editor.grabHorizontal = true;
			editor.setEditor(size, item, 2);
			item.setData("size", size);
			
			PreWcheck = new Button(table, SWT.CHECK);
			//PreWcheck.setEnabled(false);
			
			editor = new TableEditor(table);
			
			PreWcheck.pack();
			editor.minimumWidth = PreWcheck.getSize().x;
			editor.horizontalAlignment = SWT.LEFT;
			editor.setEditor(PreWcheck, item, 3);
			item.setData("PreWcheck", PreWcheck);
			
		}
	}
	
	public Table getTable(){
		return table;
	}
}
