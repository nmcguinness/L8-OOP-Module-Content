package assessments.gca.gca2.dao;

import assessments.gca.gca2.db.DatabaseConnection;
import assessments.gca.gca2.domain.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of GenericDAOInterface for Task entities.
 * Opens and closes a Connection per operation via DatabaseConnection.
 * All SQL uses PreparedStatement - no string concatenation.
 *
 * @author OOP Teaching Team
 */
public class TaskDAO implements GenericDAOInterface<Task, Integer> {

    // === Fields ===
    private DatabaseConnection _dbConn;

    // === Constructors ===

    // Creates: a TaskDAO that uses the given DatabaseConnection for all DB operations
    public TaskDAO(DatabaseConnection dbConn) {
        _dbConn = dbConn;
    }

    // === Public API ===

    // Inserts: a new task row and returns the Task with its generated task_id populated
    @Override
    public Task insert(Task task) throws Exception {
        String sql = "INSERT INTO tasks (title, description, completed) VALUES (?, ?, ?)";
        Connection c = _dbConn.open();
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,  task.getTitle());
            ps.setString(2,  task.getDescription());
            ps.setBoolean(3, task.isCompleted());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    task.setTaskId(keys.getInt(1));
            }
            return task;
        }
        finally {
            _dbConn.close(c);
        }
    }

    // Gets: the Task with the given id wrapped in Optional, or Optional.empty() if absent
    @Override
    public Optional<Task> findById(Integer id) throws Exception {
        if (id == null || id <= 0)
            return Optional.empty();

        String sql = "SELECT task_id, title, description, completed FROM tasks WHERE task_id = ?";
        Connection c = _dbConn.open();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
        finally {
            _dbConn.close(c);
        }
    }

    // Gets: all task rows as a List; returns an empty list if the table is empty
    @Override
    public List<Task> findAll() throws Exception {
        String sql = "SELECT task_id, title, description, completed FROM tasks";
        List<Task> results = new ArrayList<>();
        Connection c = _dbConn.open();
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                results.add(mapRow(rs));
            return results;
        }
        finally {
            _dbConn.close(c);
        }
    }

    // Deletes: the task with the given id; returns true if a row was removed
    @Override
    public boolean deleteById(Integer id) throws Exception {
        if (id == null || id <= 0)
            return false;

        String sql = "DELETE FROM tasks WHERE task_id = ?";
        Connection c = _dbConn.open();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
        finally {
            _dbConn.close(c);
        }
    }

    // === Helpers ===

    // Converts: one ResultSet row into a Task object
    private Task mapRow(ResultSet rs) throws Exception {
        return new Task(
            rs.getInt("task_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getBoolean("completed")
        );
    }
}
