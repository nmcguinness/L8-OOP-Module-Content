package t06_collections_2.exercises.ex07;

import java.util.LinkedList;
import java.util.ListIterator;

public class Exercise {

    public static int josephus(int n, int k) {
        LinkedList<Integer> players = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            players.add(i);
        }

        ListIterator<Integer> it = players.listIterator();
        int step = 0;

        while (players.size() > 1) {
            if (!it.hasNext()) {
                it = players.listIterator();
            }
            it.next();
            step++;

            if (step == k) {
                it.remove();
                step = 0;
            }
        }

        return players.getFirst();
    }

    public static void run() {
        int winner = josephus(7, 3);
        System.out.println("Winner: " + winner);
    }
}
