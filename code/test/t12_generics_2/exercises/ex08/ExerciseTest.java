package t12_generics_2.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t12 ex08 - Comparator<? super T>")
class ExerciseTest {

    @Test
    void sortWith_comparatorOfTheExactType_sorts() {
        ArrayList<String> items = new ArrayList<>(List.of("pear", "fig", "apple"));
        Sorters.sortWith(items, Comparator.naturalOrder());
        assertEquals(List.of("apple", "fig", "pear"), items);
    }

    @Test
    void sortWith_comparatorOfASupertype_isAccepted() {
        // A Comparator<Entity> can order a List<Enemy> - that is what ? super T buys.
        ArrayList<Enemy> enemies = new ArrayList<>(List.of(new Enemy(), new Enemy()));
        Comparator<Entity> cmp = (a, b) -> 0;
        Sorters.sortWith(enemies, cmp);
        assertEquals(2, enemies.size());
    }

    @Test
    void sortWith_sortsInPlaceRatherThanReturningACopy() {
        ArrayList<Integer> items = new ArrayList<>(List.of(3, 1, 2));
        Sorters.sortWith(items, Comparator.naturalOrder());
        assertEquals(List.of(1, 2, 3), items, "the list passed in must itself be reordered");
    }

    @Test
    void sortWith_reversedComparator_ordersDescending() {
        ArrayList<Integer> items = new ArrayList<>(List.of(1, 3, 2));
        Sorters.sortWith(items, Comparator.<Integer>naturalOrder().reversed());
        assertEquals(List.of(3, 2, 1), items);
    }

    @Test
    void sortWith_nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> Sorters.sortWith(null, Comparator.<Integer>naturalOrder()));
        assertThrows(NullPointerException.class,
                () -> Sorters.sortWith(new ArrayList<Integer>(), null));
    }
}
