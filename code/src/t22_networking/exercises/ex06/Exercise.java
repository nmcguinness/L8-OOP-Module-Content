package t22_networking.exercises.ex06;

import java.io.IOException;

public class Exercise {

    public static void run() throws IOException, InterruptedException {
        EchoServer server = new EchoServer(0);
        Thread serverThread = new Thread(() -> {
            try {
                server.serveOneClient();
            } catch (IOException e) {
                System.out.println("Server stopped: " + e.getMessage());
            }
        });
        serverThread.start();

        String reply = EchoClient.send("localhost", server.port(),
                "{\"requestType\":\"GET_PLAYER\",\"payload\":{\"id\":42}}");
        System.out.println("Reply: " + reply);

        serverThread.join();
    }
}
