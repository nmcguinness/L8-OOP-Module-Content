package t21_json_2_jackson_advanced.exercises.ex04;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Exercise {

    public static String encodeToBase64(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        return Base64.getEncoder().encodeToString(data);
    }

    // Throws IllegalArgumentException if the text is not valid Base64
    public static byte[] decodeFromBase64(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("encoded must not be null");
        }
        return Base64.getDecoder().decode(encoded);
    }

    public static void run() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] original = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        // Put the encoded bytes into a JSON field, as an upload request would
        ObjectNode payload = mapper.createObjectNode();
        payload.put("fileName", "hello.txt");
        payload.put("data", encodeToBase64(original));
        String json = mapper.writeValueAsString(payload);
        System.out.println("JSON: " + json);

        // Take the field back out and decode it
        String field = mapper.readTree(json).get("data").asText();
        byte[] restored = decodeFromBase64(field);

        System.out.println("Bytes match: " + Arrays.equals(original, restored));
        System.out.println("Original " + original.length + " bytes, Base64 "
                + field.length() + " characters");
    }
}
