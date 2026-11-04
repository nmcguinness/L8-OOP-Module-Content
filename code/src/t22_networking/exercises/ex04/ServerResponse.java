package t22_networking.exercises.ex04;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class ServerResponse<T> {

    // === Fields ===
    // "OK" or "ERROR"
    private final String status;
    private final String message;
    private final T data;

    // === Constructors ===
    // Creates: a response with all fields set
    public ServerResponse(String status, String message, T data) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // === Public API ===
    // Creates: a successful response with data
    public static <T> ServerResponse<T> ok(String message, T data) {
        return new ServerResponse<>("OK", message, data);
    }

    // Creates: an error response with no data
    public static <T> ServerResponse<T> error(String message) {
        return new ServerResponse<>("ERROR", message, null);
    }

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public T getData() { return data; }

    // Derived from status, so it is not sent as a separate JSON field
    @JsonIgnore
    public boolean isOk() { return "OK".equals(status); }
}
