package assessments.gca.gca2.db;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link DatabaseConnection}.
 *
 * <p>These are <em>integration</em> tests, not unit tests: they need a running MySQL
 * instance. They are tagged {@code database} and are therefore skipped by a plain
 * {@code mvn test}. Run them explicitly with:
 *
 * <pre>mvn test -Dgroups=database</pre>
 *
 * @author OOP Teaching Team
 */
@Tag("database")
class DatabaseConnectionTest {

    private static final String URL     = "jdbc:mysql://localhost:3306/gca2_support_db";
    private static final String BAD_URL = "jdbc:mysql://localhost:3306/no_such_database";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    @Test
    void open_validCredentials_returnsOpenConnection() throws SQLException {

        // Arrange
        DatabaseConnection db = new DatabaseConnection(URL, DB_USER, DB_PASS);

        // Act
        try (Connection conn = db.open()) {

            // Assert
            assertNotNull(conn, "open() must return a connection");
            assertTrue(conn.isValid(2), "connection should be usable");
        }
    }

    @Test
    void open_unknownDatabase_throwsSQLException() {

        // Arrange
        DatabaseConnection db = new DatabaseConnection(BAD_URL, DB_USER, DB_PASS);

        // Act + Assert - the failure IS the expected behaviour here
        assertThrows(SQLException.class, db::open);
    }

    @Test
    void close_nullConnection_doesNotThrow() {

        // Arrange
        DatabaseConnection db = new DatabaseConnection(URL, DB_USER, DB_PASS);

        // Act + Assert - close() is documented as null-safe
        db.close(null);
    }
}
