package t22_networking.exercises.ex01;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Exercise {

    public static void run() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> {
            try {
                new EchoServer(9_100).start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(200);
        new EchoClient("localhost", 9_100).run();
    }
}

class EchoServer {

    // === Fields ===
    private final int port;

    // === Constructors ===
    // Creates: an echo server on the given port
    public EchoServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024–65535");
        this.port = port;
    }

    // === Public API ===
    // Starts: the server — accepts one client and echoes every line
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket client = serverSocket.accept();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    out.println("ECHO: " + line);
                }
            }
        }
    }
}

class EchoClient {

    // === Fields ===
    private final String host;
    private final int port;

    // === Constructors ===
    // Creates: a client targeting the given host and port
    public EchoClient(String host, int port) {
        if (host == null || host.isBlank())
            throw new IllegalArgumentException("host is required");
        this.host = host;
        this.port = port;
    }

    // === Public API ===
    // Runs: sends three messages and prints replies
    public void run() throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String[] messages = {"ping", "hello server", "goodbye"};

            for (String msg : messages) {
                out.println(msg);
                System.out.println(in.readLine());
            }
        }
    }
}
