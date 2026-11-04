package t15_dao.exercises.ex02;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Exercise {

    public static void run() {
        try {
            CarDao dao = new InMemoryCarDao();

            int a = dao.insert(new Car(0, "12-LH-1234", "Toyota", "Yaris", 45.0, "AVAILABLE"));
            System.out.println("Inserted car id=" + a);

            int b = dao.insert(new Car(0, "15-D-7788", "Ford", "Focus", 55.0, "MAINTENANCE"));
            System.out.println("Inserted car id=" + b);

            System.out.println("All cars:");
            for (Car c : dao.findAll()) {
                System.out.println(" - " + c);
            }

            System.out.println("Update status id=" + a + " -> " + dao.updateStatus(a, "RENTED"));
            System.out.println("Delete id=" + b + " -> " + dao.deleteById(b));

            System.out.println("All cars:");
            for (Car c : dao.findAll()) {
                System.out.println(" - " + c);
            }
        }
        catch (DaoException e) {
            System.out.println("Storage error: " + e.getMessage());
        }
    }
}

/**
 * The one checked exception a CarDao may throw. Callers never see SQLException,
 * so swapping the database (or using the in-memory version) changes nothing above the DAO.
 */
class DaoException extends Exception {

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}

class Car {

    private final int id;
    private final String reg;
    private final String make;
    private final String model;
    private final double dailyRate;
    private final String status;

    // id is 0 for a car that has not been stored yet; the DAO assigns the real id
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

    // Returns a copy with a different id; Car is immutable
    public Car withId(int id) {
        return new Car(id, reg, make, model, dailyRate, status);
    }

    // Returns a copy with a different status
    public Car withStatus(String status) {
        return new Car(id, reg, make, model, dailyRate, status);
    }

    @Override
    public String toString() {
        return "Car{id=" + id + ", reg='" + reg + "', make='" + make
                + "', model='" + model + "', rate=" + dailyRate + ", status=" + status + "}";
    }
}

interface CarDao {
    // Stores a new car (its id is ignored) and returns the generated id
    int insert(Car car) throws DaoException;
    Optional<Car> findById(int id) throws DaoException;
    List<Car> findAll() throws DaoException;
    boolean updateStatus(int id, String newStatus) throws DaoException;
    boolean deleteById(int id) throws DaoException;
}

class InMemoryCarDao implements CarDao {

    private final List<Car> cars = new ArrayList<>();
    private int nextId = 1;

    // No storage can fail here, so these methods declare no exceptions at all:
    // an implementation may throw fewer checked exceptions than its interface
    @Override
    public int insert(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car is required");
        }

        Car stored = car.withId(nextId);
        cars.add(stored);
        nextId++;
        return stored.id();
    }

    @Override
    public Optional<Car> findById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }

        for (Car c : cars) {
            if (c.id() == id) {
                return Optional.of(c);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public boolean updateStatus(int id, String newStatus) {
        if (id <= 0) {
            return false;
        }

        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("newStatus is required");
        }

        for (int i = 0; i < cars.size(); i++) {
            Car c = cars.get(i);
            if (c.id() == id) {
                cars.set(i, c.withStatus(newStatus));
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteById(int id) {
        if (id <= 0) {
            return false;
        }

        return cars.removeIf(c -> c.id() == id);
    }
}
