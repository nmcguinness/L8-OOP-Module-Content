package t20_json_1_jackson_basics.exercises.ex02;

import java.sql.*;

public class Exercise {

    private static final String URL     = "jdbc:mysql://localhost:3306/game_assets_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String[] args) throws Exception {

        try (Connection c = DriverManager.getConnection(URL, DB_USER, DB_PASS)) {

            // Metadata-only query - asset_data deliberately excluded
            String sql = "SELECT asset_id, asset_name, asset_type, file_size FROM game_assets ORDER BY asset_id";

            try (PreparedStatement ps = c.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int    id       = rs.getInt("asset_id");
                    String name     = rs.getString("asset_name");
                    String type     = rs.getString("asset_type");
                    int    fileSize = rs.getInt("file_size");
                    System.out.println("[" + id + "] " + name + " (" + type + ", " + fileSize + " bytes)");
                }
            }

            try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM game_assets");
                 ResultSet rs = ps.executeQuery()) {

                rs.next();
                System.out.println("Total assets: " + rs.getInt(1));
            }
        }
    }
}