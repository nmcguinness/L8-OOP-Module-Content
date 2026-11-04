package assessments.gca.gca2.dao;

import assessments.gca.gca2.db.DatabaseConnection;
import assessments.gca.gca2.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Solution to t23 Exercise 06: integration tests for TaskDAO.
 *
 * <p>Needs MySQL and a separate test database, gca2_test_db, with the same tasks table as
 * gca2_support_db. Tagged "database", so a plain {@code mvn test} skips it. Run it with
 * {@code mvn test -Pdb-tests}.
 */
@Tag("database")
@DisplayName("t23 ex06 - TaskDAO against a test database")
class TaskDAOTest {

    private static final String URL = "jdbc:mysql://localhost:3306/gca2_test_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private final DatabaseConnection db = new DatabaseConnection(URL, DB_USER, DB_PASS);
    private final TaskDAO dao = new TaskDAO(db);

    // Every test starts from an empty table, so no test depends on another
    @BeforeEach
    void emptyTheTable() throws Exception {
        try (Connection c = db.open(); Statement st = c.createStatement()) {
            st.executeUpdate("TRUNCATE TABLE tasks");
        }
    }

    @Test
    void insert_validTask_returnsTaskWithPositiveId() throws Exception {
        Task saved = dao.insert(new Task(0, "Fix login", "desc", false));

        assertTrue(saved.getTaskId() > 0);
    }

    @Test
    void findById_existingId_returnsPresentOptional() throws Exception {
        Task saved = dao.insert(new Task(0, "Fix login", "desc", false));

        assertEquals(saved, dao.findById(saved.getTaskId()).orElseThrow());
    }

    @Test
    void findById_unknownId_returnsEmptyOptional() throws Exception {
        assertTrue(dao.findById(9_999).isEmpty());
    }

    @Test
    void findAll_emptyTable_returnsEmptyListNotNull() throws Exception {
        assertNotNull(dao.findAll());
        assertTrue(dao.findAll().isEmpty());
    }

    @Test
    void deleteById_existingTask_returnsTrueAndRemovesRow() throws Exception {
        Task saved = dao.insert(new Task(0, "Fix login", "desc", false));

        assertTrue(dao.deleteById(saved.getTaskId()));
        assertTrue(dao.findById(saved.getTaskId()).isEmpty());
    }

    @Test
    void deleteById_unknownId_returnsFalse() throws Exception {
        assertFalse(dao.deleteById(9_999));
    }
}
