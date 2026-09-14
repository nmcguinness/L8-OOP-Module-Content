package t03_ordering.exercises.common;

import java.util.Comparator;

public class NameAscComparator implements Comparator<Product> {
    @Override public int compare(Product a, Product b) {
        return a.name().compareTo(b.name());
    }
}
