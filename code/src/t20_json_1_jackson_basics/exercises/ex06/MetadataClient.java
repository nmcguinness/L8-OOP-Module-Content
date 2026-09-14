package t20_json_1_jackson_basics.exercises.ex06;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class MetadataClient {

    private static final ObjectMapper MAPPER  = new ObjectMapper();
    private static final int          PORT    = 9_208;

    // Entry point: seed the DB, request metadata from MetadataServer, assert no fileData in response
    public static void main(String[] args) throws Exception {
        // Retrieve the row ID=1
        int testId = 1;

        // Request metadata only - no binary payload
        try (Socket         socket = new Socket("localhost", PORT);
             BufferedReader in     = new BufferedReader(new InputStreamReader(socket.getInputStream(),  StandardCharsets.UTF_8));
             PrintWriter    out    = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            Map<String, Object> request = new LinkedHashMap<>();
            request.put("type",    "GET_METADATA");
            request.put("payload", Map.of("id", testId));

            out.println(MAPPER.writeValueAsString(request));

            String   responseJson = in.readLine();
            Map<?,?> response     = MAPPER.readValue(responseJson, Map.class);
            Map<?,?> metadata     = (Map<?,?>) response.get("data");

            System.out.println("File name:    " + metadata.get("fileName"));
            System.out.println("Content type: " + metadata.get("contentType"));
            System.out.println("File size:    " + metadata.get("fileSize") + " bytes");
            System.out.println("fileData absent from response: " + !metadata.containsKey("fileData"));
        }
    }
}
