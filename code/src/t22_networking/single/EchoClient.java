package t22_networking.single;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class EchoClient {

    // === Fields ===
    private final String host;
    private final int port;

    // === Constructors ===
    // Creates: a client that will connect to the given host and port
    public EchoClient(String host, int port) {
        if (host == null || host.isBlank())
            throw new IllegalArgumentException("host is required");
        this.host = host;
        this.port = port;
    }

    // === Public API ===
    // Connects: sends a test message and prints the reply
    public void run() throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                  new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            out.println("hello server");      // send
            String reply = in.readLine();     // receive (blocks until a line arrives)
            System.out.println("Reply: " + reply);
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoClient("localhost", 9_000).run();
    }
}