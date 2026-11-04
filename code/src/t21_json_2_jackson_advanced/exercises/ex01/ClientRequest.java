package t21_json_2_jackson_advanced.exercises.ex01;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The request envelope: a requestType that says what the client wants, and a payload
 * whose shape depends on that type. The payload stays an untyped JsonNode so
 * that one envelope class serves every request type.
 */
public class ClientRequest {

    private String requestType;
    private JsonNode payload;

    public ClientRequest() {
        this(null, null);
    }

    public ClientRequest(String requestType, JsonNode payload) {
        this.requestType = requestType;
        this.payload = payload;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}
