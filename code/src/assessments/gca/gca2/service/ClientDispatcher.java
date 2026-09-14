package assessments.gca.gca2.service;

import assessments.gca.gca2.dao.TaskDAO;
import assessments.gca.gca2.domain.Task;
import assessments.gca.gca2.server.ClientRequest;
import assessments.gca.gca2.server.ServerResponse;

import java.util.List;
import java.util.Optional;

/**
 * Routes a parsed ClientRequest to the correct TaskDAO method and wraps the result
 * in a ServerResponse. Isolating routing logic here means dispatch() can be tested
 * independently - no socket or server required.
 *
 * Supported request types: INSERT, FIND_BY_ID, LIST, DELETE_BY_ID.
 * Any other type returns an error response.
 *
 * @author OOP Teaching Team
 */
public class ClientDispatcher {

    // === Fields ===
    private TaskDAO _taskDAO;

    // === Constructors ===

    // Creates: a ClientDispatcher that routes all requests to the given TaskDAO
    public ClientDispatcher(TaskDAO taskDAO) {
        _taskDAO = taskDAO;
    }

    // === Public API ===

    // Handles: routing the request to the correct handler method; unknown types return an error
    public ServerResponse<?> dispatch(ClientRequest request) {
        switch (request.getRequestType()) {
            case "INSERT":       return handleInsert(request);
            case "FIND_BY_ID":   return handleFindById(request);
            case "LIST":         return handleList();
            case "DELETE_BY_ID": return handleDeleteById(request);
            default:
                return ServerResponse.error("Unknown request type: " + request.getRequestType());
        }
    }

    // === Helpers ===

    // Handles: INSERT - builds a Task from payload fields, persists it, returns saved Task
    private ServerResponse<Task> handleInsert(ClientRequest request) {
        try {
            Task task = new Task(
                0,
                request.getString("title"),
                request.getString("description"),
                request.getBoolean("completed")
            );
            Task saved = _taskDAO.insert(task);
            return ServerResponse.ok("Task inserted with id " + saved.getTaskId(), saved);
        }
        catch (Exception e) {
            return ServerResponse.error("Insert failed: " + e.getMessage());
        }
    }

    // Handles: FIND_BY_ID - looks up taskId from payload; returns not-found message if absent
    private ServerResponse<Task> handleFindById(ClientRequest request) {
        try {
            int id = request.getInt("taskId");
            Optional<Task> result = _taskDAO.findById(id);
            if (result.isPresent())
                return ServerResponse.ok("Task found", result.get());
            return ServerResponse.error("No task found with id " + id);
        }
        catch (Exception e) {
            return ServerResponse.error("Find failed: " + e.getMessage());
        }
    }

    // Handles: LIST - returns all tasks
    private ServerResponse<List<Task>> handleList() {
        try {
            List<Task> tasks = _taskDAO.findAll();
            return ServerResponse.ok("Retrieved " + tasks.size() + " task(s)", tasks);
        }
        catch (Exception e) {
            return ServerResponse.error("List failed: " + e.getMessage());
        }
    }

    // Handles: DELETE_BY_ID - deletes the task with the given id; reports success or not-found
    private ServerResponse<Boolean> handleDeleteById(ClientRequest request) {
        try {
            int id = request.getInt("taskId");
            boolean deleted = _taskDAO.deleteById(id);
            if (deleted)
                return ServerResponse.ok("Task " + id + " deleted", true);
            return ServerResponse.error("No task found with id " + id);
        }
        catch (Exception e) {
            return ServerResponse.error("Delete failed: " + e.getMessage());
        }
    }
}
