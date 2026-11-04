package t17_streams_api.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t17 ex02 - map and join")
class ExerciseTest {

    @Test
    void upperJoined_example() {
        assertEquals("RED | GREEN | BLUE", Exercise.upperJoined(List.of("red", "green", "blue")));
    }

    @Test
    void upperJoined_oneWord_hasNoSeparator() {
        assertEquals("SOLO", Exercise.upperJoined(List.of("solo")));
    }

    @Test
    void upperJoined_emptyOrNull_returnsEmptyString() {
        assertEquals("", Exercise.upperJoined(List.of()));
        assertEquals("", Exercise.upperJoined(null));
    }
}
