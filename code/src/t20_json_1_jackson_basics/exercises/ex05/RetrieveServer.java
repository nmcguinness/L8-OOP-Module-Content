package t20_json_1_jackson_basics.exercises.ex05;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class RetrieveServer {

    // === Constants ===
    private static final String URL     = "jdbc:mysql://localhost:3306/game_assets_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "game_assets_user";
    private static final String DB_PASS = "your_password";
    private static final int    PORT    = 9_207;

    // === Fields ===
    private int    _port;
    private String _url;
    private String _user;
    private String _pass;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // === Entry point ===
    // Starts: the retrieve server; run this class before running RetrieveClient
    public static void main(String[] args) throws Exception {
        System.out.println("RetrieveServer listening on port " + PORT + " ...");
        new RetrieveServer(PORT, URL, DB_USER, DB_PASS).start();
    }

    // === Constructors ===
    // Creates: a retrieve-only server bound to the given port and database
    public RetrieveServer(int port, String url, String user, String pass) {
        _port = port;
        _url  = url;
        _user = user;
        _pass = pass;
    }

    // === Public API ===
    // Starts: the server loop; accepts connections until interrupted
    public void start() throws Exception {
        try (ServerSocket ss = new ServerSocket(_port)) {
            while (!Thread.currentThread().isInterrupted())
                handleRetrieve(ss.accept());
        }
    }

    // === Helpers ===
    // Handles: one RETRIEVE_FILE request - fetches the BLOB and Base64-encodes it for the client
    private void handleRetrieve(Socket client) {
        try (client;
             BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream(),  StandardCharsets.UTF_8));
             PrintWriter    out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line = in.readLine();
            if (line == null) return;

            Map<?,?> req     = MAPPER.readValue(line, Map.class);
            Map<?,?> payload = (Map<?,?>) req.get("payload");
            int      id      = ((Number) payload.get("id")).intValue();

            String sql = "SELECT asset_name, asset_type, file_size, asset_data "
                       + "FROM game_assets WHERE asset_id = ?";

            try (Connection        c  = DriverManager.getConnection(_url, _user, _pass);
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        out.println(MAPPER.writeValueAsString(
                            Map.of("status", "ERROR", "message", "not found id=" + id)));
                        return;
                    }

                    String name  = rs.getString("asset_name");
                    String type  = rs.getString("asset_type");
                    int    size  = rs.getInt("file_size");
                    byte[] bytes = rs.getBytes("asset_data");   // load the BLOB

                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("id",          id);
                    data.put("fileName",    name);
                    data.put("contentType", type);
                    data.put("fileSize",    size);
                    data.put("fileData",    Base64.getEncoder().encodeToString(bytes));

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("status", "OK");
                    response.put("data",   data);
                    out.println(MAPPER.writeValueAsString(response));
                }
            }

        } catch (Exception e) {
            System.err.println("Retrieve handler error: " + e.getMessage());
        }
    }
}
