package t21_json_2_jackson_advanced.exercises.ex02;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import t21_json_2_jackson_advanced.exercises.ex01.ClientRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Routes a request to the handler registered for its type. Adding a request
 * type means adding one entry to the map; dispatch() never changes.
 */
public class RequestDispatcher {

    static final String UNKNOWN_TYPE = "{\"status\":\"ERROR\",\"message\":\"unknown type\"}";

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, RequestHandler> handlers = new HashMap<>();

    public RequestDispatcher() {
        handlers.put("PING", payload -> "{\"status\":\"OK\"}");
        handlers.put("ECHO", payload -> mapper.writeValueAsString(payload));
    }

    public String dispatch(ClientRequest request) throws JsonProcessingException {
        if (request == null || request.getRequestType() == null) {
            return UNKNOWN_TYPE;
        }
        RequestHandler handler = handlers.get(request.getRequestType());
        if (handler == null) {
            return UNKNOWN_TYPE;
        }
        return handler.handle(request.getPayload());
    }
}
