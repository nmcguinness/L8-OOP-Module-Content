package t03_ordering.exercises.ex06;

import java.util.Comparator;

public class RatingNullLast implements Comparator<NewProduct> {
    @Override public int compare(NewProduct a, NewProduct b) {
        int byRating = safeCompareDescNullLast(a.rating(), b.rating());
        if (byRating != 0) return byRating;
        return a.name().compareTo(b.name());
    }

    private static int safeCompareDescNullLast(Double ra, Double rb) {
        if (ra == null && rb == null) return 0;
        if (ra == null) return 1;   // null after any non-null
        if (rb == null) return -1;  // any non-null before null
        return Double.compare(rb, ra); // both non-null: descending
    }
}
