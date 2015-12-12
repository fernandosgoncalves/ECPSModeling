package ecpsmodeling.parser;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import java.util.ArrayList;
import org.eclipse.swt.SWT;

public class SenFunctionShell {
	public static Integer WIDTH = 440;
	public static Integer HEIGHT = 480;
	
	protected boolean confirm = false;
	protected boolean edit = false;

	protected ArrayList<String> inputs;
	protected ArrayList<String> functionOut;
	protected ArrayList<String> outputs;

	protected TabFolder tabFolder;

	protected Table tableFunctionOut;
	protected Table tableOutputs;
	protected Table tableInputs;
	
	protected String txtTemplate;
	protected String txtName;

	protected Label lsTemplate;
	protected Label lsname;

	protected Button cancel;
	protected Button ok;

	protected Combo cTemplate;
	
	protected Text tInput;
	protected Text name;

	final Shell shell;

	public SenFunctionShell(Display display, ArrayList<String> iinputs) {
		shell = new Shell(display,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.BORDER | SWT.CLOSE | SWT.CENTER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 10;
		layout.marginTop = 8;

		shell.setLayout(layout);
		shell.setSize(WIDTH, HEIGHT);
		shell.setText("Post-reading Function Specification");

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

		inputs = new ArrayList<String>();
		functionOut = new ArrayList<String>();
		outputs = new ArrayList<String>();

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

	public SenFunctionShell(Display display, ArrayList<String> iinputs, SensingFunction sensing, String template) {
		shell = new Shell(display,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.BORDER | SWT.CLOSE | SWT.CENTER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 10;
		layout.marginTop = 8;

		shell.setLayout(layout);
		shell.setSize(WIDTH, HEIGHT);
		shell.setText("Pre-writing Function Specification");

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

		inputs = new ArrayList<String>();
		functionOut = new ArrayList<String>();
		outputs = new ArrayList<String>();

		createControl();
		init(iinputs, sensing, template);

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

		lsTemplate = new Label(shell, SWT.NONE);
		lsTemplate.setText("Template:");

		cTemplate = new Combo(shell, SWT.SINGLE | SWT.BORDER);
		cTemplate.add("KalmanFilter");
		cTemplate.add("ComplementaryFilter");
		//cTemplate.add("DCMotorsActuation");
		cTemplate.setLayoutData(ilayout);
		
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
				outputs.clear();

				for (i = 0; i < tableOutputs.getItemCount(); i++) {
					outputs.add(tableOutputs.getItem(i).getText(0));
				}

				for (i = 0; i < tableInputs.getItemCount(); i++) {
					inputs.add(tableInputs.getItem(i).getText(0));
				}

				for (i = 0; i < tableFunctionOut.getItemCount(); i++) {
					functionOut.add(tableFunctionOut.getItem(i).getText(0));
				}

				txtTemplate = cTemplate.getText();
				
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

	private Control getTabOutputsControl(TabFolder tabFolder) {
		// Create a composite and add four buttons to it
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout folder = new GridLayout();
		folder.numColumns = 2;
		composite.setLayout(folder);

		// ---------------------- Table ---------------------------
		tableOutputs = new Table(composite, SWT.BORDER);
		tableOutputs.setLinesVisible(true);
		tableOutputs.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.verticalSpan = 2;

		tableOutputs.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn column = new TableColumn(tableOutputs, SWT.NONE);
		column.setText("Outputs");
		column.setWidth(150);

		// ---------------------- Table ---------------------------
		tableFunctionOut = new Table(composite, SWT.BORDER);
		tableFunctionOut.setLinesVisible(true);
		tableFunctionOut.setHeaderVisible(true);

		tableFunctionOut.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn columnOut = new TableColumn(tableFunctionOut, SWT.NONE);
		columnOut.setText("Function Outputs");
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
				if (tableFunctionOut.getSelectionIndex() > -1) {
					TableItem item = new TableItem(tableOutputs, SWT.NONE);
					item.setText(0, tableFunctionOut.getItem(tableFunctionOut.getSelectionIndex()).getText(0));
					tableFunctionOut.remove(tableFunctionOut.getSelectionIndex());
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
				if (tableOutputs.getSelectionIndex() > -1) {
					TableItem itemADD = new TableItem(tableFunctionOut, SWT.NONE);
					itemADD.setText(0, tableOutputs.getItem(tableOutputs.getSelectionIndex()).getText(0));
					tableOutputs.remove(tableOutputs.getSelectionIndex());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		return composite;
	}

	private Control getTabInputsControl(TabFolder tabFolder) {
		// Create a composite and add four buttons to it
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout folder = new GridLayout();
		folder.numColumns = 3;
		composite.setLayout(folder);

		Label input = new Label(composite, SWT.NONE);
		input.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		input.setText("Input:");

		GridData text = new GridData();
		text.widthHint = 200;
		tInput = new Text(composite, SWT.BORDER);
		tInput.setLayoutData(text);

		GridData bt = new GridData();
		bt.widthHint = 70;

		Button addOutput = new Button(composite, SWT.PUSH);
		addOutput.setText("Add");
		addOutput.setLayoutData(bt);
		addOutput.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!tInput.getText().isEmpty()) {
					TableItem item = new TableItem(tableInputs, SWT.NONE);
					item.setText(0, tInput.getText());
					tInput.setText("");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// ---------------------- Table ---------------------------
		tableInputs = new Table(composite, SWT.BORDER);
		tableInputs.setLinesVisible(true);
		tableInputs.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 2;
		data.verticalSpan = 2;

		tableInputs.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn column = new TableColumn(tableInputs, SWT.NONE);
		column.setText("Inputs");
		column.setWidth(150);

		// ------------Buttons------------
		bt.verticalAlignment = SWT.TOP;

		Button removeOutput = new Button(composite, SWT.PUSH);
		removeOutput.setText("Remove");
		removeOutput.setLayoutData(bt);

		removeOutput.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableInputs.getSelectionIndex() > -1)
					tableInputs.remove(tableInputs.getSelectionIndex());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return composite;
	}

	private void init(ArrayList<String> inputs) {
		for (int i = 0; i < inputs.size(); i++) {
			TableItem item = new TableItem(tableOutputs, SWT.NONE);
			item.setText(inputs.get(i));
		}
	}

	private void init(ArrayList<String> inputs, SensingFunction sensing, String template) {
		init(inputs);
		name.setText(sensing.getName());
		for (int i = 0; i < sensing.getOutputs().size(); i++) {
			TableItem item = new TableItem(tableFunctionOut, SWT.NONE);
			item.setText(0, sensing.getOutputs().get(i));
		}
		for (int i = 0; i < sensing.getInputs().size(); i++) {
			TableItem item = new TableItem(tableInputs, SWT.NONE);
			item.setText(sensing.getInputs().get(i));
		}
		for(int i = 0; i < cTemplate.getItemCount(); i++){
			if(cTemplate.getItem(i).equals(template))
				cTemplate.select(i);
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

	public ArrayList<String> getOutputs() {
		return outputs;
	}

	public ArrayList<String> getInputs() {
		return inputs;
	}

	public ArrayList<String> getFunction() {
		return functionOut;
	}
	
	public String geTemplate() {
		return txtTemplate;
	}

}

