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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import java.util.ArrayList;
import org.eclipse.swt.SWT;

public class ActuationThreadShell {
	public static Integer WIDTH = 460;
	public static Integer HEIGHT = 520;

	protected boolean periodic = false;
	protected boolean confirm = false;
	protected boolean edit = false;

	protected ArrayList<Actuator> threadActuators;
	protected ArrayList<Actuator> actuators;

	protected ArrayList<ActuationFunction> functions;
	protected ArrayList<ActuationFunction> threadFunctions;

	protected TabFolder tabFolder;

	protected Table tableThreadActuators;
	protected Table tableThreadFunctions;
	protected Table tableFunctions;
	protected Table tableActuators;

	protected String txtTemplate;
	protected String txtName;

	protected int priority;
	protected int period;

	protected Label lsPeriodic;
	protected Label lsTemplate;
	protected Label lsPriority;
	protected Label lsPeriod;
	protected Label lsname;

	protected Button btPeriodic;
	protected Button cancel;
	protected Button ok;

	protected Combo cTemplate;

	protected Spinner sPriority;
	protected Spinner sPeriod;

	protected Text name;

	Shell shell;

	public ActuationThreadShell(Display display, ArrayList<Actuator> iinputs, String title, Boolean bperiodic,
			ArrayList<ActuationFunction> iFunctions) {
		createShell(display, title);

		periodic = bperiodic;

		createControl();

		threadActuators = new ArrayList<Actuator>();
		threadFunctions = new ArrayList<ActuationFunction>();
		actuators = new ArrayList<Actuator>();
		functions = new ArrayList<ActuationFunction>();

		init(iinputs, iFunctions);

		shell.open();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
	}

	public ActuationThreadShell(Display display, ArrayList<Actuator> iinputs, AADLThread thread, String title,
			Boolean bperiodic, ArrayList<ActuationFunction> iActFunctionsList) {
		createShell(display, title);

		periodic = bperiodic;

		createControl();

		threadActuators = new ArrayList<Actuator>();
		threadFunctions = new ArrayList<ActuationFunction>();
		actuators = new ArrayList<Actuator>();
		functions = new ArrayList<ActuationFunction>();

		init(iinputs, thread, iActFunctionsList);

		shell.open();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
	}

	private void createShell(Display display, String title) {
		shell = new Shell(display,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.BORDER | SWT.CLOSE | SWT.CENTER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 6;
		layout.marginLeft = 10;
		layout.marginTop = 8;

		shell.setLayout(layout);
		shell.setSize(WIDTH, HEIGHT);
		shell.setText(title);

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

	}

	private void createControl() {
		GridData gdLabel = new GridData();
		gdLabel.horizontalSpan = 5;
		gdLabel.widthHint = 325;

		lsname = new Label(shell, SWT.NONE);
		lsname.setText("Thread:");

		name = new Text(shell, SWT.SINGLE | SWT.BORDER);
		name.setLayoutData(gdLabel);

		lsTemplate = new Label(shell, SWT.NONE);
		lsTemplate.setText("Template:");

		cTemplate = new Combo(shell, SWT.SINGLE | SWT.BORDER);
		cTemplate.add("SignalTransformation");
		cTemplate.add("ESCCommunication");
		cTemplate.add("ServoCommunication");
		cTemplate.setLayoutData(gdLabel);

		lsPeriodic = new Label(shell, SWT.NONE);
		lsPeriodic.setText("Period (ms):");

		btPeriodic = new Button(shell, SWT.CHECK);
		GridData playout = new GridData();
		playout.minimumWidth = btPeriodic.getSize().x;
		btPeriodic.setSelection(periodic);
		btPeriodic.setEnabled(false);

		lsPeriod = new Label(shell, SWT.NONE);
		lsPeriod.setText("Period:");

		sPeriod = new Spinner(shell, SWT.BORDER);
		sPeriod.setMinimum(0);
		sPeriod.setEnabled(false);

		if (periodic)
			sPeriod.setEnabled(true);

		lsPriority = new Label(shell, SWT.NONE);
		lsPriority.setText("Priority:");

		sPriority = new Spinner(shell, SWT.BORDER);
		sPriority.setMinimum(0);
		// sPriority.setLayoutData(gdLabel);

		GridData gdTabFolder = new GridData();
		gdTabFolder.horizontalSpan = 6;
		gdTabFolder.widthHint = 415;
		gdTabFolder.heightHint = 300;
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(gdTabFolder);
		tabFolder.setSize(400, 200);

		TabItem tab0 = new TabItem(tabFolder, SWT.NONE);
		tab0.setText("Actuators");
		tab0.setControl(getTabActuatorsControl(tabFolder));

		TabItem tab1 = new TabItem(tabFolder, SWT.NONE);
		tab1.setText("Functions");
		tab1.setControl(getTabFunctionsControl(tabFolder));

		GridData blayout = new GridData();
		blayout.widthHint = 80;

		ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setLayoutData(blayout);
		ok.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// int i;
				confirm = true;

				txtName = name.getText();

				functions.clear();

				txtTemplate = cTemplate.getText();

				priority = sPriority.getSelection();
				period = sPeriod.getSelection();

				//System.out.println(threadActuators.size());
				//System.out.println(actuators.size());
				
				System.out.println(threadFunctions.size());
				System.out.println(functions.size());

				shell.close();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		GridData b2layout = new GridData();
		b2layout.horizontalSpan = 2;
		b2layout.widthHint = 80;

		cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setLayoutData(b2layout);
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

	private Control getTabFunctionsControl(TabFolder tabFolder2) {
		// Create a composite and add the components to the sensor specification
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout folder = new GridLayout();
		folder.numColumns = 2;
		composite.setLayout(folder);

		// ---------------------- Table ---------------------------
		tableFunctions = new Table(composite, SWT.BORDER);
		tableFunctions.setLinesVisible(true);
		tableFunctions.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.verticalSpan = 2;

		tableFunctions.setLayoutData(data);

		final TableColumn column = new TableColumn(tableFunctions, SWT.NONE);
		column.setText("Functions");
		column.setWidth(165);

		// ---------------------- Table ---------------------------
		tableThreadFunctions = new Table(composite, SWT.BORDER);
		tableThreadFunctions.setLinesVisible(true);
		tableThreadFunctions.setHeaderVisible(true);

		tableThreadFunctions.setLayoutData(data);

		final TableColumn columnOut = new TableColumn(tableThreadFunctions, SWT.NONE);
		columnOut.setText("Thread Functions");
		columnOut.setWidth(165);

		// ------------Buttons------------
		Button remove = new Button(composite, SWT.PUSH);
		remove.setText("<<");
		GridData bt = new GridData();
		bt.horizontalAlignment = SWT.RIGHT;
		remove.setLayoutData(bt);
		remove.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableThreadFunctions.getSelectionIndex() > -1) {
					TableItem item = new TableItem(tableFunctions, SWT.NONE);
					item.setText(0, tableThreadFunctions.getItem(tableThreadFunctions.getSelectionIndex()).getText(0));
					for (int i = 0; i < threadFunctions.size(); i++) {
						if (threadFunctions.get(i).getName().equals(
								tableThreadFunctions.getItem(tableThreadFunctions.getSelectionIndex()).getText(0))) {
							functions.add(threadFunctions.get(i));
							threadFunctions.remove(i);
						}
					}

					tableThreadFunctions.remove(tableThreadFunctions.getSelectionIndex());
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
				if (tableFunctions.getSelectionIndex() > -1) {
					TableItem itemADD = new TableItem(tableThreadFunctions, SWT.NONE);
					itemADD.setText(0, tableFunctions.getItem(tableFunctions.getSelectionIndex()).getText(0));
					for (int i = 0; i < functions.size(); i++) {
						if (functions.get(i).getName()
								.equals(tableFunctions.getItem(tableFunctions.getSelectionIndex()).getText(0))) {
							threadFunctions.add(functions.get(i));
							functions.remove(i);
						}
					}

					tableFunctions.remove(tableFunctions.getSelectionIndex());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		return composite;
	}

	private Control getTabActuatorsControl(TabFolder tabFolder) {
		// Create a composite and add the components to the sensor specification
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout folder = new GridLayout();
		folder.numColumns = 2;
		composite.setLayout(folder);

		// ---------------------- Table ---------------------------
		tableActuators = new Table(composite, SWT.BORDER);
		tableActuators.setLinesVisible(true);
		tableActuators.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.verticalSpan = 2;

		tableActuators.setLayoutData(data);

		final TableColumn column = new TableColumn(tableActuators, SWT.NONE);
		column.setText("Actuators");
		column.setWidth(165);

		// ---------------------- Table ---------------------------
		tableThreadActuators = new Table(composite, SWT.BORDER);
		tableThreadActuators.setLinesVisible(true);
		tableThreadActuators.setHeaderVisible(true);

		tableThreadActuators.setLayoutData(data);

		final TableColumn columnOut = new TableColumn(tableThreadActuators, SWT.NONE);
		columnOut.setText("Thread Actuators");
		columnOut.setWidth(165);

		// ------------Buttons------------
		Button remove = new Button(composite, SWT.PUSH);
		remove.setText("<<");
		GridData bt = new GridData();
		bt.horizontalAlignment = SWT.RIGHT;
		remove.setLayoutData(bt);
		remove.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableThreadActuators.getSelectionIndex() > -1) {
					TableItem item = new TableItem(tableActuators, SWT.NONE);
					item.setText(0, tableThreadActuators.getItem(tableThreadActuators.getSelectionIndex()).getText(0));
					for (int i = 0; i < threadActuators.size(); i++) {
						if (threadActuators
								.get(i).getName().equals(tableThreadActuators
										.getItem(tableThreadActuators.getSelectionIndex()).getText(0))
								&& threadActuators.get(i).isPeriodic() == periodic) {
							actuators.add(threadActuators.get(i));
							threadActuators.remove(i);
						}
					}

					tableThreadActuators.remove(tableThreadActuators.getSelectionIndex());
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
				if (tableActuators.getSelectionIndex() > -1) {
					TableItem itemADD = new TableItem(tableThreadActuators, SWT.NONE);
					itemADD.setText(0, tableActuators.getItem(tableActuators.getSelectionIndex()).getText(0));
					for (int i = 0; i < actuators.size(); i++) {
						if (actuators.get(i).getName()
								.equals(tableActuators.getItem(tableActuators.getSelectionIndex()).getText(0))
								&& actuators.get(i).isPeriodic() == periodic) {
							threadActuators.add(actuators.get(i));
							actuators.remove(i);
						}
					}

					tableActuators.remove(tableActuators.getSelectionIndex());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return composite;
	}

	private void init(ArrayList<Actuator> inputs, ArrayList<ActuationFunction> iFunctions) {
		if (inputs.size() > 0) {
			for (int i = 0; i < inputs.size(); i++) {
				if (inputs.get(i).isPeriodic() == periodic) {
					TableItem item = new TableItem(tableActuators, SWT.NONE);
					item.setText(inputs.get(i).getName());
					actuators.add(inputs.get(i));
				}
			}
		}

		if (iFunctions.size() > 0) {
			for (int i = 0; i < iFunctions.size(); i++) {
				TableItem item = new TableItem(tableFunctions, SWT.NONE);
				item.setText(iFunctions.get(i).getName());
				functions.add(iFunctions.get(i));
			}
		}
	}

	private void init(ArrayList<Actuator> inputs, AADLThread thread, ArrayList<ActuationFunction> iFunctions) {
		init(inputs, iFunctions);
		name.setText(thread.getName());
		for (int i = 0; i < cTemplate.getItemCount(); i++) {
			if (cTemplate.getItem(i).equals(thread.getTemplate()))
				cTemplate.select(i);
		}

		btPeriodic.setSelection(thread.isPeriodic());

		sPeriod.setSelection(thread.getPeriod());

		sPriority.setSelection(thread.getPriority());

		for (int i = 0; i < thread.getActuators().size(); i++) {
			TableItem item = new TableItem(tableThreadActuators, SWT.NONE);
			item.setText(0, thread.getActuators().get(i).getName());
			threadActuators.add(thread.getActuators().get(i));
		}
		
		for (int i = 0; i < thread.getFunctions().size(); i++) {
			TableItem item = new TableItem(tableThreadFunctions, SWT.NONE);
			item.setText(0, thread.getFunctions().get(i).getName());
			threadFunctions.add(thread.getFunctions().get(i));
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

	public String geTemplate() {
		return txtTemplate;
	}

	public ArrayList<Actuator> getActuators() {
		return actuators;
	}

	public ArrayList<Actuator> getThreadActuators() {
		return threadActuators;
	}

	public ArrayList<ActuationFunction> getFunctions() {
		return functions;
	}

	public ArrayList<ActuationFunction> getThreadFunctions() {
		return threadFunctions;
	}

	
	public int getPriority() {
		return priority;
	}

	public int getPeriod() {
		return period;
	}

}