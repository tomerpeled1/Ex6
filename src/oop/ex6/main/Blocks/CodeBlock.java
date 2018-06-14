package oop.ex6.main.Blocks;

import oop.ex6.VariableWrapper;

import java.util.ArrayList;

public abstract class CodeBlock {
    protected ArrayList<VariableWrapper> variables;

    public abstract void run();

}
