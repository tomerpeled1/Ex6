package oop.ex6.main.Blocks;

import com.sun.org.apache.bcel.internal.classfile.Code;
import oop.ex6.main.VariableWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CodeBlock {
    protected ArrayList<VariableWrapper> variables;
    private CodeBlock parent;
    protected static LinesRunner runner;


    protected CodeBlock() {
    }

    /**
     * Constructs a new CodeBlock with a parent block.
     * @param parent The block this block is contained in.
     */
    public CodeBlock(CodeBlock parent) {
        this.parent = parent;
    }

    public void run() throws IllegalLineException {
        String line = runner.GetNextLine();
        Matcher BLOCK_END_MATCHER = Regex.BLOCK_END_PATTERN.matcher(line);
        while (!BLOCK_END_MATCHER.matches()) {
            CodeBlock nextBlock = null;
            //TODO check if variable declaration (aka int x = 3), check that it's not in this scope variables(can be in others)

            //TODO check if variable assigment (aka x = 3)

            //TODO check a call to a fucntion, check if function is in the fnctions list and check paramenters

            //TODO check if if/while and a boolean expression.
            Matcher ifMatcher = Regex.IF_PATTERN.matcher(line);
            Matcher whileMatcher = Regex.WHILE_PATTERN.matcher(line);
            String booleanExpression = null;
            int expressionStart = 0;
            int expressionEnd = 0;
            if (ifMatcher.matches() || whileMatcher.matches()) {
                expressionStart = line.indexOf('(');
                expressionEnd = line.indexOf(')');
                booleanExpression = line.substring(expressionStart, expressionEnd);
                String[] splittedExpression = booleanExpression.split(Regex.BOOLEAN_EXPRESSION_SPLIT);
                for (String exp : splittedExpression) {
                    Matcher doubleMatch = Regex.DOU
                    if (exp.equals("true") || exp.equals("false")) ||
                    //TODO check if initiliazed boolean, double or int.
                    //TODO check if a a double or int constant value.
                    //TODO if not throw exception.
                    nextBlock = new BooleanExpressionBlock(this);
                }
            }
            if (nextBlock != null) {
                nextBlock.run();
            }
        }
    }

}
