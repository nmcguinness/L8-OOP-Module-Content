package t20_json_1_jackson_basics.exercises.ex04;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class UploadClient {

    private static final ObjectMapper MAPPER  = new ObjectMapper();
    private static final int          PORT    = 9_206;

    // Entry point: create the test file, upload to UploadServer, verify the returned ID
    public static void main(String[] args) throws Exception {
        // Create a synthetic 512-byte test file
        byte[] original = new byte[512];
        for (int i = 0; i < original.length; i++) original[i] = (byte)(i % 200);

        Files.createDirectories(Path.of("data"));
        Files.write(Path.of("data/upload_test.bin"), original);

        // Base64-encode the file and send an UPLOAD_FILE request
        int storedId;
        try (Socket         socket = new Socket("localhost", PORT);
             BufferedReader in     = new BufferedReader(new InputStreamReader(socket.getInputStream(),  StandardCharsets.UTF_8));
             PrintWriter    out    = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String encoded = Base64.getEncoder().encodeToString(original);

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("fileName",    "upload_test.bin");
            payload.put("contentType", "application/octet-stream");
            payload.put("fileSize",    original.length);
            payload.put("fileData",    encoded);

            Map<String, Object> request = new LinkedHashMap<>();
            request.put("type",    "UPLOAD_FILE");
            request.put("payload", payload);

            out.println(MAPPER.writeValueAsString(request));

            String responseJson = in.readLine();
            if (responseJson == null)
                throw new IOException("Server closed connection without responding");
            Map<?,?> response = MAPPER.readValue(responseJson, Map.class);
            if ("ERROR".equals(response.get("status")))
                throw new RuntimeException("Server error: " + response.get("message"));
            storedId = ((Number) response.get("id")).intValue();
            System.out.println("Upload OK - stored id: " + storedId);
        }

    }

}
