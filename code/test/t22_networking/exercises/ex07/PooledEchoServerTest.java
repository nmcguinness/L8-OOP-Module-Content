package t22_networking.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t22 ex07 - several clients at once")
class PooledEchoServerTest {

    @Test
    @Timeout(5)
    void twoOpenClients_bothGetAnswers() throws Exception {
        try (PooledEchoServer server = new PooledEchoServer(0)) {
            new Thread(server::start).start();

            try (Exercise.Connection a = new Exercise.Connection(server.port());
                 Exercise.Connection b = new Exercise.Connection(server.port())) {
                // b is answered while a is still connected
                assertTrue(b.send("{\"requestType\":\"B\",\"payload\":{}}").contains("\"data\":\"B\""));
                assertTrue(a.send("{\"requestType\":\"A\",\"payload\":{}}").contains("\"data\":\"A\""));
            }
        }
    }
}
