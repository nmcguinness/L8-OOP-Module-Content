package assessments.gca.gca2.domain;

/**
 * Data transfer object representing a single task.
 * Fields are validated in the constructor; title is trimmed and must not be blank.
 * Includes a public no-arg constructor so Jackson can deserialise JSON into a Task.
 *
 * @author OOP Teaching Team
 */
public class Task {

    // === Fields ===
    private int     _taskId;
    private String  _title;
    private String  _description;
    private boolean _completed;

    // === Constructors ===

    // Creates: a no-arg Task required by Jackson for JSON deserialisation
    public Task() {
        _taskId      = 0;
        _title       = "";
        _description = "";
        _completed   = false;
    }

    // Creates: a Task with all fields; trims title and description, rejects a blank title
    public Task(int taskId, String title, String description, boolean completed) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title must not be null or blank");
        _taskId      = taskId;
        _title       = title.trim();
        _description = (description != null) ? description.trim() : "";
        _completed   = completed;
    }

    // === Public API ===

    // Gets: the auto-generated task_id; 0 means the task has not been persisted yet
    public int getTaskId() { return _taskId; }

    // Sets: the task_id - called after insert to store the generated key
    public void setTaskId(int taskId) { _taskId = taskId; }

    // Gets: the task title
    public String getTitle() { return _title; }

    // Sets: the task title; trims and rejects null or blank values
    public void setTitle(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title must not be null or blank");
        _title = title.trim();
    }

    // Gets: the task description; never null
    public String getDescription() { return _description; }

    // Sets: the task description; null is stored as empty string
    public void setDescription(String description) {
        _description = (description != null) ? description.trim() : "";
    }

    // Checks: whether the task is marked as completed
    public boolean isCompleted() { return _completed; }

    // Sets: the completed flag
    public void setCompleted(boolean completed) { _completed = completed; }

    // === Overrides ===

    @Override
    public int hashCode() {
        return Integer.hashCode(_taskId);
    }

    @Override
    public String toString() {
        return "Task{taskId=" + _taskId
             + ", title='"    + _title + '\''
             + ", completed=" + _completed + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task other = (Task) o;
        return _taskId == other._taskId
            && _title.equals(other._title)
            && _description.equals(other._description)
            && _completed == other._completed;
    }
}
