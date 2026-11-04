package assessments.gca.gca2.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents one JSON request sent by a client.
 * Carries a requestType string and a key-value payload map.
 * Use getString(), getInt(), and getBoolean() to extract payload values safely.
 *
 * @author OOP Teaching Team
 */
public class ClientRequest {

    // === Fields ===
    private String requestType;
    private Map<String, Object> payload;

    // === Constructors ===

    // Creates: an empty ClientRequest required by Jackson for deserialisation
    public ClientRequest() {
        requestType = "";
        payload = new HashMap<>();
    }

    // === Public API ===

    // Gets: the request type string, e.g. "INSERT" or "FIND_BY_ID"
    public String getRequestType() { return requestType; }

    // Sets: the request type (used by Jackson during deserialisation)
    public void setRequestType(String requestType) { this.requestType = requestType; }

    // Gets: the raw payload map (used by Jackson during deserialisation)
    public Map<String, Object> getPayload() { return payload; }

    // Sets: the payload map (used by Jackson during deserialisation)
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    // Gets: a String value from the payload, or null if the key is absent
    public String getString(String key) {
        Object value = payload.get(key);
        return (value != null) ? value.toString() : null;
    }

    // Gets: an int value from the payload, or -1 if the key is absent or non-numeric
    public int getInt(String key) {
        Object value = payload.get(key);
        if (value instanceof Number)
            return ((Number) value).intValue();
        return -1;
    }

    // Gets: a boolean value from the payload, or false if the key is absent
    public boolean getBoolean(String key) {
        Object value = payload.get(key);
        if (value instanceof Boolean)
            return (Boolean) value;
        return false;
    }
}
