package t20_json_1_jackson_basics.exercises.ex03;

import java.sql.*;
import java.util.Optional;

public class JdbcGameAssetDao implements GameAssetDao {

    // === Fields ===
    private String _url;
    private String _user;
    private String _pass;

    // === Constructors ===
    // Creates: a DAO backed by the given database connection details
    public JdbcGameAssetDao(String url, String user, String pass) {
        if (url == null || url.isBlank())
            throw new IllegalArgumentException("url is required");
        _url  = url;
        _user = user;
        _pass = pass;
    }

    // === Public API ===
    // Inserts: an asset record and returns the auto-generated ID
    @Override
    public int insert(GameAsset asset) throws Exception {
        if (asset == null)
            throw new IllegalArgumentException("asset is required");

        String sql = "INSERT INTO game_assets (asset_name, asset_type, file_size, asset_data) VALUES (?, ?, ?, ?)";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, asset.getAssetName());
            ps.setString(2, asset.getAssetType());
            ps.setInt(3, asset.getFileSize());
            ps.setBytes(4, asset.getAssetData());   // <-- BLOB

            int rows = ps.executeUpdate();
            if (rows != 1)
                throw new IllegalStateException("insert failed, rows=" + rows);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next())
                    throw new IllegalStateException("no generated key returned");
                return keys.getInt(1);
            }
        }
    }

    // Gets: the full asset record including binary data
    @Override
    public Optional<GameAsset> findById(int id) throws Exception {
        if (id <= 0)
            return Optional.empty();

        String sql = "SELECT asset_id, asset_name, asset_type, file_size, asset_data FROM game_assets WHERE asset_id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return Optional.empty();

                int    assetId   = rs.getInt("asset_id");
                String assetName = rs.getString("asset_name");
                String assetType = rs.getString("asset_type");
                int    fileSize  = rs.getInt("file_size");
                byte[] assetData = rs.getBytes("asset_data");  // <-- BLOB

                return Optional.of(new GameAsset(assetId, assetName, assetType, fileSize, assetData));
            }
        }
    }

    // Deletes: an asset record by ID
    @Override
    public boolean deleteById(int id) throws Exception {
        if (id <= 0)
            return false;

        String sql = "DELETE FROM game_assets WHERE asset_id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // === Helpers ===
    // Opens: a new database connection
    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }
}
