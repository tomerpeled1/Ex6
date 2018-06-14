import oop.ex6.main.Regex;

import java.util.regex.Matcher;

public class TestPeled {


	public static void main(String[] args) {
		String function = "void f(int j){";

		Matcher m = Regex.FUNCTION_TEMPLATE.matcher(function);

		System.out.println(m.matches());
	}

}
