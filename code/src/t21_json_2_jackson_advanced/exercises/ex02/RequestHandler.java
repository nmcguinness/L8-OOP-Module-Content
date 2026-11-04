package t21_json_2_jackson_advanced.exercises.ex02;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

// One handler per request type; returns the reply as a JSON string
@FunctionalInterface
public interface RequestHandler {
    String handle(JsonNode payload) throws JsonProcessingException;
}
