package t09_interface.exercises.ex05;

import java.util.Comparator;

public final class NamedComparators {

    private NamedComparators() {
    }

    public static Comparator<Named> byNameAscending() {
        return (a, b) -> a.getName().compareToIgnoreCase(b.getName());
    }

    public static Comparator<Named> byNameLength() {
        return (a, b) -> Integer.compare(a.getName().length(), b.getName().length());
    }
}
