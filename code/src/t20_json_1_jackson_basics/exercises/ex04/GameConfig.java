package t20_json_1_jackson_basics.exercises.ex04;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java calls the field maxPlayers; the JSON key is max_players. Unknown keys,
 * such as roundDuration, are ignored instead of causing an exception.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameConfig {

    private int maxPlayers;

    public GameConfig() {
        this(0);
    }

    public GameConfig(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    // Both the getter and the setter carry the annotation, so reading and writing agree
    @JsonProperty("max_players")
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @JsonProperty("max_players")
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
