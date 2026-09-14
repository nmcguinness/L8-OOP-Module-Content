package t22_networking.single.multi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Loopback integration tests for the pooled server. Its accept loop never
 * returns, so it runs on a daemon thread and is abandoned when the test ends.
 */
@DisplayName("t22 - multi-client server")
class MultiClientServerTest {

    private static int freePort() throws IOException {
        try (ServerSocket probe = new ServerSocket(0)) {
            return probe.getLocalPort();
        }
    }

    private static void startServer(int port) {
        Thread t = new Thread(() -> {
            try {
                new MultiClientServer(port).start();
            } catch (IOException e) {
                // Expected when the test JVM exits.
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private static Socket connect(int port) throws Exception {
        IOException last = null;
        for (int attempt = 0; attempt < 50; attempt++) {
            try {
                Socket s = new Socket("127.0.0.1", port);
                s.setSoTimeout(5_000);
                return s;
            } catch (IOException e) {
                last = e;
                Thread.sleep(20);
            }
        }
        throw new IllegalStateException("server never accepted a connection", last);
    }

    private static PrintWriter writer(Socket s) throws IOException {
        return new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    private static BufferedReader reader(Socket s) throws IOException {
        return new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
    }

    @Test
    void constructor_portOutsideTheAllowedRange_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new MultiClientServer(80));
        assertThrows(IllegalArgumentException.class, () -> new MultiClientServer(70_000));
    }

    @Test
    void server_echoesForASingleClient() throws Exception {
        int port = freePort();
        startServer(port);

        try (Socket socket = connect(port)) {
            writer(socket).println("hello");
            assertEquals("ECHO: hello", reader(socket).readLine());
        }
    }

    @Test
    void server_servesTwoClientsAtTheSameTime() throws Exception {
        int port = freePort();
        startServer(port);

        // Both connections stay open together - that is what the thread pool buys.
        try (Socket first = connect(port);
             Socket second = connect(port)) {

            PrintWriter firstOut = writer(first);
            BufferedReader firstIn = reader(first);
            PrintWriter secondOut = writer(second);
            BufferedReader secondIn = reader(second);

            firstOut.println("from-first");
            secondOut.println("from-second");

            assertEquals("ECHO: from-first", firstIn.readLine());
            assertEquals("ECHO: from-second", secondIn.readLine());

            // The first client is still usable after the second has been served.
            firstOut.println("again");
            assertEquals("ECHO: again", firstIn.readLine());
        }
    }

    @Test
    void server_keepsAcceptingAfterAClientDisconnects() throws Exception {
        int port = freePort();
        startServer(port);

        try (Socket first = connect(port)) {
            writer(first).println("one");
            assertEquals("ECHO: one", reader(first).readLine());
        }

        try (Socket second = connect(port)) {
            writer(second).println("two");
            assertEquals("ECHO: two", reader(second).readLine());
        }
    }
}
