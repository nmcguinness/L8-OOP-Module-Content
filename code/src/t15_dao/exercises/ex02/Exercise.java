package t15_dao.exercises.ex02;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Exercise {

    public static void run() throws Exception {

        CarDao dao = new InMemoryCarDao();

        int a = dao.insert("12-LH-1234", "Toyota", "Yaris", 45.0, "AVAILABLE");
        System.out.println("Inserted car id=" + a);

        int b = dao.insert("15-D-7788", "Ford", "Focus", 55.0, "MAINTENANCE");
        System.out.println("Inserted car id=" + b);

        System.out.println("All cars:");
        for (Car c : dao.findAll())
            System.out.println(" - " + c);

        System.out.println("Update status id=" + a + " -> " + dao.updateStatus(a, "RENTED"));
        System.out.println("Delete id=" + b + " -> " + dao.deleteById(b));

        System.out.println("All cars:");
        for (Car c : dao.findAll())
            System.out.println(" - " + c);
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

        if (id < 0)                                 throw new IllegalArgumentException("id must be >= 0");
        if (reg == null || reg.isBlank())           throw new IllegalArgumentException("reg is required");
        if (make == null || make.isBlank())         throw new IllegalArgumentException("make is required");
        if (model == null || model.isBlank())       throw new IllegalArgumentException("model is required");
        if (dailyRate <= 0.0)                       throw new IllegalArgumentException("dailyRate must be > 0");
        if (status == null || status.isBlank())     throw new IllegalArgumentException("status is required");

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

interface CarDao {
    int insert(String reg, String make, String model, double dailyRate, String status) throws Exception;
    Optional<Car> findById(int id) throws Exception;
    List<Car> findAll() throws Exception;
    boolean updateStatus(int id, String newStatus) throws Exception;
    boolean deleteById(int id) throws Exception;
}

class InMemoryCarDao implements CarDao {

    private ArrayList<Car> _cars = new ArrayList<>();
    private int _nextId = 1;

    @Override
    public int insert(String reg, String make, String model, double dailyRate, String status) {
        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        Car c = new Car(_nextId, reg, make, model, dailyRate, status);
        _cars.add(c);
        _nextId++;
        return c.id();
    }

    @Override
    public Optional<Car> findById(int id) {
        if (id <= 0) return Optional.empty();

        for (Car c : _cars)
            if (c.id() == id)
                return Optional.of(c);

        return Optional.empty();
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(_cars);
    }

    @Override
    public boolean updateStatus(int id, String newStatus) {
        if (id <= 0) return false;

        if (newStatus == null || newStatus.isBlank())
            throw new IllegalArgumentException("newStatus is required");

        for (int i = 0; i < _cars.size(); i++) {
            Car c = _cars.get(i);
            if (c.id() == id) {
                _cars.set(i, new Car(c.id(), c.reg(), c.make(), c.model(), c.dailyRate(), newStatus));
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteById(int id) {
        if (id <= 0) return false;

        for (int i = 0; i < _cars.size(); i++) {
            if (_cars.get(i).id() == id) {
                _cars.remove(i);
                return true;
            }
        }

        return false;
    }
}
