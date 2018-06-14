package oop.ex6.main;

import oop.ex6.FunctionWrapper;
import oop.ex6.VariableWrapper;
import oop.ex6.main.Blocks.MasterBlock;

import java.util.ArrayList;

public class Sjavac {



	private static String[] getLines(String path){
		return null;
	}

	public static void main(String[] args) {

		//TODO check args

		String[] lines = getLines(args[0]);
		MasterBlock master = MasterBlock.getInstance();
		ArrayList<FunctionWrapper> funcs = null;
		ArrayList<VariableWrapper> globalVars = null;

		getGlobalDataMembers(lines, funcs, globalVars);

	}

	private static void getGlobalDataMembers(String[] lines, ArrayList<FunctionWrapper> funcs, ArrayList<VariableWrapper> globalVars) {
		for (String line:lines){
			checkGlobalLine(funcs,globalVars,line);
		}
	}

	private static void checkGlobalLine(ArrayList<FunctionWrapper> funcs,
	                             ArrayList<VariableWrapper> vars, String line){

		if (line.startsWith("void") ){
			funcs.add(lineToFuncObj(line));
		} else if (/*line starts with var decleration*/ ){
			vars.add(lineToVarObj(line));
		}
	}

	private static VariableWrapper lineToVarObj(String line) {

	}

	private static FunctionWrapper lineToFuncObj(String line) {

	}
