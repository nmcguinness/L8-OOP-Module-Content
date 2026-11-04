package t12_generics_2.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t12 ex02 - printAll(List<?>)")
class ExerciseTest {

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void printAll_acceptsListsOfDifferentTypes() {
        String out = capture(() -> {
            Lists.printAll(List.of("sword"));
            Lists.printAll(List.of(10));
            Lists.printAll(List.of(new Enemy("Orc")));
        });

        assertEquals(List.of("  sword", "  10", "  Orc"), out.lines().toList());
    }

    @Test
    void printAll_null_printsNothing() {
        assertTrue(capture(() -> Lists.printAll(null)).isEmpty());
    }
}
