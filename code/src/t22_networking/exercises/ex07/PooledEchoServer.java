package t22_networking.exercises.ex07;

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
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Keeps accepting clients and serves each one on a pool thread, so a slow client
 * no longer blocks the others.
 */
public class PooledEchoServer implements AutoCloseable {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final ServerSocket serverSocket;

    public PooledEchoServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public int port() {
        return serverSocket.getLocalPort();
    }

    // Accept loop: hands each client to the pool and goes straight back to accept()
    public void start() {
        try {
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                pool.submit(() -> serve(client));
            }
        } catch (SocketException e) {
            // close() was called: the normal way to stop
        } catch (IOException e) {
            System.out.println("Accept failed: " + e.getMessage());
        }
    }

    private void serve(Socket client) {
        String thread = Thread.currentThread().getName();
        try (Socket socket = client;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                     true)) {
            String line;
            while ((line = in.readLine()) != null) {
                out.println(reply(line, thread));
            }
        } catch (IOException e) {
            System.out.println("Client left: " + e.getMessage());
        }
    }

    private String reply(String line, String thread) throws JsonProcessingException {
        try {
            ClientRequest request = mapper.readValue(line, ClientRequest.class);
            return mapper.writeValueAsString(
                    ServerResponse.ok("handled on " + thread, request.getRequestType()));
        } catch (JsonProcessingException e) {
            return mapper.writeValueAsString(ServerResponse.error("not a valid request"));
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
        pool.shutdown();
    }
}
