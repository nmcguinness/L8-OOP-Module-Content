package t15_dao.exercises.ex04;

import t15_dao.exercises.DbConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        CarRentalService service = new CarRentalService(dao);

        // The presentation layer is the one place that decides what a storage failure means
        try {
            int a = service.addCar("23-LH-1111", "Toyota", "Corolla", 58.0);
            int b = service.addCar("22-D-2222", "VW", "Golf", 60.0);

            System.out.println("Added cars: " + a + ", " + b);
            System.out.println("Rent car " + a + " -> " + service.rentCar(a));

            System.out.println("Available cars:");
            for (Car c : service.listAvailable()) {
                System.out.println(" - " + c);
            }

            System.out.println("Return car " + a + " -> " + service.returnCar(a));

            System.out.println("Available cars:");
            for (Car c : service.listAvailable()) {
                System.out.println(" - " + c);
            }
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

class CarRentalService {

    private static final String AVAILABLE = "AVAILABLE";
    private static final String RENTED = "RENTED";

    private final CarDao dao;

    public CarRentalService(CarDao dao) {
        if (dao == null) {
            throw new IllegalArgumentException("dao is null");
        }
        this.dao = dao;
    }

    // Business rule: every new car starts life as AVAILABLE
    public int addCar(String reg, String make, String model, double dailyRate) throws DaoException {
        return dao.insert(new Car(0, reg, make, model, dailyRate, AVAILABLE));
    }

    public boolean rentCar(int carId) throws DaoException {
        Optional<Car> c = dao.findById(carId);
        if (c.isEmpty()) {
            return false;
        }
        if (!c.get().status().equals(AVAILABLE)) {
            return false;
        }
        return dao.updateStatus(carId, RENTED);
    }

    public boolean returnCar(int carId) throws DaoException {
        Optional<Car> c = dao.findById(carId);
        if (c.isEmpty()) {
            return false;
        }
        if (!c.get().status().equals(RENTED)) {
            return false;
        }
        return dao.updateStatus(carId, AVAILABLE);
    }

    public List<Car> listAvailable() throws DaoException {
        return dao.findByStatus(AVAILABLE);
    }
}

interface CarDao {
    int insert(Car car) throws DaoException;
    Optional<Car> findById(int id) throws DaoException;
    List<Car> findAll() throws DaoException;
    List<Car> findByStatus(String status) throws DaoException;
    boolean updateStatus(int id, String newStatus) throws DaoException;
    boolean deleteById(int id) throws DaoException;
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
    public int insert(Car car) throws DaoException {
        if (car == null) {
            throw new IllegalArgumentException("car is required");
        }

        String sql = "INSERT INTO cars(reg, make, model, daily_rate, status) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, car.reg());
            ps.setString(2, car.make());
            ps.setString(3, car.model());
            ps.setDouble(4, car.dailyRate());
            ps.setString(5, car.status());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new DaoException("insert returned no generated key", null);
                }
                return keys.getInt(1);
            }
        }
        catch (SQLException e) {
            throw new DaoException("Could not insert car " + car.reg(), e);
        }
    }

    @Override
    public Optional<Car> findById(int id) throws DaoException {
        if (id <= 0) {
            return Optional.empty();
        }

        String sql = "SELECT " + COLUMNS + " FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }
        }
        catch (SQLException e) {
            throw new DaoException("Could not find car id=" + id, e);
        }
    }

    @Override
    public List<Car> findAll() throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM cars ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Car> out = new ArrayList<>();
            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return out;
        }
        catch (SQLException e) {
            throw new DaoException("Could not list cars", e);
        }
    }

    @Override
    public List<Car> findByStatus(String status) throws DaoException {
        if (status == null || status.isBlank()) {
            return List.of();
        }

        String sql = "SELECT " + COLUMNS + " FROM cars WHERE status = ? ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                List<Car> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
        catch (SQLException e) {
            throw new DaoException("Could not list cars with status " + status, e);
        }
    }

    @Override
    public boolean updateStatus(int id, String newStatus) throws DaoException {
        if (id <= 0) {
            return false;
        }
        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("newStatus is required");
        }

        String sql = "UPDATE cars SET status = ? WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newStatus.trim().toUpperCase());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
        catch (SQLException e) {
            throw new DaoException("Could not update car id=" + id, e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        if (id <= 0) {
            return false;
        }

        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
        catch (SQLException e) {
            throw new DaoException("Could not delete car id=" + id, e);
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

    public int id() { return id; }
    public String reg() { return reg; }
    public String make() { return make; }
    public String model() { return model; }
    public double dailyRate() { return dailyRate; }
    public String status() { return status; }

    public Car withId(int id) {
        return new Car(id, reg, make, model, dailyRate, status);
    }

    public Car withStatus(String status) {
        return new Car(id, reg, make, model, dailyRate, status);
    }

    @Override
    public String toString() {
        return "Car{id=" + id + ", reg='" + reg + "', make='" + make
                + "', model='" + model + "', rate=" + dailyRate + ", status=" + status + "}";
    }
}
