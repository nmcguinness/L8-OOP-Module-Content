package t03_ordering.exercises.ex08;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ChainComparator<T> implements Comparator<T> {
    private final List<Comparator<T>> chain;

    @SafeVarargs
    public ChainComparator(Comparator<T>... comparators) {
        this.chain = Arrays.asList(comparators);
    }

    @Override public int compare(T a, T b) {
        for (Comparator<T> c : chain) {
            int r = c.compare(a, b);
            if (r != 0) return r;
        }
        return 0;
    }
}
