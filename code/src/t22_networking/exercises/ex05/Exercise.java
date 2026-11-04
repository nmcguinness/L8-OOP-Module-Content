package t22_networking.exercises.ex05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Exercise {

    private static final int PORT = 9_103;

    public static void run() throws IOException, InterruptedException {
        RequestDispatcher dispatcher = new RequestDispatcher(new ScoreRepository());

        Thread serverThread = new Thread(() -> {
            try {
                new LeaderboardServer(PORT, dispatcher).start();
            }
            catch (IOException e) {
                System.out.println("Server stopped: " + e.getMessage());
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(300);

        // Test DISCONNECT: after the goodbye, the server closes the connection
        List<String> session = sendAll(List.of(
            "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P001\","
                + "\"playerName\":\"Alice\",\"score\":4200,\"gameMode\":\"ranked\"}}",
            "{\"requestType\":\"DISCONNECT\",\"payload\":{}}"));
        System.out.println("Score submitted: " + session.get(0));
        System.out.println("Disconnect: " + session.get(1));

        // Test missing playerId
        System.out.println("Missing playerId: " + sendAll(List.of(
            "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerName\":\"Bob\","
                + "\"score\":1000,\"gameMode\":\"ranked\"}}")).get(0));

        // Test invalid game mode
        System.out.println("Invalid mode: " + sendAll(List.of(
            "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P003\","
                + "\"playerName\":\"Carol\",\"score\":2000,\"gameMode\":\"TOURNAMENT\"}}")).get(0));
    }

    // Opens one connection, sends each line, and returns each reply in order
    static List<String> sendAll(List<String> requests) throws IOException {
        try (Socket s = new Socket("localhost", PORT);
             BufferedReader in = new BufferedReader(
                 new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                 new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {

            List<String> replies = new ArrayList<>();
            for (String request : requests) {
                out.println(request);
                replies.add(in.readLine());
            }
            return replies;
        }
    }
}
