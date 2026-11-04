package t20_json_1_jackson_basics.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t20_json_1_jackson_basics.exercises.ex01.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("t20 ex02 - serialise a List with TypeReference")
class ExerciseTest {

    @Test
    void roundTrip_list_keepsOrder() throws Exception {
        List<Player> players = List.of(new Player("A", 1), new Player("B", 2));

        List<Player> back = Exercise.fromJson(Exercise.toJson(players));

        assertEquals(2, back.size());
        assertEquals("B", back.get(1).getName());
    }

    @Test
    void fromJson_elementsArePlayersNotMaps() throws Exception {
        List<Player> back = Exercise.fromJson("[{\"name\":\"A\",\"score\":1}]");

        assertInstanceOf(Player.class, back.get(0));
    }

    @Test
    void fromJson_emptyArray_returnsEmptyList() throws Exception {
        assertEquals(0, Exercise.fromJson("[]").size());
    }
}
