package assessments.gca.gca2.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Solution to t23 Exercise 04. These tests document what ClientRequest actually
 * does, including the surprising cases, rather than what we might assume.
 */
@DisplayName("t23 ex04 - ClientRequest payload accessors")
class ClientRequestTest {

    private ClientRequest requestWith(String key, Object value) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(key, value);
        ClientRequest request = new ClientRequest();
        request.setPayload(payload);
        return request;
    }

    // ---------- getString ----------

    @Test
    void getString_present_returnsValue() {
        assertEquals("Fix login", requestWith("title", "Fix login").getString("title"));
    }

    @Test
    void getString_missing_returnsNull() {
        assertNull(new ClientRequest().getString("title"));
    }

    @Test
    void getString_wrongType_returnsToStringOfValue() {
        // Not rejected: a number is quietly turned into text
        assertEquals("5", requestWith("title", 5).getString("title"));
    }

    // ---------- getInt ----------

    @Test
    void getInt_present_returnsValue() {
        assertEquals(42, requestWith("id", 42).getInt("id"));
    }

    @Test
    void getInt_missing_returnsMinusOne() {
        assertEquals(-1, new ClientRequest().getInt("id"));
    }

    @Test
    void getInt_wrongType_string_returnsMinusOne() {
        assertEquals(-1, requestWith("id", "42").getInt("id"));
    }

    @Test
    void getInt_decimal_isTruncatedNotRejected() {
        // Any Number is accepted, so 4.9 silently becomes 4
        assertEquals(4, requestWith("id", 4.9).getInt("id"));
    }

    // ---------- getBoolean ----------

    @Test
    void getBoolean_present_returnsValue() {
        assertTrue(requestWith("completed", true).getBoolean("completed"));
    }

    @Test
    void getBoolean_missing_returnsFalse() {
        assertFalse(new ClientRequest().getBoolean("completed"));
    }

    @Test
    void getBoolean_wrongType_string_returnsFalse() {
        // "true" as a string is not a Boolean, so the answer is false
        assertFalse(requestWith("completed", "true").getBoolean("completed"));
    }
}
