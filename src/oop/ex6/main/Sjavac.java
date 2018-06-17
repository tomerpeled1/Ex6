package oop.ex6.main;

import oop.ex6.main.Blocks.MasterBlock;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Sjavac {


	/*
	returns an array of all lines in file, as Strings
	 */
	private static String[] getLines(String path) {
		return null;
	}

	public static void main(String[] args) throws IllegalLineException {

		//TODO check args

//		String[] lines = getLines(args[0]);
		String[] lines = new String[] {"void tomer( int j){"}; //todo delete
		MasterBlock master = MasterBlock.getInstance();
		ArrayList<FunctionWrapper> funcs = new ArrayList<FunctionWrapper>();
		ArrayList<VariableWrapper> globalVars = new ArrayList<VariableWrapper>();

		getGlobalDataMembers(lines, funcs, globalVars);
		System.out.println(funcs);

	}

	/*
	initializes lists of all the global methods and variables
	 */
	private static void getGlobalDataMembers(String[] lines, ArrayList<FunctionWrapper> funcs,
	                                         ArrayList<VariableWrapper> globalVars)
			throws IllegalLineException {
		int openBraces = 0;
		for (String line : lines) {
			if (openBraces == 0) {
				checkGlobalLine(funcs, globalVars, line);
			}
			if (line.endsWith("{")) {
				openBraces += 1;
			} else if (line.endsWith("}")) {
				openBraces -= 1;
			}
		}
	}

	/*
	checks a line for global variables and methods
	 */
	private static void checkGlobalLine(ArrayList<FunctionWrapper> funcs,
	                                    ArrayList<VariableWrapper> vars, String line) throws IllegalLineException {

		Matcher funcLineStart = Regex.funcLineStartPattern.matcher(line);
		Matcher varLineStart = Regex.varLineStartPattern.matcher(line);
		if (funcLineStart.lookingAt()) {
			funcs.add(lineToFuncObj(line));
		} else if (varLineStart.lookingAt()) {
			vars.add(lineToVarObj(line));
		}
	}

	/*
	gets a line of a variable deceleration, and returns a variable object.
	 */
	private static VariableWrapper lineToVarObj(String line) {
		return null;
	}

	/*
	gets a line of a function deceleration, and returns a function wrapper object.
	 */
	private static FunctionWrapper lineToFuncObj(String line) throws IllegalLineException {
		Matcher format = Regex.FUNCTION_TEMPLATE.matcher(line);
		if (!format.matches()) throw new IllegalLineException(""); //TODO add excewption massege with line.
		Matcher typesMatcher = Regex.typePattern.matcher(line);
		ArrayList<VariableWrapper> params = new ArrayList<VariableWrapper>();
		Matcher paramName = Regex.varNamePattern.matcher(line);
		Matcher bracketsM = Regex.bracketsPattern.matcher(line);
		boolean j = bracketsM.find(); // always true
		String brackets = line.substring(bracketsM.start(),
				bracketsM.end());
		String[] typesAndVals = brackets.split(",");
		int i = 0;
		while (typesMatcher.find()) {
			String[] curValAndType = typesAndVals[i].split("[ ]+");
			String curParamName;
			if (curValAndType[1].endsWith(")")){
				curParamName = curValAndType[1].substring(0,curValAndType[1].length()-1);
			} else {
				curParamName = curValAndType[1];
			}
			int start = typesMatcher.start(), end = typesMatcher.end();
			params.add(new VariableWrapper(line.substring(start, end), true, curParamName));
		}
		Matcher name = Regex.funcNamePattern.matcher(line);
		name.find();
		name.find(); //twice to skip "void".
		String funcName = line.substring(name.start(), name.end());
		return new FunctionWrapper(params, funcName);
	}
}