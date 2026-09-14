package t06_collections_2.exercises.ex04;

import java.util.Deque;
import java.util.LinkedList;
import java.util.ListIterator;

public class Exercise {

    public static void run() {
        LinkedList<String> jobs = new LinkedList<>();
        Deque<String> q = jobs;

        q.offer("build");
        q.offer("test");
        q.offer("package");
        q.offer("deploy");

        LinkedList<String> executed = new LinkedList<>();

        while (!q.isEmpty()) {
            String job = q.poll();
            executed.add(job);
            System.out.println("Processing: " + job);

            if ("test".equals(job)) {
                ListIterator<String> it = jobs.listIterator();
                it.add("lint");
            }
        }

        System.out.println("Final execution order: " + executed);
    }
}
