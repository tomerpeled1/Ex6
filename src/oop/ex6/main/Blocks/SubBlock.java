package oop.ex6.main.Blocks;

import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;
import oop.ex6.main.VariableWrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class represents a block which is not the main block - a function decleretion of if/while block.
 */
public abstract class SubBlock extends CodeBlock {

	private static final String ILLEGAL_CALL_TO_FUNCTION = ", illegal call to function.";
	private static final String INVALID_BOOL_EXPRESSION = ", boolean " +
			"expression not supported.";
	private int startLineIndex;
	private int endLineIndex;

	/**
	 * initializes a code block with a parent and a startline.
	 * @param parent the block contains directly this block.
	 * @param startLine the line in the code where the block starts.
	 */
	public SubBlock(CodeBlock parent, int startLine) {
		super(parent);
		startLineIndex = startLine;
	}

	@Override
	protected boolean isOnlyGlobalVar(VariableWrapper var) {
		for (VariableWrapper localVar:variables){
			if (localVar.getName().equals(var.getName()) && localVar.getType().equals(var.getType())){
				return false;
			}
		}
		return parent.isOnlyGlobalVar(var);
	}

	@Override
	public void run() throws IllegalLineException {
		int curLineNum = startLineIndex;
		String line = null;
		String nextLine = master.getLines()[curLineNum];
		while (!checkEnd(line, nextLine)) {
			line = nextLine;
			SubBlock nextBlock = null;
			if (line.startsWith("//") || Regex.EMPTY_LINE_PATTERN.matcher(line).matches()) {
				curLineNum++;
				nextLine = master.getLines()[curLineNum];
				continue;
			}
			Matcher varDec = Regex.VarDec.matcher(line);
			Matcher returnMatcher = Regex.RETURN_PATTERN.matcher(line);
			if (varDec.matches()) { // variable declaration line
				declarationLineToVarObj(line, curLineNum);
			} else if (isCallToFunction(line)) {
				if (!isFuncCallLegal(line)) {
					throw new IllegalLineException(ERROR_START + curLineNum + ILLEGAL_CALL_TO_FUNCTION);
				}
			} else if (checkIfValidBooleanExpression(line,curLineNum)) {
				nextBlock = new BooleanExpressionBlock(this,curLineNum+1);
			}
			else if (returnMatcher.matches()) {
				curLineNum++;
				nextLine = master.getLines()[curLineNum];
				if (checkEnd(line, nextLine)) {
					endLineIndex = curLineNum;
					return;
				}
				continue;
			} else if (assignmentLineHandle(line,curLineNum)) {
				//If it's true the function assigns the variable.
			}
			else {
				throw new IllegalLineException(ERROR_START + curLineNum + " no such operation");
			}
			if (nextBlock != null) {
				line = null;
				nextBlock.run();
				curLineNum = nextBlock.endLineIndex;
			}
			curLineNum++;
			nextLine = master.getLines()[curLineNum];
		}
		endLineIndex = curLineNum;
	}


	/*
	checks if a line is a call to function.
	 */
	private boolean isCallToFunction(String line) {
		for (FunctionDefBlock func : master.getFuncs()) {
			String regex = "\\s*" + func.getFuncName() + "\\s*(.*)\\s*;\\s*";
			if (Pattern.compile(regex).matcher(line).matches()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Checks if a line opens a new if or while block.
	 *
	 * @param line The line to check if a opens a block.
	 * @return True if a valid boolean expression, otherwise false.
	 */
	private boolean checkIfValidBooleanExpression(String line,int lineNum) throws IllegalLineException {
		Matcher ifMatcher = Regex.IF_PATTERN.matcher(line);
		Matcher whileMatcher = Regex.WHILE_PATTERN.matcher(line);
		String booleanExpression = null;
		int expressionStart = 0;
		int expressionEnd = 0;
		if (ifMatcher.matches() || whileMatcher.matches()) {
			expressionStart = line.indexOf('(');
			expressionEnd = line.indexOf(')');
			booleanExpression = line.substring(expressionStart+1, expressionEnd);
			String[] splitExpression = booleanExpression.split(Regex.BOOLEAN_EXPRESSION_SPLIT);
			for (String exp : splitExpression) {
				exp = exp.trim();
				//exp = exp.replaceAll("\\s", "");
				Matcher doubleMatch = Regex.DOUBLE_PATTERN.matcher(exp);
				if (!(exp.equals("true") || exp.equals("false") || doubleMatch.matches() ||
						InitiliazedValidBoolean(exp))) {
					throw new IllegalLineException(ERROR_START + lineNum + INVALID_BOOL_EXPRESSION);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks if the line means the end of the block.
	 * @param line The line to check if it's the end of the block.
	 * @param nextLine the next line.

	 * @return The next line if it's used in the function(because of Runner class there's no other way to get it).
	 */
	protected abstract boolean checkEnd(String line, String nextLine);


}
