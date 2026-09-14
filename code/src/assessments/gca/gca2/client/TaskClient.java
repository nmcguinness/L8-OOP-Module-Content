package assessments.gca.gca2.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Demo client for the Task management system.
 * Connects to TaskServer and exercises all four operations in sequence:
 * INSERT, LIST, FIND_BY_ID, and DELETE_BY_ID, then sends DISCONNECT.
 *
 * Run TaskServer first.
 *
 * @author OOP Teaching Team
 */
public class TaskClient {

    // === Constants ===
    private static final String HOST = "localhost";
    private static final int    PORT = 9_300;

    // === Entry point ===

    // Connects: to TaskServer and demonstrates INSERT, LIST, FIND_BY_ID, DELETE_BY_ID
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        try (
            Socket         socket = new Socket(HOST, PORT);
            BufferedReader in     = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter    out    = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            // --- INSERT ---
            System.out.println("=== INSERT ===");
            Map<String, Object> insertPayload = new LinkedHashMap<>();
            insertPayload.put("title",       "Write unit tests");
            insertPayload.put("description", "Cover DAO, JSON, and socket layers");
            insertPayload.put("completed",   false);
            System.out.println(send(out, in, mapper, "INSERT", insertPayload));

            // --- LIST ---
            System.out.println("\n=== LIST ===");
            System.out.println(send(out, in, mapper, "LIST", new LinkedHashMap<>()));

            // --- FIND_BY_ID ---
            System.out.println("\n=== FIND_BY_ID (id=1) ===");
            Map<String, Object> findPayload = new LinkedHashMap<>();
            findPayload.put("taskId", 1);
            System.out.println(send(out, in, mapper, "FIND_BY_ID", findPayload));

            // --- DELETE_BY_ID ---
            System.out.println("\n=== DELETE_BY_ID (id=1) ===");
            Map<String, Object> deletePayload = new LinkedHashMap<>();
            deletePayload.put("taskId", 1);
            System.out.println(send(out, in, mapper, "DELETE_BY_ID", deletePayload));

            // --- DISCONNECT ---
            send(out, in, mapper, "DISCONNECT", new LinkedHashMap<>());
        }
    }

    // === Helpers ===

    // Sends: a JSON request and returns the raw JSON response line; returns "" for DISCONNECT
    private static String send(PrintWriter out, BufferedReader in,
                                ObjectMapper mapper, String requestType,
                                Map<String, Object> payload) throws Exception {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("requestType", requestType);
        request.put("payload",     payload);
        out.println(mapper.writeValueAsString(request));

        if ("DISCONNECT".equals(requestType))
            return "";

        return in.readLine();
    }
}
