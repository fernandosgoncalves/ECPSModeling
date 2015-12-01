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

public class ECPSModelingPage3 extends WizardPage {
	private Composite container;

	//List list;

	Table table;
	
	protected Label information;

	public ECPSModelingPage3() {
		super("Sensing Specification");
		setTitle("Sensing Specification");
		setDescription("Detail the Sensing subsystem:");
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		GridData gridData = new GridData();

		container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		container.setSize(300, 200);

		information = new Label(container, SWT.NONE);
		information.setText("Analyze the input ports of the mathematical model:");

		//list = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		//gridData.widthHint = 300;
//		gridData.heightHint = 200;
//		list.setLayoutData(gridData);
//		list.addSelectionListener(new SelectionListener() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				setPageComplete(true);
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
//
//			}
//		});
		
		//---------------------- Table ---------------------------
		table = new Table(container, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);

		table.setLayoutData(data);
		//table.setLayout(layout);
		
		//String[] titles = { "Input", "Vector", "Inputs Size" };
		
		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Input");
		column.setWidth(200);

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Vector");
		column2.setWidth(100);

		final TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText("Inputs Size");
		column3.setWidth(100);
				
		/*for (int i = 0; i < titles.length; i++) {
			final TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
			column.setWidth(100);
			//column.pack();
		}*/

		//TableItem item = new TableItem(table, SWT.NONE);
		//item.setText(new String[] { "Input", "Vector", "Inputs Size" });
//		for (int i = 0; i < 12; i++) {
//			if(i==0){
//				TableItem item = new TableItem(table, SWT.NONE);
//				item.setText(new String[] { "Input", "Vector", "Inputs Size" });
//			}else
//				new TableItem(table, SWT.NONE);
//		}

//		TableItem[] itens = table.getItems();
					
/*		for (int i = 1; i < itens.length; i++) {
			TableEditor editor = new TableEditor(table);
			
			Button button = new Button(table, SWT.CHECK);
			
			CCombo combo = new CCombo(table, SWT.NONE);
					
			Text text = new Text(table, SWT.NONE);
			
			combo.setText("CCombo");
			combo.add("item 1");
			combo.add("item 2");
			
			editor.grabHorizontal = true;
			editor.setEditor(combo, itens[i], 0);

			editor = new TableEditor(table);
			text.setText("0");			
			editor.grabHorizontal = true;
			editor.setEditor(text, itens[i], 1);
			
			editor = new TableEditor(table);
			button.pack();
			editor.minimumWidth = button.getSize().x;
			editor.horizontalAlignment = SWT.LEFT;
			editor.setEditor(button, itens[i], 2);
		}*/
		
		
		setControl(container);
		setPageComplete(false);
	}

	public void populateInputList(SubSystem subsystem) {
		// System.out.println("Quantidade: " + subsystem.getInPortsCount());
		for (int i = 0; i < subsystem.getInPortsCount(); i++) {
			// System.out.println("Porta: " + subsystem.getInPort(i).getName());
			//list.add(subsystem.getInPort(i).getName());
			TableItem item = new TableItem(table, SWT.NONE);
			
			TableEditor editor = new TableEditor(table);
			
			Label port = new Label(table, SWT.NONE);
			
			Button check = new Button(table, SWT.CHECK);
			
			Text size = new Text(table, SWT.NONE);

			port.setText(subsystem.getInPort(i).getName());
			
			editor.grabHorizontal = true;
			editor.setEditor(port, item, 0);

			editor = new TableEditor(table);
			check.addSelectionListener(new SelectionListener() {				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					setPageComplete(true);
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
			
			editor = new TableEditor(table);
			size.setText("1");
			size.setEditable(false);
			editor.grabHorizontal = true;
			editor.setEditor(size, item, 2);
		}
	}
}
