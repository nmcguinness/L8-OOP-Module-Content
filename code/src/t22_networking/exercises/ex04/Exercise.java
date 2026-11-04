package t22_networking.exercises.ex04;

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

    private static final int PORT = 9_102;

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

        // Client 1: submit three scores
        List<String> replies = sendAll(List.of(
            "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P001\","
                + "\"playerName\":\"Alice\",\"score\":4200,\"gameMode\":\"ranked\"}}",
            "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P002\","
                + "\"playerName\":\"Bob\",\"score\":3875,\"gameMode\":\"ranked\"}}",
            "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P003\","
                + "\"playerName\":\"Carol\",\"score\":5100,\"gameMode\":\"ranked\"}}"));
        for (String r : replies)
            System.out.println("SUBMIT: " + r);

        // Client 2: GET_ALL
        System.out.println("GET_ALL: "
            + sendAll(List.of("{\"requestType\":\"GET_ALL\",\"payload\":{}}")).get(0));

        // Client 3: GET_TOP_N
        System.out.println("GET_TOP_N: "
            + sendAll(List.of("{\"requestType\":\"GET_TOP_N\",\"payload\":{\"n\":2}}")).get(0));
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
