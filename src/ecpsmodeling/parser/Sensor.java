package ecpsmodeling.parser;

import java.util.ArrayList;

public class Sensor {
	protected ArrayList<String> inputs;
	protected ArrayList<String> outputs;
	
	protected int sampling;
	protected int priority;
	protected int index;
		
	protected String protocol;
	protected String name;

	Sensor(){
		inputs = new ArrayList<>();
		outputs = new ArrayList<>();
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
	
}
