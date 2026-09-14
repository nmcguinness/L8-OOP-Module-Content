package t05_collections_1.exercises.ex05;

import java.util.ArrayList;

public class Exercise {

    public static long timeAppend(int n) {
        ArrayList<Integer> list = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        long end = System.nanoTime();
        return end - start;
    }

    public static long timeInsertFront(int n) {
        ArrayList<Integer> list = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            list.add(0, i);  // insert at front, shifts all existing elements
        }
        long end = System.nanoTime();
        return end - start;
    }

    public static void run() {
        long appendNanos = timeAppend(50_000);
        long insertNanos = timeInsertFront(10_000);

        System.out.printf(
            "append=%dus, front-insert=%dus%n",
            appendNanos / 1_000,
            insertNanos / 1_000
        );
    }
}
