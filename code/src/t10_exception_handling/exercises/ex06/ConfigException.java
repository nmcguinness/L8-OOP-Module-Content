package t10_exception_handling.exercises.ex06;

/**
 * Checked: a missing or malformed settings file is outside the program's control,
 * and every caller must decide what to do, for example fall back to defaults.
 */
public class ConfigException extends Exception {

    private final int lineNumber;

    // For a problem with one line of the file
    public ConfigException(String message, int lineNumber) {
        super(message + " (line " + lineNumber + ")");
        this.lineNumber = lineNumber;
    }

    // For a lower-level failure: keep it as the cause so its stack trace survives
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
        lineNumber = 0;
    }

    // 0 when the problem is not tied to a single line
    public int getLineNumber() {
        return lineNumber;
    }
}
