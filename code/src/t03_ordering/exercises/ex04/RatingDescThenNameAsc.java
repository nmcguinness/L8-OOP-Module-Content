package t03_ordering.exercises.ex04;

import t03_ordering.exercises.common.Product;

import java.util.Comparator;

public class RatingDescThenNameAsc implements Comparator<Product> {
    @Override public int compare(Product a, Product b) {
        int byRating = Double.compare(b.rating(), a.rating()); // desc
        if (byRating != 0) return byRating;
        return a.name().compareTo(b.name()); // asc
    }
}
