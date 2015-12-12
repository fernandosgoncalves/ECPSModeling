package ecpsmodeling.parser;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

public class EditActuatorShell {
	protected boolean confirm = false;
	
	final Shell shell;
	
	protected String txtActuator;
	protected String txtProtocol;
	protected String txtPriority;
	protected String txtPeriod;
	protected String txtSignal;
	
	protected Boolean periodic;
	
	protected Spinner priority;
	protected Spinner period;
	
	protected Text signal;
	
	protected Combo protocol;
	protected Combo actuator;
	
	protected Label lactuator;
	protected Label lperiodic;
	protected Label lpriority;
	protected Label lprotocol;
	protected Label lperiod;
	protected Label lsignal;

	protected Button bperiodic;
	protected Button cancel;
	protected Button ok;
		
	public EditActuatorShell(TableItem item, Display display) {
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.BORDER | SWT.CLOSE | SWT.CENTER);

		GridLayout layout = new GridLayout();		
		layout.numColumns = 2;
		layout.marginLeft = 10;
		layout.marginTop = 8;
		
		shell.setLayout(layout);
		shell.setSize(290, 270);
		shell.setText("Actuator Specification");
		
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		createControl();
		init(item);
		
		shell.open();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
	}

	private void createControl(){
		GridData ilayout = new GridData();
		ilayout.widthHint = 150;
		
		lsignal = new Label(shell, SWT.NONE);
		lsignal.setText("Signal:");
		
		signal = new Text(shell, SWT.SINGLE | SWT.BORDER);
		signal.setLayoutData(ilayout);
		signal.setEnabled(false);
		
		lactuator = new Label(shell, SWT.NONE);
		lactuator.setText("Actuator:");
		
		actuator = new Combo(shell, SWT.SINGLE | SWT.BORDER);
		actuator.setItems(new String[] {"ESC", "Servo", "Motor"});
		actuator.setLayoutData(ilayout);		
						
		lprotocol = new Label(shell, SWT.NONE);
		lprotocol.setText("Protocol:");
		
		protocol = new Combo(shell, SWT.NONE);
		String[] items = {"I2C", "SPI", "Serial", "PWM"};
		protocol.setItems(items);
		protocol.setLayoutData(ilayout);
		
		lpriority = new Label(shell, SWT.NONE);
		lpriority.setText("Priority:");
		
		priority = new Spinner(shell, SWT.BORDER);
		priority.setLayoutData(ilayout);
		
		lperiodic = new Label(shell, SWT.NONE);
		lperiodic.setText("Periodic:");

		bperiodic = new Button(shell, SWT.CHECK);
		bperiodic.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(period.isEnabled())
					period.setEnabled(false);
				else
					period.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		lperiod = new Label(shell, SWT.NONE);
		lperiod.setText("Period (ms):");

		period = new Spinner(shell, SWT.BORDER);
		period.setLayoutData(ilayout);
		period.setEnabled(false);
		
		GridData blayout = new GridData();
		blayout.widthHint = 80;
		
		ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setLayoutData(blayout);
		ok.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				confirm = true;
				
				txtSignal = signal.getText();
				txtActuator = actuator.getText();
				txtProtocol = protocol.getText();
				txtPriority = priority.getText();
				periodic = bperiodic.getSelection();
				txtPeriod = period.getText();
				
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
	
	private void init(TableItem item) {
		signal.setText(item.getText(0));
		
		if(!item.getText(1).isEmpty()){
			for(int i = 0; i < actuator.getItemCount(); i++){
				if(actuator.getItem(i).equals(item.getText(1)))
					actuator.select(i);
			}
		}

		if(!item.getText(2).isEmpty()){
			for(int i=0; i< protocol.getItemCount(); i++){
				if(protocol.getItem(i).equals(item.getText(2)))
					protocol.select(i);
			}
		}
	
		if(!item.getText(3).isEmpty())
			priority.setSelection(Integer.valueOf(item.getText(3)));
		
		
		Button bt = (Button)item.getData("periodic");
		if(bt.getSelection()){
			bperiodic.setSelection(true);
			period.setEnabled(true);
		}
		
		if(!item.getText(5).isEmpty())
			period.setSelection(Integer.valueOf(item.getText(5)));
		
	}

	public String getPriority() {
		return txtPriority;
	}

	public String getActuator() {
		return txtActuator;
	}

	public String getSignal() {
		return txtSignal;
	}

	public String getProtocol() {
		return txtProtocol;
	}

	public Boolean getPeriodic(){
		return periodic;
	}
	
	public String getPeriod(){
		return txtPeriod;
	}
	
	public boolean isConfirm() {
		return confirm;
	}	
}
