package oop.ex6.main.Blocks;

import oop.ex6.main.FunctionWrapper;
import oop.ex6.main.IllegalLineException;
import oop.ex6.main.VariableWrapper;

import java.util.ArrayList;

public class FunctionDefBlock extends SubBlock {


    public FunctionDefBlock (ArrayList<VariableWrapper> functionParameters, MasterBlock parent) {
        super(parent);
        this.variables.addAll(functionParameters);
    }

}
