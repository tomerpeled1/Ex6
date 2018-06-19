package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;
import oop.ex6.main.VariableWrapper;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class MasterBlock extends CodeBlock {


	private static final String ERROR_START = "Error in line ";
	private static final String VAR_ERROR = " variable deceleration is done poorly.";
	private static final String FUNC_DEC_ERROR = " function deceleration is done poorly.";

	private ArrayList<FunctionWrapper> funcs;

	private static MasterBlock ourInstance = new MasterBlock();
	private String[] lines;

	public static MasterBlock getInstance() {
		return ourInstance;
	}

	private MasterBlock() {
		super();
		funcs = new ArrayList<FunctionWrapper>();
		variables = new ArrayList<VariableWrapper>();
		runner = null;

	}

	public ArrayList<FunctionWrapper> getFuncs() {
		return funcs;
	}

	/**
	 * sets the lines if the master block.
	 *
	 * @param lines array of lines.
	 */
	public void setLines(String[] lines) {
		this.lines = lines;
		runner = new LinesRunner(lines);
	}

	//TODO

	/**
	 * Initiliazes the the functions and global variables of the master block.
	 *
	 * @throws IllegalLineException If
	 */
	public void getGlobalDataMembers()
			throws IllegalLineException {
		int openBraces = 0;
		String line = runner.getNextLine();
		while (line != null) {
			if (openBraces == 0) {
				checkGlobalLine(line);
			}
			if (line.endsWith("{")) {
				openBraces += 1;
			} else if (line.endsWith("}")) {
				openBraces -= 1;
			}
			line = runner.getNextLine();
		}
	}

	/*
	checks a line for global variables and methods
	 */
	private void checkGlobalLine(String line) throws IllegalLineException {

		Matcher funcLineStart = Regex.funcLineStartPattern.matcher(line);
		Matcher varLineStart = Regex.varLinePattern.matcher(line);
		if (funcLineStart.matches()) {
			this.funcs.add(lineToFuncObj(line));
		} else if (varLineStart.matches()) {
			this.variables.addAll(lineToVarObj(line));
		}

	}

	/*
	gets a line of a function deceleration, and returns a function wrapper object.
	 */
	private static FunctionWrapper lineToFuncObj(String line) throws IllegalLineException {
		Matcher format = Regex.FUNCTION_TEMPLATE.matcher(line);
		if (!format.matches()) {
			throw new IllegalLineException(ERROR_START + runner.getLineNumber() + FUNC_DEC_ERROR);
		}
		Matcher typesMatcher = Regex.typePattern.matcher(line);
		ArrayList<VariableWrapper> params = new ArrayList<VariableWrapper>();
		Matcher paramName = Regex.varNamePattern.matcher(line);
		String brackets = getBracketsString(line);
		if (brackets.matches(".*\\s*,\\s*\\)\\s*")) {
			throw new IllegalLineException(ERROR_START + runner.getLineNumber() + FUNC_DEC_ERROR);
		}
		String[] typesAndVals = brackets.split(",");
		int i = 0;
		while (typesMatcher.find()) {
			initParam(params, typesAndVals[i]);
			i++;
		}
		Matcher name = Regex.funcNamePattern.matcher(line);
		name.find();
		name.find(); //twice to skip "void".
		String funcName = line.substring(name.start(), name.end());
		if (funcName.equals("void")) {
			throw new IllegalLineException("error in line " + runner.getLineNumber() +
					", function can't be named void");
		}
		return new FunctionWrapper(params, funcName);
	}


	/**
	 * is used to initialize a VariableWrapper object from a code chunk, and add it to the list it gets.
	 *
	 * @param params the list of VariableWrapper objects we want to add the current parameter to.
	 * @param s      the string from which we want to create the Variable. will be in a format of a
	 *               variable deceleration.
	 */
	private static void initParam(ArrayList<VariableWrapper> params, String s) {

		String[] temp = s.split("\\s+");
		for (int j = 0; j < temp.length; j++) {
			temp[j] = temp[j].replaceAll("\\(", "");
		}
		switch (temp.length) {
			case 2:
				params.add(new VariableWrapper(temp[0], true, temp[1], false));
				break;
			case 3:
				params.add(new VariableWrapper(temp[1], true, temp[2], true));
				break;
		}

//		String[] curValAndType = s.split("\\s+");
//		String curParamName;
//		if (curValAndType[0].equals("(")) {
//			curParamName = getVarName(curValAndType[2]);
//		} else {
//			curParamName = getVarName(curValAndType[1]);
//		}
//		int start = typesMatcher.start(), end = typesMatcher.end();
//		params.add(new VariableWrapper(line.substring(start, end), true, curParamName));
//		i++;
//		return i;
	}

	@Override
	public void run() throws IllegalLineException {
		String line = runner.getNextLine();
		while (line != null) {
			if (line.startsWith("//")){
				line = runner.getNextLine();
				continue;
			}
			line = line.replaceFirst("\\s*", "");
			String[] words = line.split("\\s+");
			Matcher startWithType = Regex.typePattern.matcher(line);
			if (startWithType.lookingAt()) {
				continue;
			} else if (getVariableIfExists(words[0]) != null) {

			} else if (words[0].equals("void")) {

			}
		}
		return;
	}

//	private static String getVarName(String s) { TODO clean here at the end if needed.
//		String curParamName;
//		if (s.endsWith(")")) {
//			curParamName = s.substring(0, s.length() - 1);
//		} else {
//			curParamName = s;
//		}
//		return curParamName;
//	}
}

