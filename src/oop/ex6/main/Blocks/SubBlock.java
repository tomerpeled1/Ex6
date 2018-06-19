package oop.ex6.main.Blocks;

/**
 * this class represents a block which is not the main block - a function decleretion of if/while block.
 */
public class SubBlock extends CodeBlock {

    private MasterBlock master;

    public SubBlock(CodeBlock parent) {
        super(parent);

    }


}
