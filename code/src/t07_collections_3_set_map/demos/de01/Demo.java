package t07_collections_3_set_map.demos.de01;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Four ways a hashed or sorted collection surprises you, each shown with real output.
 * Used by Core Ideas 2, 4 and 9 in the t07 notes.
 */
public class Demo {

    // Overrides equals but NOT hashCode: breaks the contract from t04
    static final class BrokenPoint {
        private final int x;
        private final int y;

        BrokenPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BrokenPoint other)) return false;
            return x == other.x && y == other.y;
        }
    }

    // Overrides both equals and hashCode from the same fields
    static final class Point {
        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point other)) return false;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // A mutable key: its hash changes when its field changes
    static final class MutableKey {
        private String name;

        MutableKey(String name) {
            this.name = name;
        }

        void rename(String newName) {
            name = newName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MutableKey other)) return false;
            return name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public static void run() {
        brokenHashCode();
        mutableKey();
        treeSetDropsCompareEqual();
        priorityQueueIterationOrder();
    }

    static void brokenHashCode() {
        Set<BrokenPoint> broken = new HashSet<>();
        broken.add(new BrokenPoint(1, 2));
        broken.add(new BrokenPoint(1, 2));

        Set<Point> fixed = new HashSet<>();
        fixed.add(new Point(1, 2));
        fixed.add(new Point(1, 2));

        System.out.println("1. equals without hashCode");
        // Usually 2: the two equal objects land in different buckets
        System.out.println("   BrokenPoint set size: " + broken.size());
        System.out.println("   BrokenPoint contains (1,2)? "
                + broken.contains(new BrokenPoint(1, 2)));
        System.out.println("   Point set size: " + fixed.size());
        System.out.println("   Point contains (1,2)? " + fixed.contains(new Point(1, 2)));
    }

    static void mutableKey() {
        Map<MutableKey, Integer> scores = new HashMap<>();
        MutableKey key = new MutableKey("ana");
        scores.put(key, 90);

        // The entry is still stored under the bucket for "ana"
        key.rename("ben");

        System.out.println("2. mutating a key after put");
        System.out.println("   size: " + scores.size());
        System.out.println("   get(same object): " + scores.get(key));
        System.out.println("   get(new \"ana\"): " + scores.get(new MutableKey("ana")));
    }

    static void treeSetDropsCompareEqual() {
        TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
        byLength.add("cat");
        byLength.add("elephant");
        byLength.add("ox");
        // "dog" has the same length as "cat", so compare() returns 0 and it is not added
        boolean added = byLength.add("dog");

        TreeSet<String> byLengthThenText = new TreeSet<>(
                Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()));
        byLengthThenText.addAll(Set.of("cat", "elephant", "ox", "dog"));

        System.out.println("3. TreeSet uses compare(), not equals()");
        System.out.println("   add(\"dog\") returned " + added + ": " + byLength);
        System.out.println("   with a tie-breaker: " + byLengthThenText);
    }

    static void priorityQueueIterationOrder() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(10);
        pq.add(20);
        pq.add(30);
        pq.add(5);

        System.out.println("4. PriorityQueue printing is not sorted");
        System.out.println("   toString (heap order): " + pq);

        StringBuilder drained = new StringBuilder();
        while (!pq.isEmpty()) {
            drained.append(pq.poll()).append(' ');
        }
        System.out.println("   poll() order: " + drained.toString().trim());
    }
}
