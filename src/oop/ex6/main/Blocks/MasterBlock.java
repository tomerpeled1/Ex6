package oop.ex6.main.Blocks;

public class MasterBlock extends CodeBlock {
	private static MasterBlock ourInstance = new MasterBlock();

	public static MasterBlock getInstance() {
		return ourInstance;
	}

	private MasterBlock() {
	}

	//TODO
}
