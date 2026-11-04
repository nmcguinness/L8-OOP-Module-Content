package t20_json_1_jackson_basics.exercises.ex04;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Exercise {

    static final ObjectMapper MAPPER = new ObjectMapper();

    static final String INCOMING = "{\"max_players\": 8, \"roundDuration\": 60}";

    public static void run() throws JsonProcessingException {
        String json = MAPPER.writeValueAsString(new GameConfig(4));
        System.out.println("Serialised: " + json);
        System.out.println("Uses max_players: " + json.contains("\"max_players\""));

        GameConfig parsed = MAPPER.readValue(INCOMING, GameConfig.class);
        System.out.println("maxPlayers after reading: " + parsed.getMaxPlayers());
    }

    public static void main(String[] args) throws JsonProcessingException {
        run();
    }
}
