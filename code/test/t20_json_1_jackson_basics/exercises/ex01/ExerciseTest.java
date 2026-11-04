package t20_json_1_jackson_basics.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t20 ex01 - serialise and deserialise a POJO")
class ExerciseTest {

    @Test
    void toJson_player_usesGetterNamesAsKeys() throws Exception {
        String json = Exercise.toJson(new Player("Alice", 95));

        assertTrue(json.contains("\"name\":\"Alice\""), json);
        assertTrue(json.contains("\"score\":95"), json);
    }

    @Test
    void roundTrip_player_keepsNameAndScore() throws Exception {
        Player back = Exercise.fromJson(Exercise.toJson(new Player("Alice", 95)));

        assertEquals("Alice", back.getName());
        assertEquals(95, back.getScore());
    }
}
