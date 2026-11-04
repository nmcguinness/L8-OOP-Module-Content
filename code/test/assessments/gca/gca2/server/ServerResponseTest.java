package assessments.gca.gca2.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Solution to t23 Exercise 03: tests for the ServerResponse factories.
 */
@DisplayName("t23 ex03 - ServerResponse factories")
class ServerResponseTest {

    @Test
    void ok_withData_setsStatusOkAndKeepsData() {
        ServerResponse<String> r = ServerResponse.ok("found", "payload");

        assertEquals("OK", r.getStatus());
        assertEquals("found", r.getMessage());
        assertEquals("payload", r.getData());
    }

    @Test
    void ok_withData_isOkReturnsTrue() {
        assertTrue(ServerResponse.ok("found", 1).isOk());
    }

    @Test
    void error_withMessage_setsStatusErrorAndNullData() {
        ServerResponse<String> r = ServerResponse.error("not found");

        assertEquals("ERROR", r.getStatus());
        assertEquals("not found", r.getMessage());
        assertNull(r.getData());
    }

    @Test
    void error_withMessage_isOkReturnsFalse() {
        assertFalse(ServerResponse.error("not found").isOk());
    }

    // Stretch: fails with NullPointerException if isOk() is written status.equals("OK")
    @Test
    void isOk_nullStatus_returnsFalseNotException() {
        ServerResponse<String> r = new ServerResponse<>();
        r.setStatus(null);

        assertFalse(r.isOk());
    }
}
