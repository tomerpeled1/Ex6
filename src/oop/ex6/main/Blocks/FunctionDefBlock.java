package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;
import oop.ex6.main.VariableWrapper;

import java.util.ArrayList;

/**
 * this class represents a scope of a function.
 */
public class FunctionDefBlock extends SubBlock {

    private FunctionWrapper wrapper;

    /**
     * initializes a new functionBlock object.
     * @param wrapper the FunctionWrapper this block has, holds it's name and parameters.
     * @param parent the block that contains this block directly will always be the master block.
     * @param startLine the line in which the block starts.
     */
    public FunctionDefBlock (FunctionWrapper wrapper, MasterBlock parent, int startLine) {
        super(parent,startLine);
        this.variables.addAll(wrapper.getParams());
        this.wrapper = wrapper;
        this.master = parent;
//        this.globalVars =
    }



    /**
     * {@inheritDoc}
     */
    protected boolean checkEnd(String line, String nextLine) {
        if (line == null){
            return false;
        }
        if (!Regex.RETURN_PATTERN.matcher(line).matches()) {
            return false;
        }
        return Regex.BLOCK_END_PATTERN.matcher(nextLine).matches();
    }

    /**
     * Gets the name of the function.
     * @return The name of the function.
     */
    public String getFuncName() {
        return wrapper.getName();
    }

    /**
     * Gets the parameters of the function
     * @return An ArrayList that contains all the parameters of the function.
     */
    public ArrayList<VariableWrapper> getParams() {
        return wrapper.getParams();
    }

}