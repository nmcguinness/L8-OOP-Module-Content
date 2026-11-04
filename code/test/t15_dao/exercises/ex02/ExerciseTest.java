package t15_dao.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t15 e02 - the Car domain type and the in-memory DAO")
class ExerciseTest {

    private static final double TOLERANCE = 1e-9;

    private static CarDao daoWithTwoCars() throws DaoException {
        CarDao dao = new InMemoryCarDao();
        dao.insert(new Car(0, "12-LH-1234", "Toyota", "Yaris", 45.0, "AVAILABLE"));
        dao.insert(new Car(0, "15-D-7788", "Ford", "Focus", 55.0, "MAINTENANCE"));
        return dao;
    }

    // ---------- Car ----------

    @Test
    void car_normalisesRegistrationCaseAndStatus() {
        Car c = new Car(1, "  12-lh-1234  ", " Toyota ", " Yaris ", 45.0, " available ");
        assertEquals("12-lh-1234", c.reg(), "reg is trimmed but not uppercased");
        assertEquals("Toyota", c.make());
        assertEquals("AVAILABLE", c.status(), "status is uppercased");
    }

    @Test
    void car_rejectsBlankRequiredFields() {
        assertThrows(IllegalArgumentException.class, () -> new Car(1, " ", "m", "mo", 1.0, "S"));
        assertThrows(IllegalArgumentException.class, () -> new Car(1, "r", " ", "mo", 1.0, "S"));
        assertThrows(IllegalArgumentException.class, () -> new Car(1, "r", "m", " ", 1.0, "S"));
        assertThrows(IllegalArgumentException.class, () -> new Car(1, "r", "m", "mo", 1.0, " "));
    }

    @Test
    void car_rejectsNegativeIdAndNonPositiveRate() {
        assertThrows(IllegalArgumentException.class, () -> new Car(-1, "r", "m", "mo", 1.0, "S"));
        assertThrows(IllegalArgumentException.class, () -> new Car(1, "r", "m", "mo", 0.0, "S"));
    }

    // ---------- insert / findById ----------

    @Test
    void insert_returnsIncreasingIdsStartingAtOne() throws DaoException {
        CarDao dao = new InMemoryCarDao();
        assertEquals(1, dao.insert(new Car(0, "r1", "m", "mo", 10.0, "AVAILABLE")));
        assertEquals(2, dao.insert(new Car(0, "r2", "m", "mo", 10.0, "AVAILABLE")));
    }

    @Test
    void findById_existingId_returnsThatCar() throws DaoException {
        Optional<Car> found = daoWithTwoCars().findById(1);
        assertTrue(found.isPresent());
        assertEquals("Toyota", found.get().make());
        assertEquals(45.0, found.get().dailyRate(), TOLERANCE);
    }

    @Test
    void findById_unknownOrInvalidId_returnsEmptyOptional() throws DaoException {
        CarDao dao = daoWithTwoCars();
        assertTrue(dao.findById(99).isEmpty());
        assertTrue(dao.findById(0).isEmpty());
        assertTrue(dao.findById(-1).isEmpty());
    }

    @Test
    void insert_blankReg_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new InMemoryCarDao().insert(new Car(0, " ", "m", "mo", 10.0, "AVAILABLE")));
    }

    @Test
    void insert_nullCar_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new InMemoryCarDao().insert(null));
    }

    @Test
    void insert_ignoresIncomingIdAndAssignsItsOwn() throws DaoException {
        CarDao dao = new InMemoryCarDao();
        int id = dao.insert(new Car(42, "r1", "m", "mo", 10.0, "AVAILABLE"));
        assertEquals(1, id);
        assertTrue(dao.findById(42).isEmpty());
    }

    // ---------- findAll ----------

    @Test
    void findAll_emptyDao_returnsEmptyListNotNull() throws DaoException {
        List<Car> all = new InMemoryCarDao().findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void findAll_returnsACopySoCallersCannotCorruptTheStore() throws DaoException {
        CarDao dao = daoWithTwoCars();
        dao.findAll().clear();
        assertEquals(2, dao.findAll().size(), "clearing the returned list must not empty the DAO");
    }

    // ---------- updateStatus ----------

    @Test
    void updateStatus_existingCar_returnsTrueAndChangesTheStatus() throws DaoException {
        CarDao dao = daoWithTwoCars();
        assertTrue(dao.updateStatus(1, "RENTED"));
        assertEquals("RENTED", dao.findById(1).orElseThrow().status());
    }

    @Test
    void updateStatus_preservesEveryOtherField() throws DaoException {
        CarDao dao = daoWithTwoCars();
        dao.updateStatus(1, "RENTED");
        Car c = dao.findById(1).orElseThrow();
        assertEquals("12-LH-1234", c.reg());
        assertEquals(45.0, c.dailyRate(), TOLERANCE);
    }

    @Test
    void updateStatus_unknownId_returnsFalse() throws DaoException {
        assertFalse(daoWithTwoCars().updateStatus(99, "RENTED"));
    }

    @Test
    void updateStatus_blankStatus_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> daoWithTwoCars().updateStatus(1, " "));
    }

    // ---------- deleteById ----------

    @Test
    void deleteById_existingCar_returnsTrueAndRemovesIt() throws DaoException {
        CarDao dao = daoWithTwoCars();
        assertTrue(dao.deleteById(2));
        assertEquals(1, dao.findAll().size());
        assertTrue(dao.findById(2).isEmpty());
    }

    @Test
    void deleteById_unknownId_returnsFalseAndChangesNothing() throws DaoException {
        CarDao dao = daoWithTwoCars();
        assertFalse(dao.deleteById(99));
        assertEquals(2, dao.findAll().size());
    }

    @Test
    void deleteById_doesNotReuseTheDeletedId() throws DaoException {
        CarDao dao = daoWithTwoCars();
        dao.deleteById(1);
        assertEquals(3, dao.insert(new Car(0, "r3", "m", "mo", 10.0, "AVAILABLE")),
                "ids must keep climbing, as a database auto-increment would");
    }
}
