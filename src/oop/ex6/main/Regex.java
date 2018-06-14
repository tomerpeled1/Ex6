package oop.ex6.main;

import java.util.regex.Pattern;

public class Regex {

	private static String type = "[(int)(double)(String)(boolean)(char)]";

	private static String methodName = "[a-zA-Z]";

	static final Pattern FUNCTION_TEMPLATE = Pattern.compile("void" +methodName+ "\\(");

	void[ ]+[a-zA-z]\w*\([ ]*((double|String|boolean|char)[ ]+[a-zA-z]\w*[ ]*)+\){
}
