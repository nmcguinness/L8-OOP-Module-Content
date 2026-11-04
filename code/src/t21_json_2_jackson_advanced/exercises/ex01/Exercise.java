package t21_json_2_jackson_advanced.exercises.ex01;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Exercise {

    static final ObjectMapper MAPPER = new ObjectMapper();

    static ClientRequest parse(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, ClientRequest.class);
    }

    public static void run() throws JsonProcessingException {
        String json = "{\"requestType\":\"GET_PLAYER\",\"payload\":{\"id\":42}}";

        ClientRequest request = parse(json);

        // Read the type first; only then do we know how to read the payload
        System.out.println("Type: " + request.getRequestType());
        System.out.println("Id: " + request.getPayload().get("id").asInt());
    }
}
