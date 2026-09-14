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
    private String              _requestType;
    private Map<String, Object> _payload;

    // === Constructors ===

    // Creates: an empty ClientRequest required by Jackson for deserialisation
    public ClientRequest() {
        _requestType = "";
        _payload     = new HashMap<>();
    }

    // === Public API ===

    // Gets: the request type string, e.g. "INSERT" or "FIND_BY_ID"
    public String getRequestType() { return _requestType; }

    // Sets: the request type (used by Jackson during deserialisation)
    public void setRequestType(String requestType) { _requestType = requestType; }

    // Gets: the raw payload map (used by Jackson during deserialisation)
    public Map<String, Object> getPayload() { return _payload; }

    // Sets: the payload map (used by Jackson during deserialisation)
    public void setPayload(Map<String, Object> payload) { _payload = payload; }

    // Gets: a String value from the payload, or null if the key is absent
    public String getString(String key) {
        Object value = _payload.get(key);
        return (value != null) ? value.toString() : null;
    }

    // Gets: an int value from the payload, or -1 if the key is absent or non-numeric
    public int getInt(String key) {
        Object value = _payload.get(key);
        if (value instanceof Number)
            return ((Number) value).intValue();
        return -1;
    }

    // Gets: a boolean value from the payload, or false if the key is absent
    public boolean getBoolean(String key) {
        Object value = _payload.get(key);
        if (value instanceof Boolean)
            return (Boolean) value;
        return false;
    }
}
