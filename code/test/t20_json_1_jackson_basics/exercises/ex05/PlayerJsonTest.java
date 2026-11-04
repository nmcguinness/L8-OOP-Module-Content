package t20_json_1_jackson_basics.exercises.ex05;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class is the solution to Exercise 05: the exercise is to write these tests.
 */
@DisplayName("t20 ex05 - round-trip JSON tests for Player")
class PlayerJsonTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serialise_player_producesExpectedJson() throws Exception {
        String json = mapper.writeValueAsString(new Player("Alice", 95));

        assertTrue(json.contains("\"name\":\"Alice\""), json);
    }

    @Test
    void roundTrip_player_returnsEqualObject() throws Exception {
        Player original = new Player("Alice", 95);

        String json = mapper.writeValueAsString(original);
        Player back = mapper.readValue(json, Player.class);

        assertEquals(original, back);
    }
}
