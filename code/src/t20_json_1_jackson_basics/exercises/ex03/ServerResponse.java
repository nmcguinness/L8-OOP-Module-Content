package t20_json_1_jackson_basics.exercises.ex03;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The reply envelope: a status of "OK" or "ERROR", a message for people, and the data.
 *
 * @param <T> the type of the data; null on an error
 */
public class ServerResponse<T> {

    private String status;
    private String message;
    private T data;

    public ServerResponse() {
    }

    private ServerResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ServerResponse<T> ok(String message, T data) {
        return new ServerResponse<>("OK", message, data);
    }

    public static <T> ServerResponse<T> error(String message) {
        return new ServerResponse<>("ERROR", message, null);
    }

    // Not a JSON field: without @JsonIgnore, Jackson would send an extra "ok" key
    @JsonIgnore
    public boolean isOk() {
        return "OK".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
