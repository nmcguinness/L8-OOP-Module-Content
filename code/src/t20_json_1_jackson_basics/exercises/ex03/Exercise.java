package t20_json_1_jackson_basics.exercises.ex03;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import t20_json_1_jackson_basics.exercises.ex01.Player;

public class Exercise {

    static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> String toJson(ServerResponse<T> response, ObjectMapper mapper)
            throws JsonProcessingException {
        if (response == null || mapper == null) {
            throw new IllegalArgumentException("response and mapper are required");
        }
        return mapper.writeValueAsString(response);
    }

    // Without the TypeReference, data would come back as a LinkedHashMap, not a Player
    static ServerResponse<Player> fromJson(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, new TypeReference<ServerResponse<Player>>() { });
    }

    public static void run() throws JsonProcessingException {
        ServerResponse<Player> response = ServerResponse.ok("Player found", new Player("Alice", 95));

        String json = toJson(response, MAPPER);
        System.out.println("JSON: " + json);

        ServerResponse<Player> back = fromJson(json);
        System.out.println("Status: " + back.getStatus());
        System.out.println("Name: " + back.getData().getName());
    }

    public static void main(String[] args) throws JsonProcessingException {
        run();
    }
}
