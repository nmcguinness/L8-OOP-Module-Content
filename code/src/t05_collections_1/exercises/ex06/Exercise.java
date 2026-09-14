package t05_collections_1.exercises.ex06;

import java.util.ArrayList;

public class Exercise {

    static final class Task {
        final String title;
        int priority;

        Task(String t, int p) {
            title = t;
            priority = p;
        }
    }

    public static int indexOfTitle(ArrayList<Task> tasks, String title) {
        if (tasks == null || title == null)
            return -1;

        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t != null && title.equals(t.title)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean updatePriority(ArrayList<Task> tasks, String title, int newPri) {
        int idx = indexOfTitle(tasks, title);
        if (idx == -1)
            return false;

        Task t = tasks.get(idx);
        t.priority = newPri;
        return true;
    }

    public static void run() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Login", 2));
        tasks.add(new Task("Save", 1));

        System.out.println(indexOfTitle(tasks, "Save") == 1);
        System.out.println(updatePriority(tasks, "Save", 3));
        System.out.println(tasks.get(1).priority == 3);
    }
}
