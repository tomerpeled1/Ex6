package oop.ex6.main;

import java.util.regex.Pattern;

public class Regex {

	private static String type = "(int|double|String|boolean|char)";

	private static String methodName = "[a-zA-Z][a-zA-z0-9_]*";

	private static String varNAme = "[a-zA-Z_][a-zA-z0-9_]*";

	public static final Pattern FUNCTION_TEMPLATE = Pattern.compile("void[ ]+" + methodName + "[ ]*\\([ ]*([ ]*"
			+ type + "[ ]+" + varNAme + "[ ]*,)*[ ]*" + type + "[ ]+" + varNAme + "[ ]*\\)\\{");

	}
