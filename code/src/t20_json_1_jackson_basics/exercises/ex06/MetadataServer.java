package t20_json_1_jackson_basics.exercises.ex06;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class MetadataServer {

    // === Constants ===
    private static final String URL     = "jdbc:mysql://localhost:3306/game_assets_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final int    PORT    = 9_208;

    // === Fields ===
    private int    _port;
    private String _url;
    private String _user;
    private String _pass;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // === Entry point ===
    // Starts: the metadata server; run this class before running MetadataClient
    public static void main(String[] args) throws Exception {
        System.out.println("MetadataServer listening on port " + PORT + " ...");
        new MetadataServer(PORT, URL, DB_USER, DB_PASS).start();
    }

    // === Constructors ===
    // Creates: a metadata-only server bound to the given port and database
    public MetadataServer(int port, String url, String user, String pass) {
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
                handleMetadata(ss.accept());
        }
    }

    // === Helpers ===
    // Handles: one GET_METADATA request - SELECTs metadata columns only; asset_data is never fetched
    private void handleMetadata(Socket client) {
        try (client;
             BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream(),  StandardCharsets.UTF_8));
             PrintWriter    out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line = in.readLine();
            if (line == null) return;

            Map<?,?> req     = MAPPER.readValue(line, Map.class);
            Map<?,?> payload = (Map<?,?>) req.get("payload");
            int      id      = ((Number) payload.get("id")).intValue();

            // asset_data deliberately excluded from this SELECT
            String sql = "SELECT asset_name, asset_type, file_size "
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

                    Map<String, Object> metadata = new LinkedHashMap<>();
                    metadata.put("id",          id);
                    metadata.put("fileName",    rs.getString("asset_name"));
                    metadata.put("contentType", rs.getString("asset_type"));
                    metadata.put("fileSize",    rs.getInt("file_size"));

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("status", "OK");
                    response.put("data",   metadata);
                    out.println(MAPPER.writeValueAsString(response));
                }
            }

        } catch (Exception e) {
            System.err.println("Metadata handler error: " + e.getMessage());
        }
    }
}
