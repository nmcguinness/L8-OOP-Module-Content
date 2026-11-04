package t13_design_patterns_1.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t13 ex06 - café tickets: policies, commands and a queue")
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
    void isValid_blankDescription_isFalse() {
        assertFalse(new OrderTicket(202, "   ", 5.00, 1).isValid());
    }

    @Test
    void isValid_nullDescriptionZeroTotalOrNoItems_isFalse() {
        assertFalse(new OrderTicket(1, null, 5.00, 1).isValid());
        assertFalse(new OrderTicket(2, "coffee", 0.0, 1).isValid());
        assertFalse(new OrderTicket(3, "coffee", 5.00, 0).isValid());
    }

    @Test
    void isValid_wellFormedTicket_isTrue() {
        assertTrue(new OrderTicket(4, "coffee", 5.00, 1).isValid());
    }

    @Test
    void summary_showsIdDescriptionAndTwoDecimalPlaces() {
        assertEquals("201 1 cappuccino (€7.80)",
                new OrderTicket(201, "1 cappuccino", 7.8, 2).summary());
    }

    @Test
    void validatedProcess_rejectsAnInvalidTicket() {
        OrderTicket blank = new OrderTicket(202, "   ", 40.00, 1);
        String out = capture(() -> new ValidatedProcess().process(blank));
        assertTrue(out.contains("REJECT 202"), out);
    }

    @Test
    void dryRunTraining_neverMakesTheOrder() {
        OrderTicket t = new OrderTicket(203, "2 lattes", 12.50, 2);
        String out = capture(() -> new DryRunTraining().process(t));
        assertTrue(out.startsWith("TRAINING"), out);
        assertFalse(out.contains("MAKE"), out);
    }

    @Test
    void processAll_runsTicketsInFifoOrderThenEmpties() {
        TicketQueue queue = new TicketQueue();
        queue.add(new ProcessTicketCommand(
                new OrderTicket(201, "1 cappuccino", 7.80, 2), new ImmediateProcess()));
        queue.add(new ProcessTicketCommand(
                new OrderTicket(203, "2 lattes", 12.50, 3), new DryRunTraining()));

        String out = capture(queue::processAll);

        assertTrue(out.indexOf("IMMEDIATE MAKE 201") < out.indexOf("TRAINING"), out);
        assertEquals(0, queue.size());
    }

    @Test
    void processAll_keepsGoingAfterARejectedTicket() {
        TicketQueue queue = new TicketQueue();
        OrderTicket blank = new OrderTicket(1, "", 5.00, 1);
        OrderTicket tea = new OrderTicket(2, "tea", 3.00, 1);
        queue.add(new ProcessTicketCommand(blank, new ValidatedProcess()));
        queue.add(new ProcessTicketCommand(tea, new ImmediateProcess()));

        String out = capture(queue::processAll);

        assertTrue(out.contains("REJECT 1") && out.contains("IMMEDIATE MAKE 2"), out);
    }
}
