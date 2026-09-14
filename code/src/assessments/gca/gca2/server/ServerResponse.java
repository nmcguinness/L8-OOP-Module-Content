package assessments.gca.gca2.server;

/**
 * Generic wrapper for all server replies.
 * Use the static factories ok() and error() to create responses.
 * Jackson requires a public no-arg constructor and public setters to deserialise this class.
 *
 * @param <T> the type of the data payload
 * @author OOP Teaching Team
 */
public class ServerResponse<T> {

    // === Fields ===
    private String _status;
    private String _message;
    private T      _data;

    // === Constructors ===

    // Creates: an empty ServerResponse required by Jackson for deserialisation
    public ServerResponse() {
        _status  = "";
        _message = "";
        _data    = null;
    }

    // Creates: a ServerResponse with all fields set
    private ServerResponse(String status, String message, T data) {
        _status  = status;
        _message = message;
        _data    = data;
    }

    // === Public API ===

    // Creates: a successful response carrying a data payload
    public static <T> ServerResponse<T> ok(String message, T data) {
        return new ServerResponse<>("OK", message, data);
    }

    // Creates: an error response with a message and no data payload
    public static <T> ServerResponse<T> error(String message) {
        return new ServerResponse<>("ERROR", message, null);
    }

    // Checks: whether this response has status OK
    public boolean isOk() { return "OK".equals(_status); }

    // Gets: the status string - either "OK" or "ERROR"
    public String getStatus()  { return _status; }

    // Gets: the human-readable outcome message
    public String getMessage() { return _message; }

    // Gets: the data payload; null when this is an error response
    public T getData() { return _data; }

    // Sets: the status (used by Jackson during deserialisation)
    public void setStatus(String status)   { _status  = status; }

    // Sets: the message (used by Jackson during deserialisation)
    public void setMessage(String message) { _message = message; }

    // Sets: the data payload (used by Jackson during deserialisation)
    public void setData(T data)            { _data    = data; }
}
