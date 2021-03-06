package oop.ex6.main;

import java.util.regex.Pattern;

/**
 * this class holds all the Patterns the program is using.
 */
public class Regex {

	private static final String DOUBLE_CHECK = "((-?[\\d]+[.]*[\\d]*)|(-?[\\d]*[.]*[\\d]+))";
	public static final Pattern DOUBLE_PATTERN = Pattern.compile(DOUBLE_CHECK);
	public static final Pattern INT_PATTERN = Pattern.compile("-?[\\d]+");
	public static final Pattern CHAR_PATTERN = Pattern.compile("'.'");
	public static final Pattern STRING_PATTERN = Pattern.compile("\".*\"");
	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("(true|false|" + DOUBLE_CHECK +")");

	public static final Pattern RETURN_PATTERN = Pattern.compile("[\\s]*return[\\s]*;[\\s]*");

	private static final String type = "(int|double|String|boolean|char)";
	public static final String variableTypeCheck = "\\s*" + type + "\\s*";

	private static final String methodName = "[a-zA-Z][a-zA-Z0-9_]*";

	private static final String varName = "(([a-zA-Z][a-zA-Z0-9_]*)|[_][a-zA-Z0-9_]+)";
	public static final String FINAL_DEC = "\\s*final";

	private static final String equalSignCheck = "[\\s]*=[\\s]*";

	//Assignments checks.
	private static final String intAssignment = equalSignCheck + "[\\d]+";
	private static final String doubleAssignment = equalSignCheck + DOUBLE_CHECK;
	private static final String charAssignment = equalSignCheck + "'.'[\\s]*";
	private static final String booleanAssignment = equalSignCheck + "(true|false)[\\s]*";
	private static final String stringAssignment = equalSignCheck + "\".*\"[\\s]*";





	public static final Pattern intDeclarationAssignment = Pattern.compile(varName + intAssignment);
	public static final Pattern doubleDeclarationAssignment = Pattern.compile(varName + doubleAssignment);
	public static final Pattern charDeclarationAssignment = Pattern.compile(varName + charAssignment);
	public static final Pattern booleanDeclarationAssignment = Pattern.compile(varName + booleanAssignment);
	public static final Pattern stringDeclarationAssignment = Pattern.compile(varName + stringAssignment);
	//end of assignments checks.

	public static final Pattern FUNCTION_TEMPLATE = Pattern.compile("\\s*void\\s+" + methodName +
			"\\s*((\\(\\s*" +
			"(\\s*((final)*\\s+)*" + type + "\\s+" + varName + "\\s*,\\s*)*" +
			"(\\s*((final)*\\s+)*" + type + "\\s+" + varName + "\\s*)\\))|(\\(\\s*\\)))\\s*\\{\\s*");



	public static final Pattern IF_PATTERN = Pattern.compile("\\s*if\\s*\\(.+\\)\\s*\\{\\s*");
	public static final Pattern WHILE_PATTERN = Pattern.compile("\\s*while\\s*\\(.+\\)\\s*\\{\\s*");

	public static final Pattern typePattern = Pattern.compile(type);
	public static final Pattern VarDec = Pattern.compile("(\\s*((final)?\\s+" + type + ")|" + type + ").*;\\s*");
	public static final Pattern varNamePattern = Pattern.compile(varName);
	public static final Pattern bracketsPattern = Pattern.compile("\\(.*\\)");
	public static final Pattern funcLineStartPattern = Pattern.compile("\\s*void");
	private static final String varLineCheck = "[\\s]*" + type + "[\\s]+.*[\\s]*;[\\s]*";
	public static final Pattern varLinePattern = Pattern.compile(varLineCheck);
	public static final Pattern finalVarLinePattern = Pattern.compile("[\\s]*" + "final[\\s]+" + varLineCheck);
	public static final Pattern FINAL_PATTERN = Pattern.compile(FINAL_DEC);
	public static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("\\s*");
	public static final Pattern OPEN_BLOCK_PATTERN = Pattern.compile(".*\\{\\s*");
	public static final Pattern CLOSE_BLOCK_PATTERN = Pattern.compile("\\s*}\\s*");
	public static final Pattern END_WITH_COMMA = Pattern.compile(".*(\\s*,\\s*;\\s*)");


	public static final Pattern funcNamePattern = Pattern.compile(methodName);

	public static final String VARIABLE_DECLARATION_SPLIT = "(\\s*" + type + "\\s*|(\\s*(,|;)\\s*))";

	public static final Pattern BLOCK_END_PATTERN = Pattern.compile("\\s*}\\s*");

	public static final String BOOLEAN_EXPRESSION_SPLIT = "\\s*(\\|\\||&&)+\\s*";


}