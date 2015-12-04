package ecpsmodeling.parser;

import java.util.ArrayList;

public class Actuation {
	protected ArrayList<String> inputs;
	protected ArrayList<String> outputs;
	
	protected int Sampling;
	protected int index;
	
	protected String name;

	Actuation(){
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
		return Sampling;
	}

	public void setSampling(int sampling) {
		Sampling = sampling;
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
