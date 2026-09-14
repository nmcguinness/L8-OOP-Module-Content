package t06_collections_2.exercises.ex10;

import java.util.Deque;
import java.util.LinkedList;

public class Exercise {

    private static class Action {
        String name;

        Action(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static Action next(Deque<Action> hi, Deque<Action> mid, Deque<Action> lo) {
        if (!hi.isEmpty()) {
            return hi.pollFirst();
        }
        if (!mid.isEmpty()) {
            return mid.pollFirst();
        }
        if (!lo.isEmpty()) {
            return lo.pollFirst();
        }
        return null;
    }

    public static void run() {
        Deque<Action> hi = new LinkedList<>();
        Deque<Action> mid = new LinkedList<>();
        Deque<Action> lo = new LinkedList<>();

        lo.addLast(new Action("GatherResources"));
        mid.addLast(new Action("Patrol"));
        hi.addLast(new Action("BossAttack"));
        hi.addLast(new Action("Shield"));
        mid.addLast(new Action("Heal"));

        while (true) {
            Action a = next(hi, mid, lo);
            if (a == null) {
                break;
            }

            System.out.println("Executing: " + a);

            if ("BossAttack".equals(a.name) && !hi.isEmpty()) {
                while (!hi.isEmpty()) {
                    mid.addLast(hi.pollFirst());
                }
            }
        }
    }
}
