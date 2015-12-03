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

public class AddSubsystemShell {

	final Shell shell;
	TabFolder tabFolder;

	protected boolean confirm = false;

	protected ArrayList<String> inputs;

	protected String txtName;

	protected Text name;

	protected Label lsname;

	protected Button cancel;
	protected Button ok;

	protected Table tableInput;
	protected Table tableSubsys;
	protected Table tableOutput;

	public AddSubsystemShell(Display display, ArrayList<String> inputs) {
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

		createControl();
		init(inputs);

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

		TabItem inputs = new TabItem(tabFolder, SWT.NONE);
		inputs.setText("Inputs");
		inputs.setControl(getTabInputsControl(tabFolder));

		TabItem outputs = new TabItem(tabFolder, SWT.NONE);
		outputs.setText("Outputs");
		outputs.setToolTipText("This is tab two");
		outputs.setControl(getTabOutputsControl(tabFolder));

		GridData blayout = new GridData();
		blayout.widthHint = 80;

		ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setLayoutData(blayout);
		ok.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				confirm = true;

				txtName = name.getText();

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
		tableOutput = new Table(composite, SWT.BORDER);
		tableOutput.setLinesVisible(true);
		tableOutput.setHeaderVisible(true);

		tableOutput.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn columnOut = new TableColumn(tableOutput, SWT.NONE);
		columnOut.setText("Subsystem Inputs");
		columnOut.setWidth(150);

		// ------------Buttons------------
		Button remove = new Button(composite, SWT.PUSH);
		remove.setText("<<");
		GridData bt = new GridData();
		bt.horizontalAlignment = SWT.RIGHT;
		remove.setLayoutData(bt);

		Button add = new Button(composite, SWT.PUSH);
		add.setText(">>");

		return composite;
	}

	private Control getTabOutputsControl(TabFolder tabFolder) {
		// Create a composite and add four buttons to it
		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout folder = new GridLayout();
		folder.numColumns = 2;
		composite.setLayout(folder);

		// ---------------------- Table ---------------------------
		tableOutput = new Table(composite, SWT.BORDER);
		tableOutput.setLinesVisible(true);
		tableOutput.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.verticalSpan = 3;

		tableOutput.setLayoutData(data);
		// table.setSize(290, 100);

		final TableColumn column = new TableColumn(tableOutput, SWT.NONE);
		column.setText("Outputs");
		column.setWidth(150);

		// ------------Buttons------------
		GridData bt = new GridData();
		bt.widthHint = 70;
		bt.verticalAlignment = SWT.TOP;
		Button addOutput = new Button(composite, SWT.PUSH);
		addOutput.setText("Add");
		addOutput.setLayoutData(bt);
		
		Button editOutput = new Button(composite, SWT.PUSH);
		editOutput.setText("Edit");
		editOutput.setLayoutData(bt);
		
		Button removeOutput = new Button(composite, SWT.PUSH);
		removeOutput.setText("Remove");
		removeOutput.setLayoutData(bt);
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

	public String getName() {
		return txtName;
	}

	public boolean isConfirm() {
		return confirm;
	}
}
