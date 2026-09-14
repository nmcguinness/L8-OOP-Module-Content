package t03_ordering.exercises.ex03;

import t03_ordering.exercises.common.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Exercise {
    public static void run() {
        List<Product> items = new ArrayList<>();
        items.add(new Product("Monitor", 120.0, 4.1));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Mouse", 15.0, 4.2));

        // Anonymous comparator: price ascending
        Comparator<Product> priceAsc = new Comparator<Product>() {
            @Override public int compare(Product a, Product b) {
                return Double.compare(a.price(), b.price());
            }
        };

        items.sort(priceAsc);
        System.out.println(items);

        // Quick check: first price <= second
        System.out.println(items.get(0).price() <= items.get(1).price());
    }
}
