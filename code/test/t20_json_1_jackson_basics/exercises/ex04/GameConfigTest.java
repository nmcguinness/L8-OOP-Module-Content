package t20_json_1_jackson_basics.exercises.ex04;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t20 ex04 - @JsonProperty and ignoring unknown keys")
class GameConfigTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serialise_usesMaxPlayersSnakeCaseKey() throws Exception {
        String json = mapper.writeValueAsString(new GameConfig(4));

        assertTrue(json.contains("\"max_players\":4"), json);
        assertFalse(json.contains("maxPlayers"), json);
    }

    @Test
    void deserialise_readsMaxPlayersAndIgnoresUnknownKeys() throws Exception {
        GameConfig parsed = mapper.readValue(Exercise.INCOMING, GameConfig.class);

        assertEquals(8, parsed.getMaxPlayers());
    }
}
