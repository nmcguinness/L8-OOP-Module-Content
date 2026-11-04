package assessments.gca.gca2.server;

import assessments.gca.gca2.service.ClientDispatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Runs a real ClientHandler over loopback. None of these requests reach the DAO, so no MySQL.
@DisplayName("GCA2 ClientHandler - every line gets a reply")
@Timeout(10)
class ClientHandlerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    @BeforeEach
    void connect() throws IOException {
        try (ServerSocket server = new ServerSocket(0)) {
            client = new Socket("localhost", server.getLocalPort());
            Socket accepted = server.accept();
            ClientDispatcher dispatcher = new ClientDispatcher(null);
            new Thread(new ClientHandler(accepted, dispatcher, mapper)).start();
        }
        in = new BufferedReader(
                new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(
                new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    @AfterEach
    void close() throws IOException {
        client.close();
    }

    private ServerResponse<?> reply() throws IOException {
        return mapper.readValue(in.readLine(), ServerResponse.class);
    }

    @Test
    void invalidJson_getsAnErrorAndTheSessionContinues() throws IOException {
        out.println("not json");
        assertEquals("ERROR", reply().getStatus());

        out.println("{\"requestType\":\"NO_SUCH_TYPE\",\"payload\":{}}");
        assertTrue(reply().getMessage().contains("Unknown request type"));
    }

    @Test
    void missingRequestType_getsAnError() throws IOException {
        out.println("{\"requestType\":null,\"payload\":{}}");
        assertEquals("ERROR", reply().getStatus());
    }

    @Test
    void disconnect_getsAReplyThenTheServerCloses() throws IOException {
        out.println("{\"requestType\":\"DISCONNECT\",\"payload\":{}}");

        assertEquals("OK", reply().getStatus());
        assertNull(in.readLine());
    }
}
