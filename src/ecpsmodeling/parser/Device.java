package ecpsmodeling.parser;

import java.util.ArrayList;

public class Device {
	protected ArrayList<String> inputs;
	protected ArrayList<String> outputs;

	protected boolean periodic;

	protected int type; // 0 - sensor 1 - Actuator

	protected int sampling;
	protected int priority;
	protected int period;
	protected int index;

	protected String protocol;
	protected String signal;
	protected String name;

	Device(int type) {
		inputs = new ArrayList<>();
		outputs = new ArrayList<>();

		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<String> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<String> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<String> getOutputs() {
		return outputs;
	}

	public void setOutputs(ArrayList<String> outputs) {
		this.outputs = outputs;
	}

	public int getSampling() {
		return sampling;
	}

	public void setSampling(int auxSampling) {
		sampling = auxSampling;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public boolean isPeriodic() {
		return periodic;
	}

	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getSignal() {
		return signal;
	}

	public void setSignal(String signal) {
		this.signal = signal;
	}

}
