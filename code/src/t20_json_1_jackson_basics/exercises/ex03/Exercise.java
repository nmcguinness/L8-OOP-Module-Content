package t20_json_1_jackson_basics.exercises.ex03;

import java.util.Arrays;
import java.util.Optional;

public class Exercise {

    private static final String URL     = "jdbc:mysql://localhost:3306/game_assets_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String[] args) throws Exception {

        GameAssetDao dao = new JdbcGameAssetDao(URL, DB_USER, DB_PASS);

        byte[] original = new byte[]{10, 20, 30, 40, 50};
        GameAsset asset = new GameAsset(0, "test_sprite.bin", "application/octet-stream", original.length, original);

        int id = dao.insert(asset);
        System.out.println("Inserted asset with id=" + id);

        Optional<GameAsset> found = dao.findById(id);
        boolean ok = found.isPresent() && Arrays.equals(original, found.get().getAssetData());
        System.out.println("Round-trip OK: " + ok);

        System.out.println("Deleted: " + dao.deleteById(id));

    }
}
