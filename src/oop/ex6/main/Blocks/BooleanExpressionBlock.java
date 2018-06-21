package oop.ex6.main.Blocks;

import oop.ex6.main.Regex;

public class BooleanExpressionBlock extends SubBlock {

    public BooleanExpressionBlock(CodeBlock parent, int start) {
        super(parent,start);
    }

    protected boolean checkEnd(String line, String nextLine) {
        return Regex.BLOCK_END_PATTERN.matcher(line).matches();
    }
}
