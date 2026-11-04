package t10_exception_handling.exercises.ex07;

/**
 * A resource whose close() always fails, used to show suppressed exceptions.
 */
public class FlakyResource implements AutoCloseable {

    public void work() {
        throw new IllegalStateException("work failed");
    }

    @Override
    public void close() {
        throw new IllegalStateException("close failed");
    }
}
