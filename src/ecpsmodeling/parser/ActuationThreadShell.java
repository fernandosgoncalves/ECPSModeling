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

	protected Table tableThreadSensors;
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

	public ActuationThreadShell(Display display, ArrayList<Actuator> iinputs, String title, Boolean bperiodic) {
		createShell(display, title);
		
		periodic = bperiodic;
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
	
	public ActuationThreadShell(Display display, ArrayList<Actuator> iinputs, AADLThread thread, String title, Boolean bperiodic) {
		createShell(display, title);
		
		periodic = bperiodic;
		
		createControl();
		init(iinputs, thread);

		shell.open();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
	}

	private void createShell(Display display, String title){
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
		lsPeriodic.setText("Period:");

		btPeriodic = new Button(shell, SWT.CHECK);
		GridData playout = new GridData();
		playout.minimumWidth = btPeriodic.getSize().x;
		btPeriodic.setSelection(periodic);
		btPeriodic.setEnabled(false);
		
		lsPeriod = new Label(shell, SWT.NONE);
		lsPeriod.setText("Period:");
		
		sPeriod = new Spinner(shell, SWT.BORDER);
		sPeriod.setMinimum(0);
		
		if(periodic){
			btPeriodic.setEnabled(true);
			sPeriod.setEnabled(true);
		}else{
			btPeriodic.setEnabled(false);
			sPeriod.setEnabled(false);
		}
				
		lsPriority = new Label(shell, SWT.NONE);
		lsPriority.setText("Priority:");
		
		sPriority = new Spinner(shell, SWT.BORDER);
		sPriority.setMinimum(0);
		//sPriority.setLayoutData(gdLabel);
		
		GridData gdTabFolder = new GridData();
		gdTabFolder.horizontalSpan = 6;
		gdTabFolder.widthHint = 415;
		gdTabFolder.heightHint = 300;
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(gdTabFolder);
		tabFolder.setSize(400, 200);

		TabItem tab0 = new TabItem(tabFolder, SWT.NONE);
		tab0.setText("Actuators");
		tab0.setControl(getTabSensorsControl(tabFolder));
		
		TabItem tab1 = new TabItem(tabFolder, SWT.NONE);
		tab1.setText("Functions");
		//tab1.setControl(getTabSensorsControl(tabFolder));
		
		GridData blayout = new GridData();
		blayout.widthHint = 80;

		ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setLayoutData(blayout);
		ok.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//int i;
				confirm = true;

				/*txtName = name.getText();
				functions.clear();

				for (i = 0; i < tableFunctions.getItemCount(); i++) {
					functions.add(tableFunctions.getItem(i).getText(0));
				}

				for (i = 0; i < tableSensors.getItemCount(); i++) {
					sensors.add(tableSensors.getItem(i).getText(0));
				}

				for (i = 0; i < tableThreadSensors.getItemCount(); i++) {
					threadSensors.add(tableThreadSensors.getItem(i).getText(0));
				}

				txtTemplate = cTemplate.getText();*/
				
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

	private Control getTabSensorsControl(TabFolder tabFolder) {
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
		tableThreadSensors = new Table(composite, SWT.BORDER);
		tableThreadSensors.setLinesVisible(true);
		tableThreadSensors.setHeaderVisible(true);

		tableThreadSensors.setLayoutData(data);

		final TableColumn columnOut = new TableColumn(tableThreadSensors, SWT.NONE);
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
				if (tableThreadSensors.getSelectionIndex() > -1) {
					TableItem item = new TableItem(tableActuators, SWT.NONE);
					item.setText(0, tableThreadSensors.getItem(tableThreadSensors.getSelectionIndex()).getText(0));
					tableThreadSensors.remove(tableThreadSensors.getSelectionIndex());
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
					TableItem itemADD = new TableItem(tableThreadSensors, SWT.NONE);
					itemADD.setText(0, tableActuators.getItem(tableActuators.getSelectionIndex()).getText(0));
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

	private void init(ArrayList<Actuator> inputs) {
		threadActuators = new ArrayList();
		actuators = new ArrayList();
		functions = new ArrayList();
		
		for (int i = 0; i < inputs.size(); i++) {
			TableItem item = new TableItem(tableActuators, SWT.NONE);
			item.setText(inputs.get(i).getName());
		}
	}

	private void init(ArrayList<Actuator> inputs, AADLThread thread) {
		init(inputs);
		name.setText(thread.getName());
		/*for(int i = 0; i < cTemplate.getItemCount(); i++){
			if(cTemplate.getItem(i).equals(thread.getTemplate()))
				cTemplate.select(i);
		}*/		
		
		btPeriodic.setSelection(thread.isPeriodic());
						
		
		/*for (int i = 0; i < thread.getSensors().size(); i++) {
			TableItem item = new TableItem(tableThreadSensors, SWT.NONE);
			item.setText(0, thread.getSensors().get(i).getName());
		}*/
		
		for (int i = 0; i < inputs.size(); i++) {
			TableItem item = new TableItem(tableActuators, SWT.NONE);
			item.setText(inputs.get(i).getName());
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

	public int getPriority() {
		return priority;
	}

	public int getPeriod() {
		return period;
	}
	
}