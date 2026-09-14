package assessments.gca.gca2.server;

import assessments.gca.gca2.service.ClientDispatcher;
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
 * and writes the JSON response back. Loops until the client sends DISCONNECT or
 * the connection closes.
 * Implements Runnable so it can be submitted to an ExecutorService.
 *
 * @author OOP Teaching Team
 */
public class ClientHandler implements Runnable {

    // === Fields ===
    private Socket           _socket;
    private ClientDispatcher _dispatcher;
    private ObjectMapper     _mapper;

    // === Constructors ===

    // Creates: a ClientHandler for the given socket, dispatcher, and JSON mapper
    public ClientHandler(Socket socket, ClientDispatcher dispatcher, ObjectMapper mapper) {
        _socket     = socket;
        _dispatcher = dispatcher;
        _mapper     = mapper;
    }

    // === Public API ===

    // Handles: the full client session - reads requests until DISCONNECT or socket closes
    @Override
    public void run() {
        try (
            BufferedReader in  = new BufferedReader(
                new InputStreamReader(_socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter    out = new PrintWriter(
                new OutputStreamWriter(_socket.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                ClientRequest request = _mapper.readValue(line, ClientRequest.class);

                if ("DISCONNECT".equals(request.getRequestType())) {
                    System.out.println("ClientHandler: client disconnected cleanly");
                    break;
                }

                ServerResponse<?> response = _dispatcher.dispatch(request);
                out.println(_mapper.writeValueAsString(response));
            }
        }
        catch (Exception e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        }
        finally {
            closeSocket();
        }
    }

    // === Helpers ===

    // Closes: the client socket, suppressing any IOException
    private void closeSocket() {
        try {
            if (!_socket.isClosed())
                _socket.close();
        }
        catch (IOException e) {
            System.err.println("ClientHandler.closeSocket error: " + e.getMessage());
        }
    }
}
