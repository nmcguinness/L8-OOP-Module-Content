package t12_generics_2.exercises.ex02;

import java.util.List;

public final class Lists {

    private Lists() {
    }

    // Works for a list of any element type; the elements can only be read, as Object
    public static void printAll(List<?> items) {
        if (items == null) {
            return;
        }
        for (Object item : items) {
            System.out.println("  " + item);
        }
    }
}
