package t21_json_2_jackson_advanced.exercises.ex05;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;

public class Exercise {

    public static void run() {
        AssetDao dao = new AssetDao("jdbc:mysql://localhost:3306/game_db", "root", "");
        byte[] sprite = "pretend these are PNG bytes".getBytes(StandardCharsets.UTF_8);

        try {
            dao.insertAsset(1, "hero.png", sprite);
            byte[] back = dao.getAsset(1).orElseThrow();
            System.out.println("Bytes match: " + Arrays.equals(sprite, back));
            System.out.println("Missing id 999: " + dao.getAsset(999));
            System.out.println("Metadata: " + dao.getAllMetadata());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            System.out.println("Check MySQL is running and schema.sql has been run.");
        }
    }
}
