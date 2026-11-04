package t20_json_1_jackson_basics.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t20_json_1_jackson_basics.exercises.ex01.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t20 ex03 - a generic ServerResponse wrapper")
class ExerciseTest {

    @Test
    void toJson_response_includesStatusAndNestedData() throws Exception {
        String json = Exercise.toJson(
                ServerResponse.ok("Player found", new Player("Alice", 95)), Exercise.MAPPER);

        assertTrue(json.contains("\"status\":\"OK\""), json);
        assertTrue(json.contains("\"name\":\"Alice\""), json);
    }

    @Test
    void fromJson_dataComesBackAsPlayerNotMap() throws Exception {
        String json = Exercise.toJson(
                ServerResponse.ok("Player found", new Player("Alice", 95)), Exercise.MAPPER);

        ServerResponse<Player> back = Exercise.fromJson(json);

        assertInstanceOf(Player.class, back.getData());
        assertEquals("Alice", back.getData().getName());
    }

    @Test
    void toJson_doesNotSendIsOkAsAField() throws Exception {
        String json = Exercise.toJson(ServerResponse.error("no"), Exercise.MAPPER);

        assertFalse(json.contains("\"ok\""), json);
    }

    @Test
    void toJson_nullArguments_throw() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.toJson(null, Exercise.MAPPER));
    }
}
