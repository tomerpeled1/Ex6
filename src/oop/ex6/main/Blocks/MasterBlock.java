package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;
import oop.ex6.main.VariableWrapper;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * this class represents the global scope of the sjava file.
 */
public class MasterBlock extends CodeBlock {


	public static final String FUNC_NAME_DUPLICATION = ", two functions can't have the same name";
	public static final String FUNC_DEC = "void";
	public static final String FUNCTION_CANT_BE_NAMED_VOID = ", function can't be named void";
	private static final String VAR_DUPLICATION = ": cant assign two " +
			"variables in the same name";
	private ArrayList<FunctionDefBlock> funcs;


	/**
	 * @return the lines of the sjava file
	 */
	String[] getLines() {
		return lines;
	}

	private String[] lines;


	public static MasterBlock getInstance() { //TODO delete
		return new MasterBlock();
	}

	/**
	 * creates a new master block with empty functions and variables lists.
	 */
	public MasterBlock() {
		super();
		funcs = new ArrayList<FunctionDefBlock>();
		variables = new ArrayList<VariableWrapper>();

	}

	/**
	 * @return functions of the program.
	 */
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
	}


	/**
	 * Initiliazes the the functions and global variables of the master block.
	 *
	 * @throws IllegalLineException If
	 */
	private void getGlobalDataMembers()
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
		if (openBraces != 0) {
			throw new IllegalLineException(OPEN_BLOCK_AT_END_ERROR);
		}
	}

	/*
	checks a  single line
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
			declarationLineToVarObj(line, lineNum);
		} else if (methodDec.lookingAt()) { // method declaration line
			FunctionWrapper wrapper = lineToFuncObj(line, lineNum);
			FunctionDefBlock functionDefBlock = new FunctionDefBlock(wrapper, this, lineNum + 1);
			this.funcs.add(functionDefBlock);
		} else if (getVariableIfExists(words[0]) != null) { //assign value to already initialized variable
			assignmentLineHandle(line, lineNum);
		} else { //all other options were checked so the line is illegal
			throw new IllegalLineException(ERROR_START + lineNum +
					ILLEGAL_LINE_IN_MAIN_SCOPE);
		}


	}

	/*
	gets a line of a function deceleration, and returns a function wrapper object.
	 */
	private FunctionWrapper lineToFuncObj(String line, int lineNum) throws IllegalLineException {
		Matcher format = Regex.FUNCTION_TEMPLATE.matcher(line);
		if (!format.matches()) {
			throw new IllegalLineException(ERROR_START + lineNum + FUNC_DEC_ERROR);
		}
		Matcher typesMatcher = Regex.typePattern.matcher(line);
		ArrayList<VariableWrapper> params = new ArrayList<VariableWrapper>();
		String brackets = getBracketsString(line);
		if (brackets.matches(".*\\s*,\\s*\\)\\s*")) {
			throw new IllegalLineException(ERROR_START + lineNum + FUNC_DEC_ERROR);
		}
		String[] typesAndVals = brackets.split(",");
		int i = 0;
		while (typesMatcher.find()) {
			initParam(params, typesAndVals[i], lineNum);
			i++;
		}
		Matcher name = Regex.funcNamePattern.matcher(line);
		name.find();
		name.find(); //twice to skip "void".
		String funcName = line.substring(name.start(), name.end());
		if (funcAlreadyExists(funcName)) {
			throw new IllegalLineException(ERROR_START + lineNum +
					FUNC_NAME_DUPLICATION);
		}
		if (funcName.equals(FUNC_DEC)) {
			throw new IllegalLineException(ERROR_START + lineNum +
					FUNCTION_CANT_BE_NAMED_VOID);
		}
		return new FunctionWrapper(params, funcName);
	}

	private boolean funcAlreadyExists(String funcName) {
		for (FunctionDefBlock func : this.funcs) {
			if (func.getFuncName().equals(funcName)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * is used to initialize a VariableWrapper object from a code chunk, and add it to the list it gets.
	 *
	 * @param params the list of VariableWrapper objects we want to add the current parameter to.
	 * @param s      the string from which we want to create the Variable. will be in a format of a
	 *               variable deceleration.
	 */
	private void initParam(ArrayList<VariableWrapper> params, String s, int lineNum) throws IllegalLineException {
		s = s.trim();
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
		if (checkIfNameTaken(name, params)) {
			throw new IllegalLineException(ERROR_START + lineNum + VAR_DUPLICATION);
		}
		params.add(new VariableWrapper(type, true, name, isFinal));
	}

	/*
	check if the name of a variable already defines another variable.l
	 */
	private boolean checkIfNameTaken(String name, ArrayList<VariableWrapper> params) {
		for (VariableWrapper p : params) {
			if (p.getName().equals(name)) {
				return true;
			}
		}
		for (FunctionDefBlock func :funcs ) {
			if (func.getFuncName().equals(name)) {
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
	}

	@Override
	protected boolean isOnlyGlobalVar(VariableWrapper var) {
		return true;
	}
}

