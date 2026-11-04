package t10_exception_handling.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t10 ex03 - Optional for routine absence, checked exception for required data")
class PlayerRepositoryTest {

    private final PlayerRepository repo = new PlayerRepository(Map.of("ana", 90));

    @Test
    void findScore_knownPlayer_returnsScore() {
        assertEquals(Optional.of(90), repo.findScore("ana"));
    }

    @Test
    void findScore_unknownPlayer_returnsEmptyAndDoesNotThrow() {
        assertEquals(Optional.empty(), repo.findScore("zoe"));
    }

    @Test
    void requireScore_knownPlayer_returnsScore() throws PlayerNotFoundException {
        assertEquals(90, repo.requireScore("ana"));
    }

    @Test
    void requireScore_unknownPlayer_throwsWithName() {
        PlayerNotFoundException e = assertThrows(PlayerNotFoundException.class,
                () -> repo.requireScore("zoe"));

        assertEquals("zoe", e.getName());
    }

    @Test
    void playerNotFoundException_isChecked() {
        Exception e = new PlayerNotFoundException("x");

        assertFalse(e instanceof RuntimeException);
        assertInstanceOf(Exception.class, e);
    }

    @Test
    void constructor_copiesTheMap() {
        Map<String, Integer> source = new HashMap<>(Map.of("ana", 90));
        PlayerRepository copy = new PlayerRepository(source);

        source.put("ben", 10);

        assertEquals(Optional.empty(), copy.findScore("ben"));
    }
}
