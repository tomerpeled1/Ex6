import oop.ex6.main.Regex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDvir {
    public static void main(String[] args) {
//        String expression = "a || f&&3 || 87";
//        String s = "       x   = 4";
//        int expressionStart = s.indexOf('(') + 1;
//        int expressionEnd = s.indexOf(')');
//        //System.out.println(s.substring(expressionStart, expressionEnd));
//        //String[] splitted = expression.split("\\s*(\\|\\||&&)+\\s*");
//        String x = s.replaceAll("\\s", "");
//        String[] splitted = x.split("=");
//        for (String str: splitted) {
//            System.out.println(str);
//        }
        String x = "  int = 33";
        String y = x.replaceFirst("[\\s]*int[\\s]*", "");
        System.out.println(y);
        Matcher m = Pattern.compile("\\s*int\\s*").matcher(x);
        System.out.println(m.lookingAt());
    }
}
