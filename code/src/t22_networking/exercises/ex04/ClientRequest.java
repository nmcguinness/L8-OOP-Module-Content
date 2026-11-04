package t22_networking.exercises.ex04;

import java.util.Map;

public class ClientRequest {

    // === Fields ===
    private RequestType requestType;
    private Map<String, Object> payload = Map.of();

    // === Constructors ===
    // Creates: an empty request — needed by Jackson deserialisation
    public ClientRequest() { }

    // Creates: a request ready to send
    public ClientRequest(RequestType requestType, Map<String, Object> payload) {
        this.requestType = requestType;
        setPayload(payload);
    }

    // === Public API ===
    public RequestType getRequestType() { return requestType; }

    public void setRequestType(RequestType requestType) { this.requestType = requestType; }

    public Map<String, Object> getPayload() { return payload; }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload == null ? Map.of() : payload;
    }

    // Gets: a string value from the payload, or null if absent
    public String getString(String key) {
        Object v = payload.get(key);
        return v == null ? null : v.toString();
    }

    // Gets: an integer value from the payload, or -1 if absent or not a number
    public int getInt(String key) {
        Object v = payload.get(key);
        if (v == null)
            return -1;
        try {
            return Integer.parseInt(v.toString());
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
}
