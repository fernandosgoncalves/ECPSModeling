package ecpsmodeling.parser;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class ActSubsystemShell {
	protected boolean confirm = false;
	protected boolean edit = false;

	protected ArrayList<String> outputs;
	protected ArrayList<String> subsys;
	protected ArrayList<String> inputs;
	
	protected TabFolder tabFolder;

	protected Table tableSubsys;
	protected Table tableOutput;
	protected Table tableInput;

	protected String txtName;
	
	protected Label lsname;

	protected Button cancel;
	protected Button ok;
	
	protected Text tOuput;
	protected Text name;

	final Shell shell;

	public ActSubsystemShell(Display display, ArrayList<String> iinputs) {
		shell = new Shell(display,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.BORDER | SWT.CLOSE | SWT.CENTER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 10;
		layout.marginTop = 8;

		shell.setLayout(layout);
		shell.setSize(450, 450);
		shell.setText("Subsystem Specification");

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

		outputs = new ArrayList<String>();
		subsys = new ArrayList<String>();
		inputs = new ArrayList<String>();
		
		createControl();
		init(iinputs);

		shell.open();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
	}

	public ActSubsystemShell(Display display, ArrayList<String> iinputs, Actuation actuation) {
		shell = new Shell(display,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.BORDER | SWT.CLOSE | SWT.CENTER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 10;
		layout.marginTop = 8;

		shell.setLayout(layout);
		shell.setSize(450, 450);
		shell.setText("Subsystem Specification");

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

		outputs = new ArrayList<String>();
		subsys = new ArrayList<String>();
		inputs = new ArrayList<String>();
		
		createControl();
		init(iinputs, actuation);

		shell.open();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
	}
	
	private void createControl() {
		GridData ilayout = new GridData();
		ilayout.widthHint = 300;

		lsname = new Label(shell, SWT.NONE);
		lsname.setText("Name:");

		name = new Text(shell, SWT.SINGLE | SWT.BORDER);
		name.setLayoutData(ilayout);

		GridData gdTabFolder = new GridData();
		gdTabFolder.horizontalSpan = 2;
		gdTabFolder.widthHint = 400;
		gdTabFolder.heightHint = 300;
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(gdTabFolder);
		tabFolder.setSize(400, 200);

		TabItem tab0 = new TabItem(tabFolder, SWT.NONE);
		tab0.setText("Inputs");
		tab0.setControl(getTabInputsControl(tabFolder));

		TabItem tab1 = new TabItem(tabFolder, SWT.NONE);
		tab1.setText("Outputs");
		tab1.setToolTipText("This is tab two");
		tab1.setControl(getTabOutputsControl(tabFolder));

		GridData blayout = new GridData();
		blayout.widthHint = 80;

		ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setLayoutData(blayout);
		ok.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i;
				confirm = true;
							
				txtName = name.getText();
				inputs.clear();
				
				for(i = 0; i < tableInput.getItemCount(); i++){
					inputs.add(tableInput.getItem(i).getText(0));
				}
				
				for(i = 0; i < tableOutput.getItemCount(); i++){
					outputs.add(tableOutput.getItem(i).getText(0));
				}
				
				for(i = 0; i < tableSubsys.getItemCount(); i++){
					subsys.add(tableSubsys.getItem(i).getText(0));
				}
				
				shell.close();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setLayoutData(blayout);
		cancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private Control getTabInputsControl(TabFolder tabFolder) {
		// Create a composite and add four buttons to it
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout folder = new GridLayout();
		folder.numColumns = 2;
		composite.setLayout(folder);

		// ---------------------- Table ---------------------------
		tableInput = new Table(composite, SWT.BORDER);
		tableInput.setLinesVisible(true);
		tableInput.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.verticalSpan = 2;

		tableInput.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn column = new TableColumn(tableInput, SWT.NONE);
		column.setText("Input");
		column.setWidth(150);

		// ---------------------- Table ---------------------------
		tableSubsys = new Table(composite, SWT.BORDER);
		tableSubsys.setLinesVisible(true);
		tableSubsys.setHeaderVisible(true);

		tableSubsys.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn columnOut = new TableColumn(tableSubsys, SWT.NONE);
		columnOut.setText("Subsystem Inputs");
		columnOut.setWidth(150);

		// ------------Buttons------------
		Button remove = new Button(composite, SWT.PUSH);
		remove.setText("<<");
		GridData bt = new GridData();
		bt.horizontalAlignment = SWT.RIGHT;
		remove.setLayoutData(bt);
		remove.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tableSubsys.getSelectionIndex() > -1){
					TableItem item = new TableItem(tableInput, SWT.NONE);
					item.setText(0, tableSubsys.getItem(tableSubsys.getSelectionIndex()).getText(0));
					tableSubsys.remove(tableSubsys.getSelectionIndex());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		Button add = new Button(composite, SWT.PUSH);
		add.setText(">>");
		add.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tableInput.getSelectionIndex() > -1){
					TableItem itemADD = new TableItem(tableSubsys, SWT.NONE);
					itemADD.setText(0, tableInput.getItem(tableInput.getSelectionIndex()).getText(0));
					tableInput.remove(tableInput.getSelectionIndex());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		return composite;
	}

	private Control getTabOutputsControl(TabFolder tabFolder) {
		// Create a composite and add four buttons to it
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout folder = new GridLayout();
		folder.numColumns = 3;
		composite.setLayout(folder);

		Label output = new Label(composite, SWT.NONE);
		output.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		output.setText("Output:");
		
		GridData text = new GridData();
		text.widthHint = 200;
		tOuput = new Text(composite, SWT.BORDER);
		tOuput.setLayoutData(text);

		GridData bt = new GridData();
		bt.widthHint = 70;
		
		Button addOutput = new Button(composite, SWT.PUSH);
		addOutput.setText("Add");
		addOutput.setLayoutData(bt);
		addOutput.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!tOuput.getText().isEmpty()){
					TableItem item = new TableItem(tableOutput, SWT.NONE);
					item.setText(0, tOuput.getText());
					tOuput.setText("");
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// ---------------------- Table ---------------------------
		tableOutput = new Table(composite, SWT.BORDER);
		tableOutput.setLinesVisible(true);
		tableOutput.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 2;
		data.verticalSpan = 2;

		tableOutput.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn column = new TableColumn(tableOutput, SWT.NONE);
		column.setText("Outputs");
		column.setWidth(150);

		// ------------Buttons------------
		bt.verticalAlignment = SWT.TOP;
		//Button addOutput = new Button(composite, SWT.PUSH);
		//addOutput.setText("Add");
		//addOutput.setLayoutData(bt);
		
		/*Button editOutput = new Button(composite, SWT.PUSH);
		editOutput.setText("Edit");
		editOutput.setLayoutData(bt);*/
		
		Button removeOutput = new Button(composite, SWT.PUSH);
		removeOutput.setText("Remove");
		removeOutput.setLayoutData(bt);
		
		removeOutput.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tableOutput.getSelectionIndex() > -1)
					tableOutput.remove(tableOutput.getSelectionIndex());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*GridData bt = new GridData();
		bt.horizontalAlignment = SWT.RIGHT;
		remove.setLayoutData(bt);*/

		return composite;
	}

	private void init(ArrayList<String> inputs) {
		for (int i = 0; i < inputs.size(); i++) {
			TableItem item = new TableItem(tableInput, SWT.NONE);
			item.setText(inputs.get(i));
		}
	}

	private void init(ArrayList<String> inputs, Actuation actuation) {
		init(inputs);
		name.setText(actuation.getName());
		for(int i = 0; i < actuation.getInputs().size(); i++){
			TableItem item = new TableItem(tableSubsys, SWT.NONE);
			item.setText(0, actuation.getInputs().get(i));
		}
		for (int i = 0; i < actuation.getOutputs().size(); i++) {
			TableItem item = new TableItem(tableOutput, SWT.NONE);
			item.setText(actuation.getOutputs().get(i));
		}		
		edit = true;
	}
	
	public String getName() {
		return txtName;
	}

	public boolean isConfirm() {
		return confirm;
	}
	
	public boolean isEdit() {
		return edit;
	}
	
	public ArrayList<String> getInputs() {
		return inputs;
	}

	
	public ArrayList<String> getOutputs() {
		return outputs;
	}

	
	public ArrayList<String> getSubsys() {
		return subsys;
	}

}