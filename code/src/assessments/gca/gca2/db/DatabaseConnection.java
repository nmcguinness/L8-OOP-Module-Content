package assessments.gca.gca2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Wraps JDBC connection credentials and provides open/close helpers.
 * Call open() to obtain a fresh Connection, then pass it to close() when done.
 * Each open() call creates a new Connection - no connection is stored internally.
 *
 * @author OOP Teaching Team
 */
public class DatabaseConnection {

    // === Fields ===
    private String _url;
    private String _user;
    private String _pass;

    // === Constructors ===

    // Creates: a DatabaseConnection with the given JDBC URL and credentials
    public DatabaseConnection(String url, String user, String pass) {
        _url  = url;
        _user = user;
        _pass = pass;
    }

    // === Public API ===

    // Opens: a new JDBC Connection using the stored credentials; caller must close it
    public Connection open() throws SQLException {
        return open(_url, _user, _pass);
    }

    public Connection open(String url, String user, String pass) throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    // Closes: the given Connection safely; ignores null and already-closed connections
    public void close(Connection connection) {
        if (connection == null)
            return;
        try {
            if (!connection.isClosed())
                connection.close();
        }
        catch (SQLException e) {
            System.err.println("DatabaseConnection.close error: " + e.getMessage());
        }
    }
}
