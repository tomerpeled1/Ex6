package oop.ex6.main;

/**
 * this class represents a variable of the program checked.
 */
public class VariableWrapper {
	public enum Types {
		INT, STRING,DOUBLE, BOOLEAN, CHAR
	}

	private String name;
	private boolean hasValue;
	private Types type;
	private boolean isFinal;

	/**
	 * Constructs a VariableWrapper object with a type, hasValue and name.
	 * @param type The type of the variable.
	 * @param hasValue A boolean indicates if the variable has a value or not.
	 * @param name The name of the variable.
	 * @param isFinal Indicates this variable is final or not.
	 */
	public VariableWrapper (String type, boolean hasValue, String name,boolean isFinal) {
		this(VariableWrapper.stringToTypes(type), hasValue, name, isFinal);
	}

	/**
	 * Constructs a VariableWrapper object with a type, hasValue and name.
	 * @param type The type of the variable.
	 * @param hasValue A boolean indicates if the variable has a value or not.
	 * @param name The name of the variable.
	 * @param isFinal Indicates this variable is final or not.
	 */
	public VariableWrapper (Types type, boolean hasValue, String name, boolean isFinal) {
		this.hasValue = hasValue;
		this.type = type;
		this.name = name;
		this.isFinal = isFinal;
	}

	/**
	 * A getter for the hasValue property.
	 * @return value of hasValue property.
	 */
	public boolean getHasValue() {
		return this.hasValue;
	}

	/**
	 * Puts a new value in hasValue.
	 * @param hasValue Indicates if this variable is intiliazed or not.
	 */
	public void setHasValue(boolean hasValue) {
		this.hasValue = hasValue;
	}

	/**
	 * @param str a type of variable as a string
	 * @return  type of variable as a Types instance.
	 */
	public static Types stringToTypes(String str) {
		switch (str) {
			case "int":
				return Types.INT;
			case "double":
				return Types.DOUBLE;
			case "char":
				return Types.CHAR;
			case "boolean":
				return Types.BOOLEAN;
			case "String":
				return Types.STRING;
			default:
				return null;
		}
	}

	/**
	 * Gets the name of the variable.
	 * @return A String containing the name of the variable.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the type of the variable.
	 * @return The type of the variable.
	 */
	public Types getType() {
		return this.type;
	}

	/**
	 * Checks if the variable is a final variable.
	 * @return True if it is, false otherwise.
	 */
	public boolean isFinal() {
		return this.isFinal;
	}

	@Override
	public String toString() {
		return "type = " + type + ", name = " + name;
	}
}
