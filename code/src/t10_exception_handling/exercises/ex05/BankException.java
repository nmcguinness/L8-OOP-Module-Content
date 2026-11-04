package t10_exception_handling.exercises.ex05;

/**
 * Base type of the bank hierarchy. Unchecked, so callers catch it only where
 * they can do something useful, such as showing a message to the user.
 */
public class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }
}
