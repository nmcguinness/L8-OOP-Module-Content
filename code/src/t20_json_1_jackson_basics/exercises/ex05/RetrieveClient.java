package t20_json_1_jackson_basics.exercises.ex05;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class RetrieveClient {

    private static final ObjectMapper MAPPER  = new ObjectMapper();
    private static final int          PORT    = 9_207;

    // Entry point: seed the DB, download from RetrieveServer, verify byte-for-byte integrity
    public static void main(String[] args) throws Exception {

        // Retrieve the row ID=1
        int testId = 1;

        // Send a RETRIEVE_FILE request and reconstruct the file on disk
        try (Socket         socket = new Socket("localhost", PORT);
             BufferedReader in     = new BufferedReader(new InputStreamReader(socket.getInputStream(),  StandardCharsets.UTF_8));
             PrintWriter    out    = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            Map<String, Object> request = new LinkedHashMap<>();
            request.put("type",    "RETRIEVE_FILE");
            request.put("payload", Map.of("id", testId));

            out.println(MAPPER.writeValueAsString(request));

            String   responseJson = in.readLine();
            Map<?,?> response     = MAPPER.readValue(responseJson, Map.class);
            Map<?,?> data         = (Map<?,?>) response.get("data");

            byte[] downloaded = Base64.getDecoder().decode((String) data.get("fileData"));

            Files.createDirectories(Path.of("data"));
            Files.write(Path.of("data/retrieved_test.bin"), downloaded);

            System.out.println("Retrieved: " + data.get("fileName") + " (" + downloaded.length + " bytes)");
        }
    }
}
