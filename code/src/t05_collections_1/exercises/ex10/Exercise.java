package t05_collections_1.exercises.ex10;

import java.util.ArrayList;
import java.util.Iterator;

public class Exercise {

    static final class Score {
        final String player;
        final int value;

        Score(String p, int v) {
            player = p;
            value = v;
        }

        @Override
        public String toString() {
            return player + ":" + value;
        }
    }

    public static void addScore(ArrayList<Score> list, String player, int value) {
        if (list == null)
            return;
        list.add(new Score(player, value));
    }

    public static boolean removeBelow(ArrayList<Score> list, int threshold) {
        if (list == null)
            return false;

        boolean removedAny = false;
        Iterator<Score> it = list.iterator();
        while (it.hasNext()) {
            Score s = it.next();
            if (s.value < threshold) {
                it.remove();
                removedAny = true;
            }
        }
        return removedAny;
    }

    public static int bestScore(ArrayList<Score> list) {
        if (list == null || list.isEmpty())
            return 0;

        int best = list.get(0).value;
        for (int i = 1; i < list.size(); i++) {
            int value = list.get(i).value;
            if (value > best) {
                best = value;
            }
        }
        return best;
    }

    public static ArrayList<Score> topN(ArrayList<Score> list, int n) {
        ArrayList<Score> result = new ArrayList<>();
        if (list == null || n <= 0)
            return result;

        int size = list.size();
        int count = n < size ? n : size;

        boolean[] used = new boolean[size];

        for (int k = 0; k < count; k++) {
            int bestIndex = -1;
            int bestValue = Integer.MIN_VALUE;

            for (int i = 0; i < size; i++) {
                if (used[i])
                    continue;

                int value = list.get(i).value;
                if (bestIndex == -1 || value > bestValue) {
                    bestIndex = i;
                    bestValue = value;
                }
            }

            if (bestIndex == -1)
                break;

            result.add(list.get(bestIndex));
            used[bestIndex] = true;
        }

        return result;
    }

    public static void run() {
        ArrayList<Score> S = new ArrayList<>();
        addScore(S, "Alex", 10);
        addScore(S, "Ben", 25);
        addScore(S, "Cara", 25);
        addScore(S, "Dee", 5);

        System.out.println(bestScore(S) == 25);
        System.out.println(removeBelow(S, 10));         // removes Dee
        System.out.println(S.size() == 3);
        System.out.println(topN(S, 2).toString().equals("[Ben:25, Cara:25]"));
    }
}
