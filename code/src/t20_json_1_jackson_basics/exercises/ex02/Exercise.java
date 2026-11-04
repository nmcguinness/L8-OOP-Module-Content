package t20_json_1_jackson_basics.exercises.ex02;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import t20_json_1_jackson_basics.exercises.ex01.Player;

import java.util.List;

public class Exercise {

    static final ObjectMapper MAPPER = new ObjectMapper();

    static String toJson(List<Player> players) throws JsonProcessingException {
        return MAPPER.writeValueAsString(players);
    }

    // List<Player>.class does not exist (type erasure), so a TypeReference carries the full type
    static List<Player> fromJson(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, new TypeReference<List<Player>>() { });
    }

    public static void run() throws JsonProcessingException {
        List<Player> players = List.of(
                new Player("Alice", 95),
                new Player("Ben", 80),
                new Player("Clara", 92));

        String json = toJson(players);
        System.out.println("JSON: " + json);

        List<Player> back = fromJson(json);
        boolean sameFirst = back.get(0).getName().equals(players.get(0).getName());
        System.out.println("First name matches: " + sameFirst);
    }

    public static void main(String[] args) throws JsonProcessingException {
        run();
    }
}
