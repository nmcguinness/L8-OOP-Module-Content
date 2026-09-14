package t15_dao.exercises.ex05;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exercise {

    public static void run() throws Exception {

        String url = "jdbc:mysql://localhost:3306/car_rental";
        String user = "root";
        String pass = "";

        CarDao dao = new JdbcCarDao(url, user, pass);

        System.out.println("Page (offset=0, limit=3):");
        for (Car c : dao.findPage(0, 3))
            System.out.println(" - " + c);

        System.out.println("Search query='for':");
        for (Car c : dao.findByMakeOrModel("for"))
            System.out.println(" - " + c);

        System.out.println("Count by status:");
        System.out.println(dao.countByStatus());
    }
}

interface CarDao {
    List<Car> findPage(int offset, int limit) throws Exception;
    List<Car> findByMakeOrModel(String query) throws Exception;
    Map<String, Integer> countByStatus() throws Exception;
}

class JdbcCarDao implements CarDao {

    private String _url;
    private String _user;
    private String _pass;

    public JdbcCarDao(String url, String user, String pass) {
        if (url == null || url.isBlank())
            throw new IllegalArgumentException("url is required");
        _url  = url.trim();
        _user = user;
        _pass = pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

    @Override
    public List<Car> findPage(int offset, int limit) throws Exception {
        if (limit <= 0) return List.of();

        int safeOffset = Math.max(0, offset);
        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars ORDER BY id LIMIT ? OFFSET ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, safeOffset);

            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<Car> out = new ArrayList<>();
                while (rs.next()) out.add(mapRow(rs));
                return out;
            }
        }
    }

    @Override
    public List<Car> findByMakeOrModel(String query) throws Exception {
        if (query == null || query.isBlank()) return List.of();

        String q = "%" + query.trim() + "%";
        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars WHERE make LIKE ? OR model LIKE ? ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, q);
            ps.setString(2, q);

            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<Car> out = new ArrayList<>();
                while (rs.next()) out.add(mapRow(rs));
                return out;
            }
        }
    }

    @Override
    public Map<String, Integer> countByStatus() throws Exception {
        String sql = "SELECT status, COUNT(*) AS c FROM cars GROUP BY status";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            HashMap<String, Integer> out = new HashMap<>();
            while (rs.next())
                out.put(rs.getString("status"), rs.getInt("c"));
            return out;
        }
    }

    private static Car mapRow(ResultSet rs) throws SQLException {
        return new Car(rs.getInt("id"), rs.getString("reg"), rs.getString("make"),
                       rs.getString("model"), rs.getDouble("daily_rate"), rs.getString("status"));
    }
}

class Car {

    private int _id;
    private String _reg;
    private String _make;
    private String _model;
    private double _dailyRate;
    private String _status;

    public Car(int id, String reg, String make, String model, double dailyRate, String status) {
        if (id < 0)                             throw new IllegalArgumentException("id must be >= 0");
        if (reg == null || reg.isBlank())       throw new IllegalArgumentException("reg is required");
        if (make == null || make.isBlank())     throw new IllegalArgumentException("make is required");
        if (model == null || model.isBlank())   throw new IllegalArgumentException("model is required");
        if (dailyRate <= 0.0)                   throw new IllegalArgumentException("dailyRate must be > 0");
        if (status == null || status.isBlank()) throw new IllegalArgumentException("status is required");

        _id = id; _reg = reg.trim(); _make = make.trim();
        _model = model.trim(); _dailyRate = dailyRate; _status = status.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "Car{id=" + _id + ", reg='" + _reg + "', make='" + _make +
               "', model='" + _model + "', rate=" + _dailyRate + ", status=" + _status + "}";
    }
}
