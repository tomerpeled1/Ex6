package oop.ex6;

public class VariableWrapper {
	enum Types {
		INT, STRING,DOUBLE, BOOLEAN, CHAR
	}

	private String name;
	private boolean hasValue;
	private Types type;

	/**
	 * Constructs a VariableWrapper object with a type, hasValue and name.
	 * @param type The type of the variable.
	 * @param hasValue A boolean indicates if the variable has a value or not.
	 * @param name The name of the variable.
	 */
	public VariableWrapper (String type, boolean hasValue, String name) {
		this.hasValue = hasValue;
		this.type = stringToTypes(type);
		this.name = name;
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

	private static Types stringToTypes(String str) {
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


}
