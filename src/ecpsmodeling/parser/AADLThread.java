package ecpsmodeling.parser;

import java.util.ArrayList;

public class AADLThread {
	protected ArrayList<SystemFunction> actFunctions;
	
	protected ArrayList<SystemFunction> senFunctions;
	
	protected ArrayList<Device> actuators;
	
	protected ArrayList<Device> sensors;
		
	protected ArrayList<String> inputs;
	protected ArrayList<String> outputs;
	
	protected boolean periodic;
	
	protected int priority;
	protected int period;
	protected int index;
	
	protected String name;
	protected String tamplate;
		
	AADLThread(){
		actFunctions = new ArrayList<>();
		senFunctions = new ArrayList<>();
		actuators = new ArrayList<>();
		sensors = new ArrayList<>();
		outputs = new ArrayList<>();		
		inputs = new ArrayList<>();
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

	public ArrayList<SystemFunction> getActFunctions() {
		return actFunctions;
	}

	public void setActFunctions(ArrayList<SystemFunction> actFunctions) {
		this.actFunctions = actFunctions;
	}

	public ArrayList<Device> getActuators() {
		return actuators;
	}

	public void setActuators(ArrayList<Device> actuators) {
		this.actuators = actuators;
	}
	
	public ArrayList<SystemFunction> getSenFunctions() {
		return senFunctions;
	}

	public void setSenFunctions(ArrayList<SystemFunction> senFunctions) {
		this.senFunctions = senFunctions;
	}

	public ArrayList<Device> getSensors() {
		return sensors;
	}

	public void setSensors(ArrayList<Device> sensors) {
		this.sensors = sensors;
	}
	
	public void setTemplate(String template){
		this.tamplate = template;
	}

	public String getTemplate(){
		return this.tamplate;
	}
}
