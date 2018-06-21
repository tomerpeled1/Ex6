package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;
import oop.ex6.main.VariableWrapper;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class MasterBlock extends CodeBlock {


	private ArrayList<FunctionDefBlock> funcs;

	private static MasterBlock ourInstance = new MasterBlock();


	String[] getLines() {
		return lines;
	}

	private String[] lines;

	public static MasterBlock getInstance() {
		return new MasterBlock();
	}

	private MasterBlock() {
		super();
		funcs = new ArrayList<FunctionDefBlock>();
		variables = new ArrayList<VariableWrapper>();
//		runner = null;

	}

	public ArrayList<FunctionDefBlock> getFuncs() {
		return funcs;
	}

	/**
	 * sets the lines if the master block.
	 *
	 * @param lines array of lines.
	 */
	public void setLines(String[] lines) {
		this.lines = lines;
//		runner = new LinesRunner(lines);
	}

	//TODO

	/**
	 * Initiliazes the the functions and global variables of the master block.
	 *
	 * @throws IllegalLineException If
	 */
	public void getGlobalDataMembers() //TODO make private
			throws IllegalLineException {
		int openBraces = 0;
		for (String line : lines) {
			Matcher openScope = Regex.OPEN_BLOCK_PATTERN.matcher(line);
			Matcher closeScope = Regex.CLOSE_BLOCK_PATTERN.matcher(line);
			if (openBraces == 0) {
				checkGlobalLine(line);
			}
			if (openScope.matches()) {
				openBraces += 1;

			} else if (closeScope.matches()) {
				openBraces -= 1;
			}
		}
		if (openBraces!=0){
			throw new IllegalLineException(OPEN_BLOCK_AT_END_ERROR);
		}
	}

	/*
	checks a line
	 */
	private void checkGlobalLine(String line) throws IllegalLineException {

		int lineNum = java.util.Arrays.asList(lines).indexOf(line);
		Matcher emptyLine = Regex.EMPTY_LINE_PATTERN.matcher(line);
		if (emptyLine.matches()) return; //empty line
		if (line.startsWith("//")) return; // comment

		line = line.replaceFirst("\\s*", "");
		String[] words = line.split("\\s+");
		Matcher varDec = Regex.VarDec.matcher(line);
		Matcher methodDec = Regex.funcLineStartPattern.matcher(line);
		if (varDec.matches()) { // variable declaration line
			this.variables.addAll(declarationLineToVarObj(line,lineNum));
		} else if (methodDec.lookingAt()) { // method declaration line
			FunctionWrapper wrapper = lineToFuncObj(line,lineNum);
			FunctionDefBlock functionDefBlock = new FunctionDefBlock(wrapper, this, lineNum+1);
			this.funcs.add(functionDefBlock);
		} else if (getVariableIfExists(words[0]) != null) { //assign value to already initialized variable
			assignmentLineHandle(line,lineNum);
		} else { //all other options were checked so the line is illegal
			throw new IllegalLineException(ERROR_START + lineNum +
					ILLEGAL_LINE_IN_MAIN_SCOPE);
		}


//		Matcher funcLineStart = Regex.funcLineStartPattern.matcher(line);
//		Matcher varLineStart = Regex.varLinePattern.matcher(line);
//		if (funcLineStart.matches()) {
//			this.funcs.add(lineToFuncObj(line));
//		} else if (varLineStart.matches()) {
//			this.variables.addAll(lineToVarObj(line));
//		}

	}

	/*
	gets a line of a function deceleration, and returns a function wrapper object.
	 */
	private static FunctionWrapper lineToFuncObj(String line, int lineNum) throws IllegalLineException {
		Matcher format = Regex.FUNCTION_TEMPLATE.matcher(line);
		if (!format.matches()) {
			throw new IllegalLineException(ERROR_START + lineNum + FUNC_DEC_ERROR);
		}
		Matcher typesMatcher = Regex.typePattern.matcher(line);
		ArrayList<VariableWrapper> params = new ArrayList<VariableWrapper>();
		Matcher paramName = Regex.varNamePattern.matcher(line); //TODO maybe can delete this
		String brackets = getBracketsString(line);
		if (brackets.matches(".*\\s*,\\s*\\)\\s*")) {
			throw new IllegalLineException(ERROR_START + lineNum + FUNC_DEC_ERROR);
		}
		String[] typesAndVals = brackets.split(",");
		int i = 0;
		while (typesMatcher.find()) {
			initParam(params, typesAndVals[i],lineNum);
			i++;
		}
		Matcher name = Regex.funcNamePattern.matcher(line);
		name.find();
		name.find(); //twice to skip "void".
		String funcName = line.substring(name.start(), name.end());
		if (funcName.equals("void")) {
			throw new IllegalLineException("error in line " + lineNum+
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
	private static void initParam(ArrayList<VariableWrapper> params, String s,int lineNum) throws IllegalLineException {

		String[] temp = s.split("\\s+");
		for (int j = 0; j < temp.length; j++) {
			temp[j] = temp[j].replaceAll("\\(|\\)", "");
		}
		boolean isFinal = false;
		String name = null;
		String type = null;
		switch (temp.length) {
			case 2:
				isFinal = false;
				name = temp[1];
				type = temp[0];
				break;
			case 3:
				isFinal = true;
				name = temp[2];
				type = temp[1];
				break;
		}
		if (checkIfNameTaken(name,params)) {
			throw new IllegalLineException(ERROR_START + lineNum + ": cant assign two " +
					"variables in the same name");
		}
		params.add(new VariableWrapper(type, true, name, isFinal));



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

	private static boolean checkIfNameTaken(String name, ArrayList<VariableWrapper> params) {
		for (VariableWrapper p:params) {
			if (p.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() throws IllegalLineException {
			getGlobalDataMembers();
			for (FunctionDefBlock funcDef : funcs) {
				funcDef.run();
			}

//		String line = runner.getNextLine();
//		while (line != null) {
//			Matcher emptyLine = Regex.EMPTY_LINE_PATTERN.matcher(line);
//			if (emptyLine.matches()) continue; //empty line
//			if (line.startsWith("//")) { // comment
//				line = runner.getNextLine();
//				continue;
//			}
//			line = line.replaceFirst("\\s*", "");
//			String[] words = line.split("\\s+");
//			Matcher startWithType = Regex.typePattern.matcher(line);
//			if (startWithType.lookingAt()) { //decleration line, already did this
//				continue;
//			} else if (getVariableIfExists(words[0]) != null) {
//
//			} else if (words[0].equals("void")) {
//
//			}
//		}
//		return;
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

