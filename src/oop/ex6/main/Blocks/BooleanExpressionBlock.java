package oop.ex6.main.Blocks;

import oop.ex6.main.Regex;

public class BooleanExpressionBlock extends SubBlock {

    public BooleanExpressionBlock(CodeBlock parent, int start) {
        super(parent,start);
    }

    protected boolean checkEnd(String line, String nextLine) {
        if (line == null) {
            return Regex.BLOCK_END_PATTERN.matcher(nextLine).matches();
        }
        return Regex.BLOCK_END_PATTERN.matcher(line).matches();
    }
}
