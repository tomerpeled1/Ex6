package oop.ex6.main.Blocks;

import com.sun.org.apache.bcel.internal.classfile.Code;
import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.VariableWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		else master = parent.getMaster();
	}

	/**
	 * Constructs a new CodeBlock with a parent block.
	 *
	 * @param parent The block this block is contained in.
	 */
	public CodeBlock(CodeBlock parent) {
		this.parent = parent;
		setMaster();

	}

	/*
	returns the master of the program.
	 */
	private MasterBlock getMaster() {
		return master;
	}

	public void run() throws IllegalLineException {
		String line = runner.GetNextLine();
		Matcher BLOCK_END_MATCHER = Regex.BLOCK_END_PATTERN.matcher(line);
		while (!BLOCK_END_MATCHER.matches()) {
			CodeBlock nextBlock = null;

			if (line.startsWith("//")){
				line = runner.GetNextLine();
				continue;
			}

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
			line = runner.GetNextLine();
		}
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
					throw new IllegalLineException("error in line " + runner.GetLineNumber() + ", boolean " +
							"expression not supported.");
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks if a variable is in the list, if it is return the varible object if not returns null.
	 *
	 * @param name The name of the variable to check if is in the list.
	 * @return The variable object if it's in the list, otherwise null.
	 */
	private VariableWrapper getVariableFromList(String name) {
		for (VariableWrapper var : variables) {
			if (var.getName().equals(name)) {
				return var;
			}
		}
		return null;
	}

	/**
	 * Checks if a variable is an initialized variable.
	 * !! can't check if uninitialized variable.
	 *
	 * @param name The name of the variable to check.
	 * @return True if it's initialized, false else.
	 */
	protected boolean checkIfInitializedVariable(String name) {
		VariableWrapper var = getVariableFromList(name);
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
		VariableWrapper var = getVariableFromList(name);
		if (var == null) {//not even in the list.
			return false;
		}
		return !var.getHasValue();
	}

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
	 * @param line an string of a line
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
	public boolean isFuncCallLegal(String line) { //TODO change to private
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
			Matcher m = setMatcherFromType(wantedType,params[i]);
			if (!m.matches()){
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
	private Matcher setMatcherFromType(VariableWrapper.Types wantedType, String param) {
		Matcher m;
		switch (wantedType){
			case INT:
				m = Regex.INT_PATTERN.matcher(param);
				return m;
			case DOUBLE:
				m = Regex.DOUBLE_PATTERN.matcher(param);
				return m;
			case STRING:
				m = Regex.STRING_PATTERN.matcher(param);
				return m;
			case BOOLEAN:
				m = Regex.BOOLEAN_PATTERN.matcher(param);
				return m;
			case CHAR:
				m = Regex.CHAR_PATTERN.matcher(param);
				return m;
		}
		return null; //will never get here
	}

	protected VariableWrapper getVariableIfExists(String name){
		for (VariableWrapper var:this.variables){
			if (var.getName().equals(name)){
				return var;
			}
		}
		if (this.parent == null) return null;
		return parent.getVariableIfExists(name);
	}

}
