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
    private int taskId;
    private String title;
    private String description;
    private boolean completed;

    // === Constructors ===

    // Creates: a no-arg Task required by Jackson for JSON deserialisation
    public Task() {
        taskId = 0;
        title = "";
        description = "";
        completed = false;
    }

    // Creates: a Task with all fields; trims title and description, rejects a blank title
    public Task(int taskId, String title, String description, boolean completed) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title must not be null or blank");
        this.taskId = taskId;
        this.title = title.trim();
        this.description = (description != null) ? description.trim() : "";
        this.completed = completed;
    }

    // === Public API ===

    // Gets: the auto-generated task_id; 0 means the task has not been persisted yet
    public int getTaskId() { return taskId; }

    // Sets: the task_id - called after insert to store the generated key
    public void setTaskId(int taskId) { this.taskId = taskId; }

    // Gets: the task title
    public String getTitle() { return title; }

    // Sets: the task title; trims and rejects null or blank values
    public void setTitle(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title must not be null or blank");
        this.title = title.trim();
    }

    // Gets: the task description; never null
    public String getDescription() { return description; }

    // Sets: the task description; null is stored as empty string
    public void setDescription(String description) {
        this.description = (description != null) ? description.trim() : "";
    }

    // Checks: whether the task is marked as completed
    public boolean isCompleted() { return completed; }

    // Sets: the completed flag
    public void setCompleted(boolean completed) { this.completed = completed; }

    // === Overrides ===

    @Override
    public int hashCode() {
        return Integer.hashCode(taskId);
    }

    @Override
    public String toString() {
        return "Task{taskId=" + taskId
             + ", title='"    + title + '\''
             + ", completed=" + completed + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task other = (Task) o;
        return taskId == other.taskId
            && title.equals(other.title)
            && description.equals(other.description)
            && completed == other.completed;
    }
}
