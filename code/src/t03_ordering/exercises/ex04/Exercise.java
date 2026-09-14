package t03_ordering.exercises.ex04;

import t03_ordering.exercises.common.Product;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    public static void run() {
        List<Product> items = new ArrayList<>();
        items.add(new Product("Monitor", 120.0, 4.5));
        items.add(new Product("Mouse", 15.0, 4.5));   // tie on rating
        items.add(new Product("Keyboard", 45.0, 4.1));

        items.sort(new RatingDescThenNameAsc());
        System.out.println(items);

        // Quick checks:
        System.out.println(items.get(0).rating() >= items.get(1).rating());
        if (items.get(0).rating() == items.get(1).rating()) {
            System.out.println(items.get(0).name().compareTo(items.get(1).name()) <= 0);
        }
    }
}
