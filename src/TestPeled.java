import oop.ex6.main.Blocks.CodeBlock;
import oop.ex6.main.Blocks.MasterBlock;
import oop.ex6.main.Regex;

import java.util.regex.Matcher;

public class TestPeled {


	public static void main(String[] args) {
		String function = "void f(int j){";

		MasterBlock m = MasterBlock.getInstance();

		System.out.println(m.isFuncCallLegal("tomer( k , 5 )"));
	}


}
