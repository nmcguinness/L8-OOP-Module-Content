package t22_networking.exercises.ex07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Exercise {

    public static void run() throws IOException {
        try (PooledEchoServer server = new PooledEchoServer(0)) {
            new Thread(server::start).start();

            // Two clients connected at the same time; both are answered
            try (Connection a = new Connection(server.port());
                 Connection b = new Connection(server.port())) {
                System.out.println("A: " + a.send("{\"requestType\":\"FIRST\",\"payload\":{}}"));
                System.out.println("B: " + b.send("{\"requestType\":\"SECOND\",\"payload\":{}}"));
                System.out.println("A again: " + a.send("{\"requestType\":\"THIRD\",\"payload\":{}}"));
            }
        }
    }

    // A client connection that stays open between messages
    static final class Connection implements AutoCloseable {
        private final Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;

        Connection(int port) throws IOException {
            socket = new Socket("localhost", port);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                    true);
        }

        String send(String json) throws IOException {
            out.println(json);
            return in.readLine();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }
}
