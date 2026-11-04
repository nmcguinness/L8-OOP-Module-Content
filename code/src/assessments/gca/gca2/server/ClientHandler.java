package assessments.gca.gca2.server;

import assessments.gca.gca2.service.ClientDispatcher;
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
 * Handles one connected client for the duration of its session.
 * Reads newline-delimited JSON request lines, delegates each to ClientDispatcher,
 * and writes the JSON response back. Every line gets exactly one reply, including a
 * malformed line and DISCONNECT. Loops until the client sends DISCONNECT or the
 * connection closes.
 * Implements Runnable so it can be submitted to an ExecutorService.
 *
 * @author OOP Teaching Team
 */
public class ClientHandler implements Runnable {

    // === Fields ===
    private final Socket socket;
    private final ClientDispatcher dispatcher;
    private final ObjectMapper mapper;

    // === Constructors ===

    // Creates: a ClientHandler for the given socket, dispatcher, and JSON mapper
    public ClientHandler(Socket socket, ClientDispatcher dispatcher, ObjectMapper mapper) {
        this.socket = socket;
        this.dispatcher = dispatcher;
        this.mapper = mapper;
    }

    // === Public API ===

    // Handles: the full client session - reads requests until DISCONNECT or socket closes
    @Override
    public void run() {
        try (
            BufferedReader in  = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter    out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                ClientRequest request;
                try {
                    request = mapper.readValue(line, ClientRequest.class);
                }
                catch (JsonProcessingException e) {
                    // A bad line ends that request, not the session
                    send(out, ServerResponse.error("Malformed request: not valid JSON"));
                    continue;
                }

                if ("DISCONNECT".equals(request.getRequestType())) {
                    send(out, ServerResponse.ok("Goodbye", null));
                    System.out.println("ClientHandler: client disconnected cleanly");
                    break;
                }

                send(out, dispatcher.dispatch(request));
            }
        }
        catch (IOException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        }
        finally {
            closeSocket();
        }
    }

    // === Helpers ===

    // Sends: one response to the client as a single line of JSON
    private void send(PrintWriter out, ServerResponse<?> response) throws JsonProcessingException {
        out.println(mapper.writeValueAsString(response));
    }

    // Closes: the client socket, suppressing any IOException
    private void closeSocket() {
        try {
            if (!socket.isClosed())
                socket.close();
        }
        catch (IOException e) {
            System.err.println("ClientHandler.closeSocket error: " + e.getMessage());
        }
    }
}
