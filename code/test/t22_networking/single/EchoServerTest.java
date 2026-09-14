package t22_networking.single;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * These are loopback integration tests: a real server socket and a real client
 * socket, both on 127.0.0.1. Nothing leaves the machine.
 */
@DisplayName("t22 - single-client echo server")
class EchoServerTest {

    /** Asks the OS for a free port, then releases it for the server to claim. */
    private static int freePort() throws IOException {
        try (ServerSocket probe = new ServerSocket(0)) {
            return probe.getLocalPort();
        }
    }

    private static Thread startServer(EchoServer server) {
        Thread t = new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                // The socket closes when the test finishes; nothing to report.
            }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    /** Waits for the server thread to reach accept() before connecting. */
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

    @Test
    void constructor_portBelow1024_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EchoServer(80));
    }

    @Test
    void constructor_portAbove65535_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EchoServer(70_000));
    }

    @Test
    void constructor_boundaryPorts_areAccepted() {
        new EchoServer(1_024);
        new EchoServer(65_535);
    }

    @Test
    void client_constructor_blankOrNullHost_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EchoClient(null, 9_000));
        assertThrows(IllegalArgumentException.class, () -> new EchoClient("   ", 9_000));
    }

    @Test
    void server_echoesASingleLineBackWithThePrefix() throws Exception {
        int port = freePort();
        startServer(new EchoServer(port));

        try (Socket socket = connect(port);
             PrintWriter out = new PrintWriter(
                     new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            out.println("hello server");

            assertEquals("ECHO: hello server", in.readLine());
        }
    }

    @Test
    void server_echoesEveryLineInOrderOnOneConnection() throws Exception {
        int port = freePort();
        startServer(new EchoServer(port));

        try (Socket socket = connect(port);
             PrintWriter out = new PrintWriter(
                     new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            out.println("one");
            assertEquals("ECHO: one", in.readLine());

            out.println("two");
            assertEquals("ECHO: two", in.readLine());
        }
    }

    @Test
    void server_echoesAnEmptyLine() throws Exception {
        int port = freePort();
        startServer(new EchoServer(port));

        try (Socket socket = connect(port);
             PrintWriter out = new PrintWriter(
                     new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            out.println();

            assertEquals("ECHO: ", in.readLine());
        }
    }
}
