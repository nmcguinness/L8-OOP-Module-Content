package t22_networking.single;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class EchoServer {

    // === Fields ===
    private final int port;

    // === Constructors ===
    // Creates: an echo server that will listen on the given port
    public EchoServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024-65535");
        this.port = port;
    }

    // === Public API ===
    // Starts: the server - blocks waiting for one client, echoes each line, then exits
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Echo server listening on port " + port);

            Socket clientSocket = serverSocket.accept();  // blocks until a client connects
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(
                      new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Received: " + line);
                    out.println("ECHO: " + line);  // send reply
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoServer(9_000).start();
    }
}