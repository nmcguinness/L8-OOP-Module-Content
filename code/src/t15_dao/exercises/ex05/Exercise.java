package t15_dao.exercises.ex05;

import t15_dao.exercises.DbConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exercise {

    public static void run() {

        DbConfig config;
        try {
            config = DbConfig.load(DbConfig.DEFAULT_FILE);
        } catch (IOException e) {
            System.out.println("Could not read " + DbConfig.DEFAULT_FILE
                    + ". Copy db.properties.example to it and fill in your settings.");
            return;
        }

        CarDao dao = new JdbcCarDao(config.url(), config.user(), config.password());

        try {
            System.out.println("Page (offset=0, limit=3):");
            for (Car c : dao.findPage(0, 3)) {
                System.out.println(" - " + c);
            }

            System.out.println("Search query='for':");
            for (Car c : dao.findByMakeOrModel("for")) {
                System.out.println(" - " + c);
            }

            System.out.println("Count by status:");
            System.out.println(dao.countByStatus());
        }
        catch (DaoException e) {
            System.out.println("Storage error: " + e.getMessage());
        }
    }
}

class DaoException extends Exception {

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}

interface CarDao {
    List<Car> findPage(int offset, int limit) throws DaoException;
    List<Car> findByMakeOrModel(String query) throws DaoException;
    Map<String, Integer> countByStatus() throws DaoException;
}

class JdbcCarDao implements CarDao {

    private static final String COLUMNS = "id, reg, make, model, daily_rate, status";

    private final String url;
    private final String user;
    private final String pass;

    public JdbcCarDao(String url, String user, String pass) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url is required");
        }
        this.url = url.trim();
        this.user = user;
        this.pass = pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    @Override
    public List<Car> findPage(int offset, int limit) throws DaoException {
        if (limit <= 0) {
            return List.of();
        }

        int safeOffset = Math.max(0, offset);
        String sql = "SELECT " + COLUMNS + " FROM cars ORDER BY id LIMIT ? OFFSET ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, safeOffset);

            try (ResultSet rs = ps.executeQuery()) {
                List<Car> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
        catch (SQLException e) {
            throw new DaoException("Could not read page offset=" + offset + " limit=" + limit, e);
        }
    }

    @Override
    public List<Car> findByMakeOrModel(String query) throws DaoException {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        // The wildcards go in the bound value, never in the SQL string
        String q = "%" + query.trim() + "%";
        String sql = "SELECT " + COLUMNS
            + " FROM cars WHERE make LIKE ? OR model LIKE ? ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, q);
            ps.setString(2, q);

            try (ResultSet rs = ps.executeQuery()) {
                List<Car> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
        catch (SQLException e) {
            throw new DaoException("Could not search cars for '" + query + "'", e);
        }
    }

    @Override
    public Map<String, Integer> countByStatus() throws DaoException {
        String sql = "SELECT status, COUNT(*) AS c FROM cars GROUP BY status";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Map<String, Integer> out = new HashMap<>();
            while (rs.next()) {
                out.put(rs.getString("status"), rs.getInt("c"));
            }
            return out;
        }
        catch (SQLException e) {
            throw new DaoException("Could not count cars by status", e);
        }
    }

    private static Car mapRow(ResultSet rs) throws SQLException {
        return new Car(
            rs.getInt("id"),
            rs.getString("reg"),
            rs.getString("make"),
            rs.getString("model"),
            rs.getDouble("daily_rate"),
            rs.getString("status")
        );
    }
}

class Car {

    private final int id;
    private final String reg;
    private final String make;
    private final String model;
    private final double dailyRate;
    private final String status;

    public Car(int id, String reg, String make, String model, double dailyRate, String status) {

        if (id < 0) {
            throw new IllegalArgumentException("id must be >= 0");
        }

        if (reg == null || reg.isBlank()) {
            throw new IllegalArgumentException("reg is required");
        }

        if (make == null || make.isBlank()) {
            throw new IllegalArgumentException("make is required");
        }

        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("model is required");
        }

        if (dailyRate <= 0.0) {
            throw new IllegalArgumentException("dailyRate must be > 0");
        }

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("status is required");
        }

        this.id = id;
        this.reg = reg.trim();
        this.make = make.trim();
        this.model = model.trim();
        this.dailyRate = dailyRate;
        this.status = status.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "Car{id=" + id + ", reg='" + reg + "', make='" + make
                + "', model='" + model + "', rate=" + dailyRate + ", status=" + status + "}";
    }
}
