package t22_networking.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t22 ex06 - JSON echo over a socket")
class EchoServerTest {

    @Test
    void reply_validRequest_echoesTypeInData() throws Exception {
        EchoServer server = new EchoServer(0);

        assertEquals("{\"status\":\"OK\",\"message\":\"echo\",\"data\":\"PING\"}",
                server.reply("{\"requestType\":\"PING\",\"payload\":{}}"));
    }

    @Test
    void reply_invalidJson_isAnErrorResponse() throws Exception {
        EchoServer server = new EchoServer(0);

        assertEquals("{\"status\":\"ERROR\",\"message\":\"not a valid request\",\"data\":null}",
                server.reply("not json"));
    }

    @Test
    @Timeout(5)
    void roundTrip_overLoopback_clientGetsReply() throws Exception {
        EchoServer server = new EchoServer(0);
        Thread t = new Thread(() -> {
            try {
                server.serveOneClient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();

        String reply = EchoClient.send("localhost", server.port(), "{\"requestType\":\"HI\",\"payload\":{}}");
        t.join();

        assertEquals("{\"status\":\"OK\",\"message\":\"echo\",\"data\":\"HI\"}", reply);
    }
}
