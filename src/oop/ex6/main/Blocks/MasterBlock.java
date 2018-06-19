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

	private MasterBlock(){
		super();
		funcs = new ArrayList<FunctionWrapper>();
		variables = new ArrayList<VariableWrapper>();
		runner = new LinesRunner(lines);

	}

	public ArrayList<FunctionWrapper> getFuncs() {
		return funcs;
	}

	/**
	 * sets the lines if the master block.
	 * @param lines array of lines.
	 */
	public void setLines(String[] lines){
		this.lines =  lines;
		runner.setLines(lines);
	}

	//TODO

	/*
	initializes lists of all the global methods and variables
	 */
	public void getGlobalDataMembers()
			throws IllegalLineException {
		int openBraces = 0;
		String line = runner.GetNextLine();
		while (line != null) {

			if (openBraces == 0) {
				checkGlobalLine(line);
			}
			if (line.endsWith("{")) {
				openBraces += 1;
			} else if (line.endsWith("}")) {
				openBraces -= 1;
			}
			line = runner.GetNextLine();
		}
		System.out.println(this.funcs);
	}

	/*
	checks a line for global variables and methods
	 */
	private void checkGlobalLine(String line) throws IllegalLineException {

		Matcher funcLineStart = Regex.funcLineStartPattern.matcher(line);
		Matcher varLineStart = Regex.varLinePattern.matcher(line);
		if (funcLineStart.lookingAt()) {
			this.funcs.add(lineToFuncObj(line));
		} else if (varLineStart.lookingAt()) {
			this.variables.add(lineToVarObj(line));
		}
	}

	/*
	gets a line of a variable deceleration, and returns a variable object. //TODO maybe dvir already did this.
	 */
	private static VariableWrapper lineToVarObj(String line) throws IllegalLineException {
		Matcher varTemplateM = Regex.VARIABLE_TEMPLATE.matcher(line);
		if (!varTemplateM.matches()) {
			throw new IllegalLineException(ERROR_START + runner.GetLineNumber() + VAR_ERROR);
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
			throw new IllegalLineException(ERROR_START + runner.GetLineNumber() + FUNC_DEC_ERROR); //TODO add excewption massege with line.
		}
		Matcher typesMatcher = Regex.typePattern.matcher(line);
		ArrayList<VariableWrapper> params = new ArrayList<VariableWrapper>();
		Matcher paramName = Regex.varNamePattern.matcher(line);
		String brackets = getBracketsString(line);
		if (brackets.matches(".*\\s*,\\s*\\)\\s*")) {
			throw new IllegalLineException(ERROR_START + runner.GetLineNumber() + FUNC_DEC_ERROR);
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

