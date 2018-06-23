package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.VariableWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * this class represents a single block, scope of code - a global scope, function or if/while scope.
 */
public abstract class CodeBlock {

	protected static final String OPEN_BLOCK_AT_END_ERROR = "Error - need to close all curly braces.";
	protected static final String ERROR_START = "Error in line ";
	private static final String VAR_ERROR = " variable deceleration is done poorly.";
	protected static final String FUNC_DEC_ERROR = " function deceleration is done poorly.";
	protected static final String ILLEGAL_LINE_IN_MAIN_SCOPE = ", illegal line in main scope";
	private static final String FINAL_VARIABLE_ERROR = ", can't assign new value to final variable";
	private static final String NO_GIVEN_VALUE_ERROR = ", no given value.";
	private static final String CANT_INITIALIZE_VARIABLE = ", can't initialize variable.";
	private static final String VALUE_TYPE_ERROR = ", value doesn't match type.";


	protected ArrayList<VariableWrapper> variables;
	protected CodeBlock parent;
	//	protected static LinesRunner runner; //TODO delete if possible
	protected MasterBlock master;
//	protected ArrayList<VariableWrapper> globalVars;


	/**
	 * initializes a new code block.
	 */
	protected CodeBlock() {
		setMaster();
	}

	/**
	 * sets the master of this block to be the master lock of the file.
	 */
	private void setMaster() {
		if (this instanceof MasterBlock) master = (MasterBlock) this;
		else this.master = parent.master;
	}

	/**
	 * Constructs a new CodeBlock with a parent block.
	 *
	 * @param parent The block this block is contained in.
	 */
	public CodeBlock(CodeBlock parent) {
		this.parent = parent;
		this.variables = new ArrayList<VariableWrapper>();
		setMaster();
	}

	/**
	 * Runs on the lines of the blcok.
	 *
	 * @throws IllegalLineException When encountering with an illegal line.
	 */
	public abstract void run() throws IllegalLineException;

	/**
	 * Checks if there's a need to handle an assignment.
	 *
	 * @param line The line of the assignment to check.
	 * @return True if it was an assignment, false otherwise.
	 * @throws IllegalLineException If the assignment line was wrong.
	 */
	protected boolean assignmentLineHandle(String line, int lineNum) throws IllegalLineException {
		line = line.replaceAll("\\s|;", "");
		if (line.indexOf("=") <= 0) {
			return false;
		}
		String[] varComponents = line.split("=");
		VariableWrapper var = getVariableIfExists(varComponents[0]);
		if (isOnlyGlobalVar(var) && this instanceof SubBlock) {
			VariableWrapper newVar = new VariableWrapper(var.getType(), var.getHasValue(),
					var.getName(), var.isFinal());
			this.variables.add(newVar);
			var = newVar;
		}
		if (var == null) {
			throw new IllegalLineException(ERROR_START + lineNum);
		}
		if (var.isFinal()) {
			throw new IllegalLineException(ERROR_START + lineNum + FINAL_VARIABLE_ERROR);
		}
		if (legalAssignment(line, varComponents, false, var.getType(), lineNum)) {
			var.setHasValue(true);
		}
		return true;
	}

	protected abstract boolean isOnlyGlobalVar(VariableWrapper var);


	/**
	 * Checks if an assignment(or declaration) of variable is legal.
	 *
	 * @param var           The variable assignment.
	 * @param varComponents The components of the assignment.
	 * @param isFinal       A boolean indicating if the variable is final or not.
	 * @param type          The type of the variable.
	 * @return True if it's a legal assignment, false if it's not an assignment but a declaration.
	 * @throws IllegalLineException Whenever the assignment/declaration is not legal.
	 */
	private boolean legalAssignment(String var, String[] varComponents, boolean isFinal,
	                                VariableWrapper.Types type, int lineNum)
			throws IllegalLineException {
		if (var.indexOf('=') < 0 && isFinal) {
			throw new IllegalLineException(ERROR_START + lineNum + FINAL_VARIABLE_ERROR);
		}
		if (var.indexOf('=') >= 0 && varComponents.length == 1) {
			throw new IllegalLineException(ERROR_START + lineNum + NO_GIVEN_VALUE_ERROR);
		}
		if (varComponents.length > 2) {
			throw new IllegalLineException(ERROR_START + lineNum);
		}
		Matcher m = Regex.varNamePattern.matcher(varComponents[0]);
		//if (!m.matches() || checkIfBlockVariable(varComponents[0])) {
		if (!m.matches()) {
			throw new IllegalLineException(ERROR_START + lineNum +
					CANT_INITIALIZE_VARIABLE);
		}

		if (varComponents.length == 2) {
			if (!checkIfValueMatchType(type, varComponents[1])) {
				throw new IllegalLineException(ERROR_START + lineNum +
						VALUE_TYPE_ERROR);
			}
			return true;
		}
		return false; //means varComponents length is one, not assignment, only declaration.
	}


	/**
	 * Gets a declaration line and returns the wrappers for the declared variables.
	 *
	 * @param line The declaration line for creating variables objects.
	 * @return An ArrayList of VariableWrapper.
	 */
	protected void declarationLineToVarObj(String line, int lineNum) throws IllegalLineException {
		Matcher typeNameMatcher = Regex.typePattern.matcher(line);
		String type = "";
		if (typeNameMatcher.find()) {
			type = line.substring(typeNameMatcher.start(), typeNameMatcher.end());
		}
		//ArrayList<VariableWrapper> newVariables = new ArrayList<VariableWrapper>();
		line = line.replaceFirst(Regex.variableTypeCheck, "");
		boolean isFinal = false;
		Matcher finalM = Regex.FINAL_PATTERN.matcher(line);
		if (finalM.lookingAt()) {
			line = line.replaceFirst(Regex.FINAL_DEC, "");
			isFinal = true;
		}

		if (line.endsWith(",;")){ //TODO maybe it is a bad solution, look again, it tries to fix int a,b,c,;
			throw new IllegalLineException(ERROR_START + lineNum + ", bad variable declereation");
		}
		String[] split = line.split("(\\s*,(\\s*)|\\s*;\\s*)");
		for (String var : split) {
			String[] varComponents = var.split("\\s*=\\s*");
			if (checkIfBlockVariable(varComponents[0])) {
				throw new IllegalLineException(ERROR_START + lineNum + ", variable already exists.");
			}
			if (legalAssignment(var, varComponents, isFinal, VariableWrapper.stringToTypes(type), lineNum)) {
				//newVariables.add(new VariableWrapper(type, true, varComponents[0], isFinal));
				this.variables.add(new VariableWrapper(type, true, varComponents[0], isFinal));
			} else {
				//newVariables.add(new VariableWrapper(type, false, varComponents[0], false));
				this.variables.add(new VariableWrapper(type, false, varComponents[0], false));
			}
		}
		//return newVariables;//if it has no variables in it, it's ok.
	}

	//variable checks.
	private boolean checkIfBlockVariable(String name) {
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
	protected boolean InitiliazedValidBoolean(String name) {
		VariableWrapper var = getVariableIfExists(name);
		if (var == null) {//not even in the list.
			return false;
		}
		return var.getHasValue() &&
				(var.getType() == VariableWrapper.Types.INT || var.getType() == VariableWrapper.Types.DOUBLE
				|| var.getType() == VariableWrapper.Types.BOOLEAN);
	}

	//end of variable checks.


	/**
	 * Gets the relevant function wrapper object from the list of functions.
	 *
	 * @param line The current line in the file.
	 * @return The function wrapper for the line, otherwise null.
	 */
	private FunctionDefBlock getFuncDefBlockObj(String line) {
		Matcher m = Regex.funcNamePattern.matcher(line);
		if (m.find()) {
			String name = line.substring(m.start(), m.end());
			for (FunctionDefBlock func : master.getFuncs()) { // check if the function being called name
				// is a name of a known function.
				if (name.equals(func.getFuncName())) {
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
		FunctionDefBlock funcObj = getFuncDefBlockObj(line);
		if (funcObj == null) {
			return false;
		}
		Matcher bracketsM = Regex.bracketsPattern.matcher(line);
		boolean j = bracketsM.find();
		String brackets = line.substring(bracketsM.start() + 1, bracketsM.end() - 1);
		ArrayList<VariableWrapper> funcActualParams = funcObj.getParams();
		if (brackets.equals("")) {
			return funcActualParams.isEmpty();
		}
		String[] params = brackets.split("\\s*,\\s*");
		params[0] = params[0].replaceAll("\\s+", "");
		params[params.length - 1] = params[params.length - 1].replaceAll("\\s+", "");
		if (params.length != funcActualParams.size()) {
			return false;
		}
		for (int i = 0; i < params.length; i++) {
			VariableWrapper variableIfExists = getVariableIfExists(params[i]);
			VariableWrapper.Types wantedType = funcActualParams.get(i).getType();
			if (variableIfExists != null && !(variableIfExists.getType() == wantedType)) {
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
	 * @param param      the wanted param to be checked
	 * @return a matcher object for the param (a parameter that a function os called with) and the wanted type.
	 */
	private boolean checkIfValueMatchType(VariableWrapper.Types wantedType, String param) {
		Matcher m;
		VariableWrapper var = getVariableIfExists(param);
		if (var!= null && var.getHasValue()){
			VariableWrapper.Types varType = var.getType();
			if (wantedType == VariableWrapper.Types.BOOLEAN) {
				return varType == VariableWrapper.Types.BOOLEAN || varType == VariableWrapper.Types.INT ||
						varType == VariableWrapper.Types.DOUBLE;
			}
			if (wantedType == VariableWrapper.Types.DOUBLE) {
				return varType == VariableWrapper.Types.DOUBLE || varType == VariableWrapper.Types.INT;
			}
			return wantedType == varType;
		}
		switch (wantedType) {
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
	protected VariableWrapper getVariableIfExists(String name) {
//		for (VariableWrapper var:this.globalVars){
//			if (var.getName().equals(name)) {
//				return var;
//			}
//		}
		for (VariableWrapper var : this.variables) {
			if (var.getName().equals(name)) {
				return var;
			}
		}
		if (this.parent == null) {
			return null;
		}
		return parent.getVariableIfExists(name);
	}

}
