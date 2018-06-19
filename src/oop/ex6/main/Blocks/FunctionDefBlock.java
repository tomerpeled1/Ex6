package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.Regex;
import oop.ex6.main.VariableWrapper;

import java.util.ArrayList;

public class FunctionDefBlock extends SubBlock {


    public FunctionDefBlock (ArrayList<VariableWrapper> functionParameters, MasterBlock parent) {
        super(parent);
        this.variables.addAll(functionParameters);
    }

    protected boolean checkEnd(String line, String nextLine) {
        if (!Regex.RETURN_PATTERN.matcher(line).matches()) {
            return false;
        }
        return Regex.BLOCK_END_PATTERN.matcher(nextLine).matches();
    }
}