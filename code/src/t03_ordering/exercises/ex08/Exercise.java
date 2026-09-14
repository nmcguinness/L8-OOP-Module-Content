package t03_ordering.exercises.ex08;

import t03_ordering.exercises.common.NameAscComparator;
import t03_ordering.exercises.common.Product;

import java.util.ArrayList;

public class Exercise {
    public static void run() {
        var items = new ArrayList<Product>();
        items.add(new Product("Monitor", 120.0, 4.1));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Mouse", 15.0, 4.2));
        items.add(new Product("Mat", 12.0, 4.5));         // same rating as Keyboard
        items.add(new Product("Microphone", 45.0, 4.0));  // same price as Keyboard

        var chain = new ChainComparator<Product>(
            new PriceAsc(), new RatingDesc(), new NameAscComparator()
        );

        items.sort(chain);
        System.out.println("Sorted by price asc, then rating desc, then name asc:");
        for (Product p : items) System.out.println("  " + p);
    }
}
