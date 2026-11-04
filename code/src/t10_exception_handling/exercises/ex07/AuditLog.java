package t10_exception_handling.exercises.ex07;

import java.util.List;

/**
 * A resource that must always be closed. Implementing AutoCloseable is what
 * lets it appear in the brackets of a try-with-resources statement.
 */
public class AuditLog implements AutoCloseable {

    private final List<String> sink;
    private boolean closed;

    public AuditLog(List<String> sink) {
        if (sink == null) {
            throw new IllegalArgumentException("sink must not be null");
        }
        this.sink = sink;
        this.sink.add("opened");
    }

    public void write(String event) {
        if (closed) {
            throw new IllegalStateException("AuditLog is closed");
        }
        sink.add(event);
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        sink.add("closed");
    }
}
