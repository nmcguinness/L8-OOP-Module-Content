package t22_networking.single;

import java.io.*;
import java.net.*;

public class EchoServer {

    // === Fields ===
    private int _port;

    // === Constructors ===
    // Creates: an echo server that will listen on the given port
    public EchoServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024-65535");
        _port = port;
    }

    // === Public API ===
    // Starts: the server - blocks waiting for one client, echoes each line, then exits
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            System.out.println("Echo server listening on port " + _port);

            Socket clientSocket = serverSocket.accept();  // blocks until a client connects
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out  = new PrintWriter(clientSocket.getOutputStream(), true)) {

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