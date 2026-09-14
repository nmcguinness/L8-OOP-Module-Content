package t08_inheritance.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t08 ex06 - override versus overload")
class ExerciseTest {

    @Test
    void format_onBaseFormatter_returnsTheBaseText() {
        assertEquals("Base format", new BaseFormatter().format());
    }

    @Test
    void format_onFancyFormatter_returnsTheOverriddenText() {
        assertEquals("Fancy format", new FancyFormatter().format());
    }

    @Test
    void format_fancyHeldInABaseVariable_stillUsesTheOverride() {
        // This is the override: same signature, so the runtime type decides.
        BaseFormatter b = new FancyFormatter();
        assertEquals("Fancy format", b.format());
    }

    @Test
    void formatWithPrefix_isAnOverloadAndCallsTheOverriddenNoArgFormat() {
        // This is the overload: a different signature, so it is a separate method.
        assertEquals(">> Fancy format", new FancyFormatter().format(">>"));
    }

    @Test
    void formatWithPrefix_isNotVisibleThroughTheBaseType() throws Exception {
        // The overload exists only on FancyFormatter, so BaseFormatter cannot see it.
        assertThrows(NoSuchMethodException.class,
                () -> BaseFormatter.class.getMethod("format", String.class),
                "format(String) must not be declared on BaseFormatter");
        assertEquals(">> Fancy format",
                FancyFormatter.class.getMethod("format", String.class)
                        .invoke(new FancyFormatter(), ">>"));
    }
}
