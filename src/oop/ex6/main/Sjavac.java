package oop.ex6.main;

import oop.ex6.main.Blocks.MasterBlock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class Sjavac {

	private final static String IO_ERROR = "IO Error - problem with the file";

	/*
	returns an array of all lines in file, as Strings
	 */
	private static String[] getLines(String path) throws IOException {
		String[] lines;
		Path path1 = Paths.get(path);
		lines = (String[]) Files.readAllLines(path1).toArray();
		return lines;
	}

	public static void main(String[] args){

		//TODO check args
		String[] lines = null;
		try {
			lines = getLines(args[0]);
		} catch (IOException e) {
			System.err.println(IO_ERROR);
			return;
		}
//		lines = new String[]{"void tomer(int j,boolean kk){"}; //todo delete
		MasterBlock master = MasterBlock.getInstance();
		master.setLines(lines);
		try {
			master.getGlobalDataMembers();
		} catch (IllegalLineException e) {
			System.err.println(e.getMessage());
			return;
		}

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
		Matcher varLineStart = Regex.varLinePattern.matcher(line);
		if (funcLineStart.lookingAt()) {
			funcs.add(lineToFuncObj(line));
		} else if (varLineStart.lookingAt()) {
			vars.add(lineToVarObj(line));
		}
	}

	/*
	gets a line of a variable deceleration, and returns a variable object. //TODO maybe dvir already did this.
	 */
	private static VariableWrapper lineToVarObj(String line) throws IllegalLineException {
		Matcher varTemplateM = Regex.VARIABLE_TEMPLATE.matcher(line);
		if (!varTemplateM.matches()) {
			throw new IllegalLineException("OOPS") ;//TODO add line num
		}
		String[] temp = line.split("\\s");
		return null;
	}

	/*
	gets a line of a function deceleration, and returns a function wrapper object.
	 */
	private static FunctionWrapper lineToFuncObj(String line) throws IllegalLineException {
		Matcher format = Regex.FUNCTION_TEMPLATE.matcher(line);
		if (!format.matches()){
			throw new IllegalLineException(""); //TODO add excewption massege with line.
		}
		Matcher typesMatcher = Regex.typePattern.matcher(line);
		ArrayList<VariableWrapper> params = new ArrayList<VariableWrapper>();
		Matcher paramName = Regex.varNamePattern.matcher(line);
		Matcher bracketsM = Regex.bracketsPattern.matcher(line);
		boolean j = bracketsM.find(); // always true
		String brackets = line.substring(bracketsM.start(),
				bracketsM.end());
		if (brackets.matches(".*\\s*,\\s*\\)\\s*")) {
			throw new IllegalLineException("OOPS"); //TODO insert line num
		}
		String[] typesAndVals = brackets.split(",");
		int i = 0;
		while (typesMatcher.find()) {
			i = initParams(line, typesMatcher, params, typesAndVals[i], i);
		}
		Matcher name = Regex.funcNamePattern.matcher(line);
		name.find();
		name.find(); //twice to skip "void".
		String funcName = line.substring(name.start(), name.end());
		return new FunctionWrapper(params, funcName);
	}

	private static int initParams(String line, Matcher typesMatcher, ArrayList<VariableWrapper> params, String typesAndVal, int i) {
		String[] curValAndType = typesAndVal.split("\\s+");
		String curParamName;
		if (curValAndType[0].equals("(")) {
			curParamName = getVarName(curValAndType[2]);
		} else {
			curParamName = getVarName(curValAndType[1]);
		}
		int start = typesMatcher.start(), end = typesMatcher.end();
		params.add(new VariableWrapper(line.substring(start, end), true, curParamName));
		i++;
		return i;
	}

	private static String getVarName(String s) {
		String curParamName;
		if (s.endsWith(")")) {
			curParamName = s.substring(0, s.length() - 1);
		} else {
			curParamName = s;
		}
		return curParamName;
	}
}