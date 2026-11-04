package t10_exception_handling.exercises.ex08;

public class ScoreFileException extends Exception {

    private final int lineNumber;

    public ScoreFileException(String message, int lineNumber, Throwable cause) {
        super(lineNumber > 0 ? message + " (line " + lineNumber + ")" : message, cause);
        this.lineNumber = lineNumber;
    }

    // 0 when the problem is not tied to a single line
    public int getLineNumber() {
        return lineNumber;
    }
}
