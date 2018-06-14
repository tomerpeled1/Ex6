package oop.ex6.main;

import java.util.regex.Pattern;

public class Regex {

	private static String type = "(int|double|String|boolean|char)";

	private static String methodName = "[a-zA-Z][a-zA-z0-9_]*";

	private static String varName = "[a-zA-Z_][a-zA-z0-9_]*";

	public static final Pattern FUNCTION_TEMPLATE = Pattern.compile("void[ ]+" + methodName + "[ ]*\\([ ]*([ ]*"
			+ type + "[ ]+" + varName + "[ ]*,)*[ ]*" + type + "[ ]+" + varName + "[ ]*\\)\\{");


	public static final Pattern IF_CHECK = Pattern.compile("[ ]*if[ ]*\\(.+\\)[ ]*\\{");
	public static final Pattern WHILE_CHECK = Pattern.compile("[ ]*while[ ]*\\(.+\\)[ ]*\\{");

	public static final Pattern typePattern = Pattern.compile(type);
	public static final Pattern varNamePattern = Pattern.compile(varName);
	public static final Pattern bracketsPattern = Pattern.compile("\\(.*\\)");
	public static final Pattern lineStartPattern = Pattern.compile("[ ]*void");

	public static final Pattern funcNamePattern = Pattern.compile(methodName);

}