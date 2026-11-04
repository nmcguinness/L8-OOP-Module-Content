package t20_json_1_jackson_basics.exercises.ex01;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Exercise {

    // One mapper for the whole program; creating one is expensive
    static final ObjectMapper MAPPER = new ObjectMapper();

    static String toJson(Player player) throws JsonProcessingException {
        return MAPPER.writeValueAsString(player);
    }

    static Player fromJson(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, Player.class);
    }

    public static void run() throws JsonProcessingException {
        Player alice = new Player("Alice", 95);

        String json = toJson(alice);
        System.out.println("JSON: " + json);

        Player back = fromJson(json);
        System.out.println("Name: " + back.getName() + ", score: " + back.getScore());
    }

    public static void main(String[] args) throws JsonProcessingException {
        run();
    }
}
