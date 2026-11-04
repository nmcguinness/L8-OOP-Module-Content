package t22_networking.exercises.ex05;

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
 * One client session. Ends when the client closes the connection, or after the
 * reply to a DISCONNECT request has been sent.
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
                ClientRequest request = parse(line);
                ServerResponse<?> response = request == null
                    ? ServerResponse.error("malformed request: not valid JSON for this protocol")
                    : dispatcher.dispatch(request);

                out.println(toJson(response));

                // Decided from the parsed request, not by searching the raw text
                if (request != null && request.getRequestType() == RequestType.DISCONNECT) {
                    System.out.println("Client sent DISCONNECT; closing session");
                    break;
                }
            }
        }
        catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
    }

    private ClientRequest parse(String line) {
        try {
            return mapper.readValue(line, ClientRequest.class);
        }
        catch (JsonProcessingException e) {
            return null;
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
