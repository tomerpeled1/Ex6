package oop.ex6.main.Blocks;

import oop.ex6.main.Regex;

/**
 * this class represents a block of if/while statement.
 */
public class BooleanExpressionBlock extends SubBlock {

    /**
     *
     * @param parent
     * @param start
     */
    public BooleanExpressionBlock(CodeBlock parent, int start) {
        super(parent,start);
    }

    protected boolean checkEnd(String line, String nextLine) {
//        if (line == null) {
//            return Regex.BLOCK_END_PATTERN.matcher(nextLine).matches();
//        }
        return Regex.BLOCK_END_PATTERN.matcher(nextLine).matches();
    }
}
