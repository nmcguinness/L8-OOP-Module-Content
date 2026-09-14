package t15_dao.exercises.ex03;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Exercise {

    public static void run() throws Exception {

        String url = "jdbc:mysql://localhost:3306/car_rental";
        String user = "root";
        String pass = "";

        CarDao dao = new JdbcCarDao(url, user, pass);

        int id = dao.insert("24-D-9001", "Honda", "Civic", 62.5, "AVAILABLE");
        System.out.println("Inserted car id=" + id);

        System.out.println("Find by id -> " + dao.findById(id).orElse(null));

        List<Car> all = dao.findAll();
        System.out.println("Cars in DB -> " + all.size());
    }
}

interface CarDao {
    int insert(String reg, String make, String model, double dailyRate, String status) throws Exception;
    Optional<Car> findById(int id) throws Exception;
    List<Car> findAll() throws Exception;
    boolean updateStatus(int id, String newStatus) throws Exception;
    boolean deleteById(int id) throws Exception;
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
    public int insert(String reg, String make, String model, double dailyRate, String status) throws Exception {

        if (reg == null || reg.isBlank())       throw new IllegalArgumentException("reg is required");
        if (make == null || make.isBlank())     throw new IllegalArgumentException("make is required");
        if (model == null || model.isBlank())   throw new IllegalArgumentException("model is required");
        if (dailyRate <= 0.0)                   throw new IllegalArgumentException("dailyRate must be > 0");
        if (status == null || status.isBlank()) throw new IllegalArgumentException("status is required");

        String sql = "INSERT INTO cars(reg, make, model, daily_rate, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reg.trim());
            ps.setString(2, make.trim());
            ps.setString(3, model.trim());
            ps.setDouble(4, dailyRate);
            ps.setString(5, status.trim().toUpperCase());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next())
                    throw new IllegalStateException("no generated key returned");
                return keys.getInt(1);
            }
        }
    }

    @Override
    public Optional<Car> findById(int id) throws Exception {
        if (id <= 0) return Optional.empty();

        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    @Override
    public List<Car> findAll() throws Exception {

        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Car> out = new ArrayList<>();
            while (rs.next())
                out.add(mapRow(rs));
            return out;
        }
    }

    @Override
    public boolean updateStatus(int id, String newStatus) throws Exception {
        if (id <= 0) return false;
        if (newStatus == null || newStatus.isBlank())
            throw new IllegalArgumentException("newStatus is required");

        String sql = "UPDATE cars SET status = ? WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newStatus.trim().toUpperCase());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(int id) throws Exception {
        if (id <= 0) return false;

        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
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

        _id        = id;
        _reg       = reg.trim();
        _make      = make.trim();
        _model     = model.trim();
        _dailyRate = dailyRate;
        _status    = status.trim().toUpperCase();
    }

    public int id()          { return _id; }
    public String reg()      { return _reg; }
    public String make()     { return _make; }
    public String model()    { return _model; }
    public double dailyRate(){ return _dailyRate; }
    public String status()   { return _status; }

    @Override
    public String toString() {
        return "Car{id=" + _id + ", reg='" + _reg + "', make='" + _make +
               "', model='" + _model + "', rate=" + _dailyRate + ", status=" + _status + "}";
    }
}
