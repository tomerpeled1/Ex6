public class TestDvir {
    public static void main(String[] args) {
        String expression = "a || f && 3 || 87";
        String s = "(3 || 5)";
        int expressionStart = s.indexOf('(') + 1;
        int expressionEnd = s.indexOf(')');
        //System.out.println(s.substring(expressionStart, expressionEnd));
        String[] splitted = expression.split("[ ]*(\\|\\||&&)[ ]*");
        for (String str: splitted) {
            System.out.println(str);
        }
    }
}
