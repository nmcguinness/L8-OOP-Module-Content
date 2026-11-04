package t21_json_2_jackson_advanced.exercises.ex05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stores and reads game assets, including their bytes, in the game_assets table.
 */
public class AssetDao {

    private final String url;
    private final String user;
    private final String pass;

    public AssetDao(String url, String user, String pass) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url is required");
        }
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public void insertAsset(int id, String name, byte[] data) throws SQLException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        String sql = "INSERT INTO game_assets (id, name, asset_data) VALUES (?, ?, ?)";
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setBytes(3, data);
            ps.executeUpdate();
        }
    }

    // Empty when there is no such row: getBytes would otherwise hand back null
    public Optional<byte[]> getAsset(int id) throws SQLException {
        String sql = "SELECT asset_data FROM game_assets WHERE id = ?";
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(rs.getBytes("asset_data"));
            }
        }
    }

    // Selects id and name only: listing must never pull every blob across the network
    public List<AssetMetadata> getAllMetadata() throws SQLException {
        String sql = "SELECT id, name FROM game_assets ORDER BY id";
        List<AssetMetadata> result = new ArrayList<>();
        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new AssetMetadata(rs.getInt("id"), rs.getString("name")));
            }
        }
        return result;
    }
}
