package t21_json_2_jackson_advanced.exercises.ex02;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t21_json_2_jackson_advanced.exercises.ex01.ClientRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t21 ex02 - routing with a map of handlers")
class RequestDispatcherTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RequestDispatcher dispatcher = new RequestDispatcher();

    private ClientRequest request(String json) throws Exception {
        return mapper.readValue(json, ClientRequest.class);
    }

    @Test
    void ping_ignoresPayload_returnsOk() throws Exception {
        assertEquals("{\"status\":\"OK\"}",
                dispatcher.dispatch(request("{\"requestType\":\"PING\",\"payload\":{\"x\":1}}")));
    }

    @Test
    void echo_returnsPayloadAsJson() throws Exception {
        assertEquals("{\"msg\":\"hello\",\"n\":3}",
                dispatcher.dispatch(request("{\"requestType\":\"ECHO\",\"payload\":{\"msg\":\"hello\",\"n\":3}}")));
    }

    @Test
    void unknownType_returnsErrorReplyNotException() throws Exception {
        assertEquals(RequestDispatcher.UNKNOWN_TYPE, dispatcher.dispatch(request("{\"requestType\":\"DANCE\",\"payload\":{}}")));
    }
}
