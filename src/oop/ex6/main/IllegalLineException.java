package oop.ex6.main;

/**
 * an exception that is thrown where an error in a line happens.
 */
public class IllegalLineException extends Exception {
    /**
     * creates a new exception
     * @param message the massage that will be printed. an informative error massege.
     */
    public IllegalLineException(String message) {
        super(message);
    }
}
