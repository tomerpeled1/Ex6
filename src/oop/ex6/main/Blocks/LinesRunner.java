package oop.ex6.main.Blocks;

/**
 * this class runs on the lines and keeps the number of line it is in.
 */
public class LinesRunner {
    private String[] lines;
    private int currentPlace;

    /**
     * Constructs a LinesRunner object with the lines to run on.
     * @param lines The lines to run on.
     */
    public LinesRunner(String[] lines) {
        this.lines = lines;
        currentPlace = 0;
    }

    /**
     * Gets the next line in the array.
     * @return A String with the line.
     */
    public String getNextLine() {
        if (currentPlace >= lines.length) {
            return null;
        }
        return lines[currentPlace++];
    }

    /**
     * Gets the number of the line that was returned before.
     * @return the number of the last line returned.
     */
    public int getLineNumber() {
        return currentPlace;
    }

    /**
     * sets the runner's lines t5o be the parameter the function gets.
     * @param lines list of lines of the file
     */
    public void setLines(String[] lines) {
        this.lines = lines;
    }
}
