package t21_json_2_jackson_advanced.exercises.ex03;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Reads fields from a request payload without trusting the client.
 */
public final class Payloads {

    private Payloads() {
    }

    // The field as an int; throws if it is missing or is not a JSON whole number
    public static int requireInt(JsonNode payload, String field) {
        if (payload == null) {
            throw new IllegalArgumentException("payload is missing");
        }
        JsonNode value = payload.get(field);
        if (value == null || value.isNull()) {
            throw new IllegalArgumentException("missing field '" + field + "'");
        }
        // isInt() is false for "42" (a string), 4.2 and true: no silent conversion
        if (!value.isInt()) {
            throw new IllegalArgumentException("field '" + field + "' must be a whole number");
        }
        return value.asInt();
    }

    // The field as text, or fallback if it is missing or is not a JSON string
    public static String optionalString(JsonNode payload, String field, String fallback) {
        if (payload == null) {
            return fallback;
        }
        JsonNode value = payload.get(field);
        if (value == null || !value.isTextual()) {
            return fallback;
        }
        return value.asText();
    }
}
