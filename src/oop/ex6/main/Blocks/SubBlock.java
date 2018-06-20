package oop.ex6.main.Blocks;

import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;

import java.util.regex.Matcher;

/**
 * this class represents a block which is not the main block - a function decleretion of if/while block.
 */
public abstract class SubBlock extends CodeBlock {

    private MasterBlock master;

    public SubBlock(CodeBlock parent) {
        super(parent);

    }

    @Override
    public void run() throws IllegalLineException {
        String line = runner.getNextLine();
        Matcher BLOCK_END_MATCHER = Regex.BLOCK_END_PATTERN.matcher(line);
        while (!BLOCK_END_MATCHER.matches()) {
            CodeBlock nextBlock = null;
            if (line.startsWith("//")){
                line = runner.getNextLine();
                continue;
            }

            //TODO when checking the end, get the next line, save it, call the function check end and than run on the next line.

            //TODO check if variable declaration (aka int x = 3), check that it's not in this scope variables(can be in others)

            //TODO check if variable assigment (aka x = 3)

            //TODO check a call to a fucntion, check if function is in the fnctions list and check paramenters

            //TODO check if a declaration of functoin only in master block and than run on it.
            if (checkIfValidBooleanExpression(line)) {
                nextBlock = new BooleanExpressionBlock(this);
            }
            if (nextBlock != null) {
                nextBlock.run();
            }
            line = runner.getNextLine();
        }
    }

    /**
     * Checks if a line opens a new if or while block.
     *
     * @param line The line to check if a opens a block.
     * @return True if a valid boolean expression, otherwise false.
     */
    private boolean checkIfValidBooleanExpression(String line) throws IllegalLineException {
        Matcher ifMatcher = Regex.IF_PATTERN.matcher(line);
        Matcher whileMatcher = Regex.WHILE_PATTERN.matcher(line);
        String booleanExpression = null;
        int expressionStart = 0;
        int expressionEnd = 0;
        if (ifMatcher.matches() || whileMatcher.matches()) {
            expressionStart = line.indexOf('(');
            expressionEnd = line.indexOf(')');
            booleanExpression = line.substring(expressionStart, expressionEnd);
            String[] splitExpression = booleanExpression.split(Regex.BOOLEAN_EXPRESSION_SPLIT);
            for (String exp : splitExpression) {
                Matcher doubleMatch = Regex.DOUBLE_PATTERN.matcher(exp);
                if (!(exp.equals("true") || exp.equals("false") || doubleMatch.matches() ||
                        checkIfInitializedVariable(exp))) {
                    throw new IllegalLineException("error in line " + runner.getLineNumber() + ", boolean " +
                            "expression not supported.");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if the line means the end of the block.
     * @param line The line to check if it's the end of the block.
     * @return The next line if it's used in the function(because of Runner class there's no other way to get it).
     */
    protected abstract boolean checkEnd(String line, String nextLine);


}
