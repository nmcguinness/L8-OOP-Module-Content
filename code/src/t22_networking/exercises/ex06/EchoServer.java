package t22_networking.exercises.ex06;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import t21_json_2_jackson_advanced.exercises.ex01.ClientRequest;
import t21_json_2_jackson_advanced.exercises.ex01.ServerResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Accepts one client, answers each line it sends, and stops when that client leaves.
 */
public class EchoServer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ServerSocket serverSocket;

    // Port 0 asks the operating system for any free port; port() says which one it chose
    public EchoServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public int port() {
        return serverSocket.getLocalPort();
    }

    // The reply to one line: echoes the request type back in the data field
    String reply(String line) throws JsonProcessingException {
        ServerResponse<String> response;
        try {
            ClientRequest request = mapper.readValue(line, ClientRequest.class);
            response = ServerResponse.ok("echo", request.getRequestType());
        } catch (JsonProcessingException e) {
            response = ServerResponse.error("not a valid request");
        }
        return mapper.writeValueAsString(response);
    }

    public void serveOneClient() throws IOException {
        try (ServerSocket ss = serverSocket;
             Socket client = ss.accept();
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8),
                     true)) {
            String line;
            while ((line = in.readLine()) != null) {
                // println, not print: the newline ends the message and triggers the flush
                out.println(reply(line));
            }
        }
    }
}
