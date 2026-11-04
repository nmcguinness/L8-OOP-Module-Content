package t10_exception_handling.exercises.ex02;

/**
 * Unchecked: an out-of-range score is a programming or input-validation error,
 * so callers are not forced to catch it.
 */
public class InvalidScoreException extends RuntimeException {

    private final int score;

    public InvalidScoreException(int score) {
        super("Score must be 0 to 100, got " + score);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
