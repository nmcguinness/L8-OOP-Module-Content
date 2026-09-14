package t03_ordering.exercises.ex05;

import t03_ordering.exercises.common.NameAscComparator;
import t03_ordering.exercises.common.Product;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Exercise {
    public static void run() {
        List<Product> items = new ArrayList<>();
        items.add(new Product("Monitor", 120.0, 4.1));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Mouse", 45.0, 4.2));   // same price as Keyboard
        items.add(new Product("Mat", 12.0, 4.5));

        // 1) Sort by name A-Z
        items.sort(new NameAscComparator());
        System.out.println("By name: " + items);

        // 2) Sort by price low->high (anonymous comparator)
        Comparator<Product> priceAsc = new Comparator<Product>() {
            @Override public int compare(Product a, Product b) {
                return Double.compare(a.price(), b.price());
            }
        };
        items.sort(priceAsc);
        System.out.println("By price: " + items);

        // Stability note: among same-price items, their name order from step 1 is preserved.
    }
}
