package t03_ordering.exercises.ex06;

import t03_ordering.exercises.common.Product;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    public static void run() {
        List<NewProduct> items = new ArrayList<>();
        items.add(new NewProduct("Monitor", 120.0, 4.1));
        items.add(new NewProduct("Keyboard", 45.0, null)); // null rating
        items.add(new NewProduct("Mouse", 15.0, 4.1));     // same rating as Monitor
        items.add(new NewProduct("Mat", 12.0, null));      // null rating

        items.sort(new RatingNullLast());
        System.out.println(items);

        // Quick check: all non-null ratings appear before null ratings
        boolean seenNull = false;
        boolean ok = true;
        for (NewProduct p : items) {
            if (p.rating() == null) seenNull = true;
            if (seenNull && p.rating() != null) { ok = false; break; }
        }
        System.out.println(ok);
    }
}
