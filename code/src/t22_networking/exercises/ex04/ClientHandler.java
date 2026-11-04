package t22_networking.exercises.ex04;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * One client session: socket I/O and JSON parsing only. All request logic is
 * in RequestDispatcher.
 */
public class ClientHandler implements Runnable {

    private static final String FALLBACK_ERROR =
        "{\"status\":\"ERROR\",\"message\":\"internal server error\",\"data\":null}";

    private final Socket socket;
    private final RequestDispatcher dispatcher;
    private final ObjectMapper mapper = new ObjectMapper();

    public ClientHandler(Socket socket, RequestDispatcher dispatcher) {
        if (socket == null)
            throw new IllegalArgumentException("socket is required");
        if (dispatcher == null)
            throw new IllegalArgumentException("dispatcher is required");
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket;
             BufferedReader in = new BufferedReader(
                 new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                 new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                out.println(toJson(handleLine(line)));
            }
        }
        catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
    }

    // Parses one line and dispatches it; package-private so a test can call it directly
    ServerResponse<?> handleLine(String line) {
        try {
            return dispatcher.dispatch(mapper.readValue(line, ClientRequest.class));
        }
        catch (JsonProcessingException e) {
            return ServerResponse.error("malformed request: not valid JSON for this protocol");
        }
    }

    private String toJson(ServerResponse<?> response) {
        try {
            return mapper.writeValueAsString(response);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return FALLBACK_ERROR;
        }
    }
}
