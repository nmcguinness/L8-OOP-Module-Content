package t22_networking.single;

import java.io.*;
import java.net.*;

public class EchoClient {

    // === Fields ===
    private String _host;
    private int _port;

    // === Constructors ===
    // Creates: a client that will connect to the given host and port
    public EchoClient(String host, int port) {
        if (host == null || host.isBlank())
            throw new IllegalArgumentException("host is required");
        _host = host;
        _port = port;
    }

    // === Public API ===
    // Connects: sends a test message and prints the reply
    public void run() throws IOException {
        try (Socket socket = new Socket(_host, _port);
             BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out    = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("hello server");      // send
            String reply = in.readLine();     // receive (blocks until a line arrives)
            System.out.println("Reply: " + reply);
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoClient("localhost", 9_000).run();
    }
}