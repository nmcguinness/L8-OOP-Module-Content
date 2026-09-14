package t03_ordering.exercises.ex08;

import t03_ordering.exercises.common.Product;

import java.util.Comparator;

public class RatingDesc implements Comparator<Product> {
    @Override public int compare(Product a, Product b) {
        return Double.compare(b.rating(), a.rating());
    }
}
