package oop.ex6.main.Blocks;

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
    public String GetNextLine() {
        return lines[currentPlace++];
    }

    /**
     * Gets the number of the line that was returned before.
     * @return the number of the last line returned.
     */
    public int GetLineNumber() {
        return currentPlace;
    }
}
