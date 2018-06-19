package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.VariableWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;

import java.util.ArrayList;
import java.util.regex.Matcher;

public abstract class CodeBlock {
	protected ArrayList<VariableWrapper> variables;
	private CodeBlock parent;
	protected static LinesRunner runner;
	private MasterBlock master;


	protected CodeBlock() {
		setMaster();
	}

	/**
	 * sets the master of this block to be the master lock of the file.
	 */
	private void setMaster() {
		if (this instanceof MasterBlock) master = (MasterBlock) this;
		else master = parent.master;
	}

	/**
	 * Constructs a new CodeBlock with a parent block.
	 *
	 * @param parent The block this block is contained in.
	 */
	public CodeBlock(CodeBlock parent) {
		this.parent = parent;
		this.variables = new ArrayList<VariableWrapper>();
		//setMaster();
	}


	public void run() throws IllegalLineException {
		String line = runner.getNextLine();
		Matcher BLOCK_END_MATCHER = Regex.BLOCK_END_PATTERN.matcher(line);
		while (!BLOCK_END_MATCHER.matches()) {
			CodeBlock nextBlock = null;
			if (line.startsWith("//")){
				line = runner.getNextLine();
				continue;
			}

			//TODO when checking the end, get the next line, save it, call the function check end and than run on the next line.

			//TODO check if variable declaration (aka int x = 3), check that it's not in this scope variables(can be in others)

			//TODO check if variable assigment (aka x = 3)

			//TODO check a call to a fucntion, check if function is in the fnctions list and check paramenters

			//TODO check if a declaration of functoin only in master block and than run on it.
			if (checkIfValidBooleanExpression(line)) {
				nextBlock = new BooleanExpressionBlock(this);
			}
			if (nextBlock != null) {
				nextBlock.run();
			}
			line = runner.getNextLine();
		}
	}

	/**
	 * Gets a declaration line and returns the wrappers for the declared variables.
	 * @param line The declaration line for creating variables objects.
	 * @return An ArrayList of VariableWrapper.
	 */
	public ArrayList<VariableWrapper> lineToVarObj(String line) throws IllegalLineException {
		Matcher typeNameMatcher = Regex.typePattern.matcher(line);
		String type = "";
		if (typeNameMatcher.find()) {
			type = line.substring(typeNameMatcher.start(), typeNameMatcher.end());
		}
		ArrayList<VariableWrapper> newVariables = new ArrayList<VariableWrapper>();
		line = line.replaceFirst(Regex.variableTypeCheck, "");
		boolean isFinal = false;
		Matcher finalM = Regex.FINAL_PATTERN.matcher(line);
		if (finalM.lookingAt()){
			line = line.replaceFirst(Regex.FINAL_DEC, "");
			isFinal = true;
		}
		String[] split = line.split("(\\s*,(\\s*)|\\s*;\\s*)");
		for (String var : split) {
			var = var.replaceAll("\\s", "");
			String[] varComponents = var.split("=");
			if (var.indexOf('=') < 0 && isFinal){
				throw new IllegalLineException("error in line " + runner.getLineNumber() + ", final " +
						"variables must be assigned at deceleration");
			}
			if (var.indexOf('=') >= 0 && varComponents.length == 1) {
				throw new IllegalLineException("error in line " + runner.getLineNumber() + ", no given value.");
			}
			if (varComponents.length > 2) {
				throw new IllegalLineException("error in line " + runner.getLineNumber());
			}
			Matcher m = Regex.varNamePattern.matcher(varComponents[0]);
			if (!m.matches() || checkIfBlockVariable(varComponents[0])) {
				throw new IllegalLineException("error in line " + runner.getLineNumber() +
												", can't initialize variable.");
			}
			if (varComponents.length == 2) {
				if (!checkIfValueMatchType(VariableWrapper.stringToTypes(type), varComponents[1])) {
					throw new IllegalLineException("error in line " + runner.getLineNumber() +
							", value doesn't match type.");
				}
				newVariables.add(new VariableWrapper(type, true, varComponents[0],isFinal));
			} else {
				newVariables.add(new VariableWrapper(type, false, varComponents[0],false));
			}
		}
		return newVariables;//if it has no variables in it, it's ok.
	}


	/**
	 * Checks if a line opens a new if or while block.
	 *
	 * @param line The line to check if a opens a block.
	 * @return True if a valid boolean expression, otherwise false.
	 */
	private boolean checkIfValidBooleanExpression(String line) throws IllegalLineException {
		Matcher ifMatcher = Regex.IF_PATTERN.matcher(line);
		Matcher whileMatcher = Regex.WHILE_PATTERN.matcher(line);
		String booleanExpression = null;
		int expressionStart = 0;
		int expressionEnd = 0;
		if (ifMatcher.matches() || whileMatcher.matches()) {
			expressionStart = line.indexOf('(');
			expressionEnd = line.indexOf(')');
			booleanExpression = line.substring(expressionStart, expressionEnd);
			String[] splitExpression = booleanExpression.split(Regex.BOOLEAN_EXPRESSION_SPLIT);
			for (String exp : splitExpression) {
				Matcher doubleMatch = Regex.DOUBLE_PATTERN.matcher(exp);
				if (!(exp.equals("true") || exp.equals("false") || doubleMatch.matches() ||
						checkIfInitializedVariable(exp))) {
					throw new IllegalLineException("error in line " + runner.getLineNumber() + ", boolean " +
							"expression not supported.");
				}
			}
			return true;
		}
		return false;
	}

	private boolean checkIfBlockVariable(String name){
			for (VariableWrapper var : variables) {
				if (var.getName().equals(name)) {
					return true;
				}
			}
			return false;
		}


	/**
	 * Checks if a variable is an initialized variable.
	 * !! can't check if uninitialized variable.
	 *
	 * @param name The name of the variable to check.
	 * @return True if it's initialized, false else.
	 */
	protected boolean checkIfInitializedVariable(String name) {
		VariableWrapper var = getVariableIfExists(name);
		if (var == null) {//not even in the list.
			return false;
		}
		return var.getHasValue();
	}

	/**
	 * Checks if a variable is an uninitialized variable.
	 * !! can't check if initialized variable.
	 *
	 * @param name The name of the variable to check.
	 * @return True if it's uninitialized, false else.
	 */
	protected boolean checkIfUninitializedVariable(String name) {
		VariableWrapper var = getVariableIfExists(name);
		if (var == null) {//not even in the list.
			return false;
		}
		return !var.getHasValue();
	}

	/**
	 * Gets the relevant function wrapper object from the list of functions.
	 * @param line The current line in the file.
	 * @return The function wrapper for the line, otherwise null.
	 */
	private FunctionWrapper getFuncWrapperObj(String line) {
		Matcher m = Regex.funcNamePattern.matcher(line);
		if (m.find()) {
			String name = line.substring(m.start(), m.end());
			System.out.println(master);
			for (FunctionWrapper func : master.getFuncs()) { // check if the function being called name
				// is a name of a known function.
				if (name.equals(func.getName())) {
					return func;
				}
			}
		}
		return null;
	}

	/**
	 * @param line a string of a line
	 * @return a string contains only the brackets and its contents.
	 */
	protected static String getBracketsString(String line) {
		Matcher bracketsM = Regex.bracketsPattern.matcher(line);
		boolean j = bracketsM.find(); // always true
		return line.substring(bracketsM.start(),
				bracketsM.end());
	}

	/**
	 * @param line the line where the function call happens.
	 * @return true if the call is legal, false otherwise.
	 */
	protected boolean isFuncCallLegal(String line) {
		FunctionWrapper funcObj = getFuncWrapperObj(line);
		if (funcObj == null) {
			return false;
		}
		Matcher bracketsM = Regex.bracketsPattern.matcher(line);
		boolean j = bracketsM.find();
		String brackets = line.substring(bracketsM.start()+1, bracketsM.end()-1);
		ArrayList<VariableWrapper> funcActualParams = funcObj.getParams();
		if (brackets.equals("")){
			return funcActualParams == null;
		}
		String[] params = brackets.split("\\s*,\\s*");
		params[0] = params[0].replaceAll("\\s+","");
		params[params.length-1] = params[params.length-1].replaceAll("\\s+","");
		if (params.length != funcActualParams.size()){
			return false;
		}
		for (int i = 0; i <params.length; i++) {
			VariableWrapper variableIfExists = getVariableIfExists(params[i]);
			VariableWrapper.Types wantedType = funcActualParams.get(i).getType();
			if (variableIfExists != null && !(variableIfExists.getType() == wantedType)){
					return false;
			}
			if (!checkIfValueMatchType(wantedType, params[i])) {
				return false;
			}
		}
		return true;

	}

	/**
	 * @param wantedType the type to fit to
	 * @param param the wanted param to be checked
	 * @return a matcher object for the param (a parameter that a function os called with) and the wanted type.
	 */
	private boolean checkIfValueMatchType(VariableWrapper.Types wantedType, String param) {
		Matcher m;
		switch (wantedType){
			case INT:
				m = Regex.INT_PATTERN.matcher(param);
				return m.matches();
			case DOUBLE:
				m = Regex.DOUBLE_PATTERN.matcher(param);
				return m.matches();
			case STRING:
				m = Regex.STRING_PATTERN.matcher(param);
				return m.matches();
			case BOOLEAN:
				m = Regex.BOOLEAN_PATTERN.matcher(param);
				return m.matches();
			case CHAR:
				m = Regex.CHAR_PATTERN.matcher(param);
				return m.matches();
		}
		return false; //will never get here
	}

	/**
	 * Checks if a variable is in the list, if it is return the varible object if not returns null.
	 *
	 * @param name The name of the variable to check if is in the list.
	 * @return The variable object if it's in the list, otherwise null.
	 */
	protected VariableWrapper getVariableIfExists(String name){
		for (VariableWrapper var:this.variables){
			if (var.getName().equals(name)){
				return var;
			}
		}
		if (this.parent == null) {
			return null;
		}
		return parent.getVariableIfExists(name);
	}

}
