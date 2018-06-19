package oop.ex6.main.Blocks;

import oop.ex6.main.IllegalLineException;

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
        //TODO implement here insted of CodeBlock.
    }

    /**
     * Checks if the line means the end of the block.
     * @param line The line to check if it's the end of the block.
     * @return The next line if it's used in the function(because of Runner class there's no other way to get it).
     */
    protected abstract boolean checkEnd(String line, String nextLine);


}
