package t22_networking.exercises.ex02;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class Exercise {

    public static void run() throws InterruptedException {
        Thread serverThread = new Thread(() -> {
            try {
                new MultiClientEchoServer(9_101).start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(200);

        String[] clientNames = {"client-A", "client-B", "client-C"};

        for (String name : clientNames) {
            Thread t = new Thread(() -> {
                try (Socket s = new Socket("localhost", 9_101);
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {

                    out.println(name + " msg-1");
                    System.out.println(in.readLine());
                    out.println(name + " msg-2");
                    System.out.println(in.readLine());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }

        Thread.sleep(1_000);
        System.out.println("All clients done");
    }
}

class MultiClientEchoServer {

    // === Fields ===
    private final int port;
    private final ExecutorService pool;

    // === Constructors ===
    // Creates: a multi-client echo server on the given port
    public MultiClientEchoServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024–65535");
        this.port = port;
        pool = Executors.newCachedThreadPool();
    }

    // === Public API ===
    // Starts: the accept loop — handles each client on a pool thread
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(new ClientHandler(client));
            }
        }
    }

    // === Helpers ===
    // Handles: one client connection — echoes lines until disconnect
    private static class ClientHandler implements Runnable {

        private final Socket socket;

        // Creates: a handler for the given socket
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        // Runs: reads lines and echoes them with the thread name prefix
        @Override
        public void run() {
            String name = Thread.currentThread().getName();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    out.println("[" + name + "] ECHO: " + line);
                }
                // readLine returns null once the client closes its end
                System.out.println("Client disconnected: " + socket.getInetAddress());
            }
            catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            }
            finally {
                try { socket.close(); } catch (IOException ignored) { }
            }
        }
    }
}
