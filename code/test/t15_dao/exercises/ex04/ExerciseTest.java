package t15_dao.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The service layer is tested here with **no database at all**.
 *
 * <p>That is the whole argument for defining {@code CarDao} as an interface: the
 * business rules in {@code CarRentalService} can be checked against a stub, so a
 * failure here is a rule bug, never a broken connection string.
 */
@DisplayName("t15 e04 - service-layer rules, tested against a stub DAO")
class ExerciseTest {

    /** A minimal in-memory CarDao standing in for JdbcCarDao. */
    private static final class StubCarDao implements CarDao {
        private final List<Car> cars = new ArrayList<>();
        private int nextId = 1;
        private int updateStatusCalls;

        @Override
        public int insert(String reg, String make, String model, double dailyRate, String status) {
            cars.add(new Car(nextId, reg, make, model, dailyRate, status));
            return nextId++;
        }

        @Override
        public Optional<Car> findById(int id) {
            return cars.stream().filter(c -> c.id() == id).findFirst();
        }

        @Override
        public List<Car> findAll() {
            return new ArrayList<>(cars);
        }

        @Override
        public List<Car> findByStatus(String status) {
            return cars.stream().filter(c -> c.status().equals(status)).toList();
        }

        @Override
        public boolean updateStatus(int id, String newStatus) {
            updateStatusCalls++;
            for (int i = 0; i < cars.size(); i++) {
                Car c = cars.get(i);
                if (c.id() == id) {
                    cars.set(i, new Car(c.id(), c.reg(), c.make(), c.model(), c.dailyRate(), newStatus));
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean deleteById(int id) {
            return cars.removeIf(c -> c.id() == id);
        }
    }

    @Test
    void constructor_nullDao_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CarRentalService(null));
    }

    @Test
    void addCar_alwaysStartsLifeAsAvailable() throws Exception {
        StubCarDao dao = new StubCarDao();
        int id = new CarRentalService(dao).addCar("12-LH-1", "Toyota", "Yaris", 45.0);
        assertEquals("AVAILABLE", dao.findById(id).orElseThrow().status());
    }

    @Test
    void rentCar_availableCar_succeedsAndMarksItRented() throws Exception {
        StubCarDao dao = new StubCarDao();
        CarRentalService service = new CarRentalService(dao);
        int id = service.addCar("12-LH-1", "Toyota", "Yaris", 45.0);

        assertTrue(service.rentCar(id));
        assertEquals("RENTED", dao.findById(id).orElseThrow().status());
    }

    @Test
    void rentCar_alreadyRented_isRefused() throws Exception {
        StubCarDao dao = new StubCarDao();
        CarRentalService service = new CarRentalService(dao);
        int id = service.addCar("12-LH-1", "Toyota", "Yaris", 45.0);
        service.rentCar(id);

        assertFalse(service.rentCar(id), "a rented car cannot be rented twice");
    }

    @Test
    void rentCar_carUnderMaintenance_isRefused() throws Exception {
        StubCarDao dao = new StubCarDao();
        int id = dao.insert("12-LH-1", "Toyota", "Yaris", 45.0, "MAINTENANCE");

        assertFalse(new CarRentalService(dao).rentCar(id));
    }

    @Test
    void rentCar_unknownId_returnsFalseWithoutTouchingTheDao() throws Exception {
        StubCarDao dao = new StubCarDao();
        assertFalse(new CarRentalService(dao).rentCar(99));
        assertEquals(0, dao.updateStatusCalls, "a missing car must not trigger a write");
    }

    @Test
    void returnCar_rentedCar_succeedsAndMarksItAvailable() throws Exception {
        StubCarDao dao = new StubCarDao();
        CarRentalService service = new CarRentalService(dao);
        int id = service.addCar("12-LH-1", "Toyota", "Yaris", 45.0);
        service.rentCar(id);

        assertTrue(service.returnCar(id));
        assertEquals("AVAILABLE", dao.findById(id).orElseThrow().status());
    }

    @Test
    void returnCar_carThatWasNeverRented_isRefused() throws Exception {
        StubCarDao dao = new StubCarDao();
        CarRentalService service = new CarRentalService(dao);
        int id = service.addCar("12-LH-1", "Toyota", "Yaris", 45.0);

        assertFalse(service.returnCar(id));
    }

    @Test
    void returnCar_unknownId_returnsFalse() throws Exception {
        assertFalse(new CarRentalService(new StubCarDao()).returnCar(99));
    }

    @Test
    void listAvailable_excludesRentedAndMaintenanceCars() throws Exception {
        StubCarDao dao = new StubCarDao();
        CarRentalService service = new CarRentalService(dao);
        int free = service.addCar("12-LH-1", "Toyota", "Yaris", 45.0);
        int rented = service.addCar("15-D-2", "Ford", "Focus", 55.0);
        dao.insert("16-C-3", "Opel", "Corsa", 40.0, "MAINTENANCE");
        service.rentCar(rented);

        List<Car> available = service.listAvailable();

        assertEquals(1, available.size());
        assertEquals(free, available.get(0).id());
    }

    @Test
    void listAvailable_noCars_returnsEmptyListNotNull() throws Exception {
        assertTrue(new CarRentalService(new StubCarDao()).listAvailable().isEmpty());
    }
}
