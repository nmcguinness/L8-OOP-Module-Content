package t21_json_2_jackson_advanced.exercises.ex06;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t21_json_2_jackson_advanced.exercises.ex01.ClientRequest;
import t21_json_2_jackson_advanced.exercises.ex01.ServerResponse;
import t21_json_2_jackson_advanced.exercises.ex03.Payloads;
import t21_json_2_jackson_advanced.exercises.ex04.Exercise;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class is the solution to Exercise 06: the exercise is to write these tests.
 */
@DisplayName("t21 ex06 - protocol round-trip tests")
class ProtocolRoundTripTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void clientRequest_roundTrip_preservesTypeAndPayload() throws Exception {
        JsonNode payload = mapper.readTree("{\"id\":42}");
        ClientRequest original = new ClientRequest("GET_PLAYER", payload);

        String json = mapper.writeValueAsString(original);
        ClientRequest restored = mapper.readValue(json, ClientRequest.class);

        // ClientRequest has no equals, so compare field by field; JsonNode does have equals
        assertEquals(original.getRequestType(), restored.getRequestType());
        assertEquals(original.getPayload(), restored.getPayload());
    }

    @Test
    void base64_roundTrip_returnsOriginalBytes() {
        byte[] original = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        byte[] restored = Exercise.decodeFromBase64(Exercise.encodeToBase64(original));

        assertArrayEquals(original, restored);
    }

    @Test
    void requireInt_missingField_throwsIllegalArgumentException() throws Exception {
        JsonNode payload = mapper.readTree("{}");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> Payloads.requireInt(payload, "id"));
        assertEquals("missing field 'id'", e.getMessage());
    }

    @Test
    void serverResponse_error_hasStatusErrorAndNullData() throws Exception {
        String json = mapper.writeValueAsString(ServerResponse.error("no such player"));

        ServerResponse<String> restored =
                mapper.readValue(json, new TypeReference<ServerResponse<String>>() { });

        assertFalse(restored.isOk());
        assertEquals("ERROR", restored.getStatus());
        assertNull(restored.getData());
        assertEquals("no such player", restored.getMessage());
    }
}
