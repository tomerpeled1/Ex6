package oop.ex6.main;

import java.util.regex.Pattern;

public class Regex {

	public static final String DOUBLE_CHECK = "(([\\d]+[.]*[\\d]*)|([\\d]*[.]*[\\d]+))";
	public static final Pattern DOUBLE_PATTERN = Pattern.compile(DOUBLE_CHECK);

	private static final String type = "(int|double|String|boolean|char)";

	private static final String methodName = "[a-zA-Z][a-zA-z0-9_]*";

	private static final String varName = "[a-zA-Z_][a-zA-z0-9_]*";

	public static final Pattern intAssignment = Pattern.compile(varName + "[\\s]*=[\\s]*[\\d]+");
	public static final Pattern doubleAssignnment = Pattern.compile(varName + "[\\s]*=[\\s]*" + DOUBLE_CHECK);
	//TODO check for char should check '' and only one charcter
	//TODO check for boolean should check only for true or false.
	//TODO check for String should check for ""

	public static final Pattern FUNCTION_TEMPLATE = Pattern.compile("\\s*void\\s+" + methodName + "\\s*\\(\\s*" +
			"(\\s*" + type + "\\s+" + varName + "\\s*,\\s*)*" +
			"(\\s*" + type + "\\s+" + varName + "\\s*)*\\)\\s*\\{\\s*");


	public static final Pattern VARIABLE_TEMPLATE = Pattern.compile("\\s*" + type + "\\s+" +
			varName + "\\s*;");


	public static final Pattern IF_PATTERN = Pattern.compile("\\s*if\\s*\\(.+\\)\\s*\\{");
	public static final Pattern WHILE_PATTERN = Pattern.compile("\\s*while\\s*\\(.+\\)\\s*\\{");

	public static final Pattern typePattern = Pattern.compile(type);
	public static final Pattern varNamePattern = Pattern.compile(varName);
	public static final Pattern bracketsPattern = Pattern.compile("\\(.*\\)");
	public static final Pattern funcLineStartPattern = Pattern.compile("\\s*void");
	public static final Pattern varLinePattern = Pattern.compile("[\\s]*" + type + "[\\s]*.*[\\s]*;[\\s]*");
	public static final Pattern finalVarLinePattern = Pattern.compile("[\\s]*" + "final" + "[\\s]*" + type +
																	   "[\\s]*.*[\\s]*;[\\s]*");

	public static final Pattern funcNamePattern = Pattern.compile(methodName);

	public static final String VARIABLE_DECLARATION_SPLIT = "(\\s*" + type + "\\s*|(\\s*(,|;)\\s*))";

	public static final Pattern BLOCK_END_PATTERN = Pattern.compile("\\s*}\\s*");

	public static final String BOOLEAN_EXPRESSION_SPLIT = "\\s*(\\|\\||&&)*\\s*";


}