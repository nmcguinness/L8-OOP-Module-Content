package t20_json_1_jackson_basics.exercises.ex01;

import java.util.Arrays;

public class Exercise {

    public static void main(String[] args) throws Exception {
        // Create a synthetic test file
        byte[] synthetic = new byte[256];
        for (int i = 0; i < synthetic.length; i++)
            synthetic[i] = (byte) i;
        BinaryFileUtil.writeFile("data/test_asset.bin", synthetic);

        // Round-trip test
        byte[] original = BinaryFileUtil.readFile("data/test_asset.bin");
        BinaryFileUtil.writeFile("data/test_asset_copy.bin", original);
        byte[] copy = BinaryFileUtil.readFile("data/test_asset_copy.bin");

        System.out.println("File size: " + original.length + " bytes");
        System.out.println("Round-trip OK: " + Arrays.equals(original, copy));

        // Print first 8 bytes
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 8; i++) {
            sb.append(original[i] & 0xFF);
            if (i < 7) sb.append(", ");
        }
        sb.append("]");
        System.out.println("First 8 bytes: " + sb);
    }
}
