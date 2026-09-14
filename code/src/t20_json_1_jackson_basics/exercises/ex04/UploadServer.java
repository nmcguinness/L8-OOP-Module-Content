package t20_json_1_jackson_basics.exercises.ex04;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class UploadServer {

    // === Constants ===
    private static final String URL     = "jdbc:mysql://localhost:3306/game_assets_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "game_assets_user";
    private static final String DB_PASS = "your_password";
    private static final int    PORT    = 9_206;

    // === Fields ===
    private int    _port;
    private String _url;
    private String _user;
    private String _pass;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // === Entry point ===
    // Starts: the upload server; run this class before running UploadClient
    public static void main(String[] args) throws Exception {
        System.out.println("UploadServer listening on port " + PORT + " ...");
        new UploadServer(PORT, URL, DB_USER, DB_PASS).start();
    }

    // === Constructors ===
    // Creates: an upload-only server bound to the given port and database
    public UploadServer(int port, String url, String user, String pass) {
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
                handleUpload(ss.accept());
        }
    }

    // === Helpers ===
    // Handles: one UPLOAD_FILE request - decodes Base64 payload and stores in game_assets
    private void handleUpload(Socket client) {
        try (client;
             BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream(),  StandardCharsets.UTF_8));
             PrintWriter    out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line = in.readLine();
            if (line == null) return;

            Map<?,?> req     = MAPPER.readValue(line, Map.class);
            Map<?,?> payload = (Map<?,?>) req.get("payload");

            String b64  = (String)  payload.get("fileData");
            String name = (String)  payload.get("fileName");
            String type = (String)  payload.get("contentType");
            int    size = ((Number) payload.get("fileSize")).intValue();
            byte[] data = Base64.getDecoder().decode(b64);

            int id = insertAsset(name, type, size, data);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "OK");
            response.put("id",     id);
            out.println(MAPPER.writeValueAsString(response));

        } catch (Exception e) {
            System.err.println("Upload handler error: " + e.getMessage());
            try (PrintWriter errOut = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true)) {
                Map<String, Object> err = new LinkedHashMap<>();
                err.put("status", "ERROR");
                err.put("message", e.getMessage());
                errOut.println(MAPPER.writeValueAsString(err));
            } catch (Exception ignored) {}
        }
    }

    // Inserts: a binary asset into game_assets; returns the auto-generated asset_id
    private int insertAsset(String name, String type, int size, byte[] data) throws Exception {
        String sql = "INSERT INTO game_assets (asset_name, asset_type, file_size, asset_data) VALUES (?, ?, ?, ?)";
        try (Connection        c  = DriverManager.getConnection(_url, _user, _pass);
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setInt(3,    size);
            ps.setBytes(4,  data);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next())
                    throw new IllegalStateException("no generated key returned");
                return keys.getInt(1);
            }
        }
    }
}
