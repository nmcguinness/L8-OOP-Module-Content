package t22_networking.exercises.ex05;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeaderboardServer {

    private final int port;
    private final RequestDispatcher dispatcher;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public LeaderboardServer(int port, RequestDispatcher dispatcher) {
        if (dispatcher == null)
            throw new IllegalArgumentException("dispatcher is required");
        this.port = port;
        this.dispatcher = dispatcher;
    }

    // Starts: the accept loop — handles each client on a pool thread
    public void start() throws IOException {
        try (ServerSocket ss = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = ss.accept();
                pool.submit(new ClientHandler(client, dispatcher));
            }
        }
        finally {
            pool.shutdown();
        }
    }
}
