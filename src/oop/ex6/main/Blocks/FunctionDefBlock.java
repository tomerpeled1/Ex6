package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;
import oop.ex6.main.VariableWrapper;

import java.util.ArrayList;

public class FunctionDefBlock extends SubBlock {

    private FunctionWrapper wrapper;

    public FunctionDefBlock (FunctionWrapper wrapper, MasterBlock parent) {
        super(parent);
        this.variables.addAll(wrapper.getParams());
        this.wrapper = wrapper;
    }

    protected boolean checkEnd(String line, String nextLine) {
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