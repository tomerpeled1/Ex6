package oop.ex6.main;

import java.util.ArrayList;

public class FunctionWrapper {

	private String name;
	private ArrayList<oop.ex6.main.VariableWrapper> params;

	public FunctionWrapper(ArrayList<oop.ex6.main.VariableWrapper> params, String name){
		this.name = name;
		this.params = params;
	}

	@Override
	public String toString() {
		return ("name = " + name + ", params = " + params);
	}
}
