package oop.ex6.main;

import java.util.ArrayList;

/**
 * this class represents a function (not a function decleration block!) - it holds the parameters it
 * should get(types and names),and the name of the function.
 */
public class FunctionWrapper {

	private String name;

	/**
	 * @return the name of the function.
	 */
	public String getName() {
		return name;
	}

	private ArrayList<oop.ex6.main.VariableWrapper> params;

	/**
	 *
	 * @return the parameter of the function
	 */
	public ArrayList<VariableWrapper> getParams() {
		return params;
	}

	/**
	 * constructs a FunctionWrapper object.
	 * @param params a list of the parameters the function should get
	 * @param name the name of the function
	 */
	public FunctionWrapper(ArrayList<oop.ex6.main.VariableWrapper> params, String name){
		this.name = name;

		this.params = params;
	}

	@Override
	public String toString() {
		return ("name = " + name + ", params = " + params);
	}
}
