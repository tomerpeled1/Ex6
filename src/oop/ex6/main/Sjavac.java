package oop.ex6.main;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.VariableWrapper;
import oop.ex6.main.Blocks.MasterBlock;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Sjavac {


	private static String[] getLines(String path) {
		return null;
	}

	public static void main(String[] args) {

		//TODO check args

		String[] lines = getLines(args[0]);
		MasterBlock master = MasterBlock.getInstance();
		ArrayList<FunctionWrapper> funcs = null;
		ArrayList<VariableWrapper> globalVars = null;

		getGlobalDataMembers(lines, funcs, globalVars);

	}

	private static void getGlobalDataMembers(String[] lines, ArrayList<FunctionWrapper> funcs, ArrayList<VariableWrapper> globalVars) {
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

	private static void checkGlobalLine(ArrayList<FunctionWrapper> funcs,
	                                    ArrayList<VariableWrapper> vars, String line) throws IllegalLineException {

		Matcher funcLineStart = Regex.lineStartPattern.matcher(line);
		if (funcLineStart.lookingAt()) {
			funcs.add(lineToFuncObj(line));
		} else if () {
			vars.add(lineToVarObj(line));
		}
	}

	private static VariableWrapper lineToVarObj(String line) {
		return null;
	}

	private static FunctionWrapper lineToFuncObj(String line) throws IllegalLineException {
		Matcher format = Regex.FUNCTION_TEMPLATE.matcher(line);
		if (!format.matches()) throw  new IllegalLineException(""); //TODO add excewption massege with line.
		Matcher typesMatcher = Regex.typePattern.matcher(line);
		ArrayList<VariableWrapper> params = null;
		Matcher paramName = Regex.varNamePattern.matcher(line);
		Matcher bracketsM = Regex.bracketsPattern.matcher(line);
		String brackets = line.substring(bracketsM.start(),bracketsM.end());
		String[] typesAndVals = brackets.split(",");
		int i = 0;
		while (typesMatcher.find()){
			String[] curValAndType = typesAndVals[i].split("[ ]+");
			String curParamName = curValAndType[1];
			int start= typesMatcher.start(),end = typesMatcher.end();
			params.add(new VariableWrapper(line.substring(start,end),true,curParamName));
		}
		Matcher name = Regex.funcNamePattern.matcher(line);
		name.find();
		String funcName= line.substring(name.start(),name.end());
		return new FunctionWrapper(params,funcName);
	}
}