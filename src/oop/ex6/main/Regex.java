package oop.ex6.main;

import java.util.regex.Pattern;

public class Regex {

	private static final String type = "(int|double|String|boolean|char)";

	private static final String methodName = "[a-zA-Z][a-zA-z0-9_]*";

	private static final String varName = "[a-zA-Z_][a-zA-z0-9_]*";

	public static final Pattern FUNCTION_TEMPLATE = Pattern.compile("void[ ]+" + methodName + "[ ]*\\([ ]*([ ]*"
			+ type + "[ ]+" + varName + "[ ]*,)*[ ]*" + type + "[ ]+" + varName + "[ ]*\\)\\{");


	public static final Pattern IF_PATTERN = Pattern.compile("[ ]*if[ ]*\\(.+\\)[ ]*\\{");
	public static final Pattern WHILE_PATTERN = Pattern.compile("[ ]*while[ ]*\\(.+\\)[ ]*\\{");

	public static final Pattern typePattern = Pattern.compile(type);
	public static final Pattern varNamePattern = Pattern.compile(varName);
	public static final Pattern bracketsPattern = Pattern.compile("\\(.*\\)");
	public static final Pattern lineStartPattern = Pattern.compile("[ ]*void");

	public static final Pattern funcNamePattern = Pattern.compile(methodName);


	public static final Pattern BLOCK_END_PATTERN = Pattern.compile("[ ]*}[ ]*");

	public static final String BOOLEAN_EXPRESSION_SPLIT = "[ ]*(\\|\\||&&)[ ]*";

}