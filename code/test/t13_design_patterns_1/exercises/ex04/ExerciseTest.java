package t13_design_patterns_1.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t13 ex04 - MacroCommand: commands as building blocks")
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
    void execute_runsEveryChildCommandInTheOrderAdded() {
        MacroCommand macro = new MacroCommand();
        macro.add(new PrintCommand("start"));
        macro.add(new PrintCommand("validate"));
        macro.add(new PrintCommand("done"));

        assertEquals(List.of("start", "validate", "done"),
                capture(macro::execute).lines().toList());
    }

    @Test
    void execute_emptyMacro_printsNothingAndDoesNotThrow() {
        assertEquals("", capture(new MacroCommand()::execute));
    }

    @Test
    void execute_singleCommand_behavesLikeThatCommand() {
        MacroCommand macro = new MacroCommand();
        macro.add(new PrintCommand("only"));
        assertEquals(List.of("only"), capture(macro::execute).lines().toList());
    }

    @Test
    void macroCommand_isItselfACommandSoItCanNest() {
        MacroCommand inner = new MacroCommand();
        inner.add(new PrintCommand("inner-1"));
        inner.add(new PrintCommand("inner-2"));

        MacroCommand outer = new MacroCommand();
        outer.add(new PrintCommand("before"));
        outer.add(inner);          // a macro treated as an ordinary Command
        outer.add(new PrintCommand("after"));

        assertEquals(List.of("before", "inner-1", "inner-2", "after"),
                capture(outer::execute).lines().toList());
    }

    @Test
    void execute_isRepeatable() {
        MacroCommand macro = new MacroCommand();
        macro.add(new PrintCommand("x"));

        assertEquals(List.of("x"), capture(macro::execute).lines().toList());
        assertEquals(List.of("x"), capture(macro::execute).lines().toList());
    }
}
