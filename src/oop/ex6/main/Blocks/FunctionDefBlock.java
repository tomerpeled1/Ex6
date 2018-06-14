package oop.ex6.main.Blocks;

import oop.ex6.VariableWrapper;

import java.util.ArrayList;

public class FunctionDefBlock extends SubBlock {


    public FunctionDefBlock (ArrayList<VariableWrapper> functionParameters) {
        this.variables.addAll(functionParameters);
    }
}
