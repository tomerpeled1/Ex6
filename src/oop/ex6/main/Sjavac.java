package oop.ex6.main;

import oop.ex6.main.Blocks.MasterBlock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * the main class. it gets a sjava file name and checks if the file will be able to compile.
 */
public class Sjavac {

	private final static String IO_ERROR = "IO Error - problem with the file";
	private static final String ARGS_NUM_ERROR = "The program should get exactly one argument.";
	private static final int IO_ERROR_OUTPUT = 2;
	private static final int LEGAL_CODE_OUTPUT = 0;
	private static final int ILLEGAL_CODE_OUTPUT = 1;

	/*
	returns an array of all lines in file, as Strings
	 */
	private static String[] getLinesFromPath(String path) throws IOException {
		String[] lines;
		Path path1 = Paths.get(path);
		lines = Files.readAllLines(path1).toArray(new String[0]);
		return lines;
	}


	/**
	 * main function, runs the program- gets an sjava file and returns 0 if it is compileable,
	 * 1 if it is not and 2 if cant open the file.
	 * @param args one sjava file.
	 */
	public static void main(String[] args){

		if (args.length != 1){
			System.err.println(ARGS_NUM_ERROR);
			System.out.println(IO_ERROR_OUTPUT);
			return;
		}
		String[] lines = null;
		try {
			lines = getLinesFromPath(args[0]);
		} catch (IOException e) {
			System.err.println(IO_ERROR);
			System.out.println(IO_ERROR_OUTPUT);
			return;
		}
		MasterBlock master = new MasterBlock();
		master.setLines(lines);
		try {
			master.run();
			System.out.println(LEGAL_CODE_OUTPUT);
			return;
		} catch (IllegalLineException e) {
			System.err.println(e.getMessage());
			System.out.println(ILLEGAL_CODE_OUTPUT);
			return;
		}

	}

}