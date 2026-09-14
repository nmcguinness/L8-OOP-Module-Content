package t22_networking.single.multi;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiClientServer {

    // === Fields ===
    private int _port;
    private ExecutorService _pool;

    // === Constructors ===
    // Creates: a multi-client server using a cached thread pool
    public MultiClientServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024-65535");
        _port = port;
        _pool = Executors.newCachedThreadPool();
    }

    // === Public API ===
    // Starts: the server accept loop - runs until the process is stopped
    public void start() throws IOException {
        System.out.println("Server starting on port " + _port);

        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();    // block until a client arrives
                System.out.println("Accepted: " + clientSocket.getInetAddress());
                _pool.submit(new ClientHandler(clientSocket)); // hand off to pool
            }
        }
    }

    // === Helpers ===
    // Handles: one connected client - runs on a pool thread
    private static class ClientHandler implements Runnable {

        private Socket _socket;

        // Creates: a handler for the given socket
        public ClientHandler(Socket socket) {
            _socket = socket;
        }

        // Runs: the client session - reads lines and echoes them
        @Override
        public void run() {
            System.out.println("Handling client on " + Thread.currentThread().getName());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                 PrintWriter out  = new PrintWriter(_socket.getOutputStream(), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    out.println("ECHO: " + line);
                }
            }
            catch (IOException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            }
            finally {
                try {
                    _socket.close();
                }
                catch (IOException e) {
                    // nothing useful to do here
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new MultiClientServer(9_000).start();
    }
}