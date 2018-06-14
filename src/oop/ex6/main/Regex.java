package oop.ex6.main;

import java.util.regex.Pattern;

public class Regex {

	private static String type = "[(int)(double)(String)(boolean)(char)]";

	static final Pattern FUNCTION_TEMPLATE = Pattern.compile("void" +". " type + "\\(");

}
