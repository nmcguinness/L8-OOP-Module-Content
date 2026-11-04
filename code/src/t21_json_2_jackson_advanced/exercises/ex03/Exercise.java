package t21_json_2_jackson_advanced.exercises.ex03;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Exercise {

    public static void run() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String[] payloads = {"{\"id\":42}", "{\"id\":\"42\"}", "{}"};

        for (String json : payloads) {
            JsonNode payload = mapper.readTree(json);
            try {
                System.out.println(json + " -> id " + Payloads.requireInt(payload, "id"));
            } catch (IllegalArgumentException e) {
                System.out.println(json + " -> rejected: " + e.getMessage());
            }
        }

        JsonNode named = mapper.readTree("{\"name\":\"Ana\"}");
        System.out.println("name: " + Payloads.optionalString(named, "name", "(none)"));
        System.out.println("title: " + Payloads.optionalString(named, "title", "(none)"));
    }
}
