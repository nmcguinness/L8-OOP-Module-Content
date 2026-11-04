package t21_json_2_jackson_advanced.exercises.ex03;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t21 ex03 - defensive payload reading")
class PayloadsTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode json(String text) throws Exception {
        return mapper.readTree(text);
    }

    @Test
    void requireInt_presentAndCorrect_returnsValue() throws Exception {
        assertEquals(42, Payloads.requireInt(json("{\"id\":42}"), "id"));
    }

    @Test
    void requireInt_stringInsteadOfNumber_isRejectedNotConverted() throws Exception {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> Payloads.requireInt(json("{\"id\":\"42\"}"), "id"));
        assertEquals("field 'id' must be a whole number", e.getMessage());
    }

    @Test
    void requireInt_missingField_namesTheField() throws Exception {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> Payloads.requireInt(json("{}"), "id"));
        assertEquals("missing field 'id'", e.getMessage());
    }

    @Test
    void requireInt_nullPayload_throws() {
        assertThrows(IllegalArgumentException.class, () -> Payloads.requireInt(null, "id"));
    }

    @Test
    void optionalString_presentAndCorrect_returnsValue() throws Exception {
        assertEquals("Ana", Payloads.optionalString(json("{\"name\":\"Ana\"}"), "name", "x"));
    }

    @Test
    void optionalString_wrongType_returnsFallback() throws Exception {
        assertEquals("x", Payloads.optionalString(json("{\"name\":7}"), "name", "x"));
    }

    @Test
    void optionalString_missingField_returnsFallback() throws Exception {
        assertEquals("x", Payloads.optionalString(json("{}"), "name", "x"));
    }

    @Test
    void optionalString_nullPayload_returnsFallback() {
        assertEquals("x", Payloads.optionalString(null, "name", "x"));
    }
}
