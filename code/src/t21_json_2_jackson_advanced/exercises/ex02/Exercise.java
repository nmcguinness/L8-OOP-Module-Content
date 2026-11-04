package t21_json_2_jackson_advanced.exercises.ex02;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import t21_json_2_jackson_advanced.exercises.ex01.ClientRequest;

public class Exercise {

    public static void run() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RequestDispatcher dispatcher = new RequestDispatcher();

        String[] requests = {
            "{\"requestType\":\"PING\",\"payload\":{}}",
            "{\"requestType\":\"ECHO\",\"payload\":{\"msg\":\"hello\",\"n\":3}}",
            "{\"requestType\":\"DANCE\",\"payload\":{}}"
        };

        for (String json : requests) {
            ClientRequest request = mapper.readValue(json, ClientRequest.class);
            System.out.println(request.getRequestType() + " -> " + dispatcher.dispatch(request));
        }
    }
}
