package oop.ex6.main;

import java.util.ArrayList;

public class FunctionWrapper {

	private String name;

	public String getName() {
		return name;
	}

	private ArrayList<oop.ex6.main.VariableWrapper> params;

	public ArrayList<VariableWrapper> getParams() {
		return params;
	}

	public FunctionWrapper(ArrayList<oop.ex6.main.VariableWrapper> params, String name){
		this.name = name;

		this.params = params;
	}

	@Override
	public String toString() {
		return ("name = " + name + ", params = " + params);
	}
}
