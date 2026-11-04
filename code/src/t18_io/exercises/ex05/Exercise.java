package t18_io.exercises.ex05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Exercise {

    // Copies any file byte for byte, replacing the destination if it exists
    public static void copyBinary(Path source, Path destination) throws IOException {
        byte[] data = Files.readAllBytes(source);
        Files.write(destination, data);
    }

    public static void run() throws IOException {
        Path source = Files.createTempFile("source", ".bin");
        Path copy = Files.createTempFile("copy", ".bin");

        // Bytes from 0 to 255: not valid text, so a text read and write would damage them
        byte[] original = new byte[256];
        for (int i = 0; i < original.length; i++) {
            original[i] = (byte) i;
        }
        Files.write(source, original);

        copyBinary(source, copy);
        boolean same = Arrays.equals(Files.readAllBytes(source), Files.readAllBytes(copy));
        System.out.println("Copy identical: " + same);

        Files.deleteIfExists(source);
        Files.deleteIfExists(copy);
    }
}
