package t06_collections_2.exercises.ex09;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class Exercise {

    public static int[] maxWindow(int[] a, int w) {
        if (a == null || w <= 0 || w > a.length) {
            return new int[0];
        }

        int n = a.length;
        int[] result = new int[n - w + 1];
        int outIndex = 0;

        Deque<Integer> dq = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) {
                dq.removeLast();
            }

            dq.addLast(i);

            int windowStart = i - w + 1;
            if (windowStart > dq.peekFirst()) {
                dq.removeFirst();
            }

            if (i >= w - 1) {
                result[outIndex++] = a[dq.peekFirst()];
            }
        }

        return result;
    }

    public static void run() {
        int[] input = {1, 3, -1, -3, 5, 3, 6, 7};
        int w = 3;
        int[] out = maxWindow(input, w);
        System.out.println(Arrays.toString(out));
    }
}
