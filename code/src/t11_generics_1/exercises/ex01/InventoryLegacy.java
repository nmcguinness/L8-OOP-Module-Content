package t11_generics_1.exercises.ex01;

import java.util.ArrayList;
import java.util.List;

/**
 * The old way: everything goes in and comes out as Object, so every caller must cast.
 */
public class InventoryLegacy {

    private final List<Object> items = new ArrayList<>();

    public void add(Object item) {
        items.add(item);
    }

    public Object get(int index) {
        return items.get(index);
    }
}
