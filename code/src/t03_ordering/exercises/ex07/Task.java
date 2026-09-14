package t03_ordering.exercises.ex07;

import java.time.LocalDate;

public class Task implements Comparable<Task> {
    private String title;
    private LocalDate dueDate;

    public Task(String title, LocalDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;
    }

    public String title() { return title; }
    public LocalDate dueDate() { return dueDate; }

    @Override public int compareTo(Task other) {
        return this.dueDate.compareTo(other.dueDate); // asc
    }

    @Override public String toString() {
        return title + " (due " + dueDate + ")";
    }
}
