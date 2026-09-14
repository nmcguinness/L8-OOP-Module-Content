package t13_design_patterns_1.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t13 ex06 - Command plus Strategy in a ticket dispatcher")
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

    // ---------- policy selection ----------

    @Test
    void select_ordinaryTicket_choosesImmediateProcessing() {
        OrderTicket t = new OrderTicket(201, "1 cappuccino", 7.80, 2);
        assertInstanceOf(ImmediateProcess.class, new ProcessingPolicySelector().select(t));
    }

    @Test
    void select_trainingInTheDescription_winsOverEveryOtherRule() {
        // Large and item-heavy, but "training" is checked first.
        OrderTicket t = new OrderTicket(203, "training: 8 lattes", 99.00, 20);
        assertInstanceOf(DryRunTraining.class, new ProcessingPolicySelector().select(t));
    }

    @Test
    void select_highValueTicket_choosesValidatedProcessing() {
        OrderTicket t = new OrderTicket(204, "8 americanos", 42.00, 2);
        assertInstanceOf(ValidatedProcess.class, new ProcessingPolicySelector().select(t));
    }

    @Test
    void select_manyItems_choosesValidatedProcessing() {
        OrderTicket t = new OrderTicket(205, "6 teas", 9.00, 6);
        assertInstanceOf(ValidatedProcess.class, new ProcessingPolicySelector().select(t));
    }

    @Test
    void select_justBelowBothThresholds_staysImmediate() {
        OrderTicket t = new OrderTicket(206, "5 teas", 29.99, 5);
        assertInstanceOf(ImmediateProcess.class, new ProcessingPolicySelector().select(t));
    }

    // ---------- ticket validity ----------

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

    // ---------- queue behaviour ----------

    @Test
    void validatedProcess_rejectsAnInvalidTicket() {
        OrderTicket blank = new OrderTicket(202, "   ", 40.00, 1);
        String out = capture(() -> new ValidatedProcess().process(blank));
        assertTrue(out.contains("REJECT 202"), out);
    }

    @Test
    void processAll_runsTicketsInFifoOrderThenEmpties() {
        TicketQueue queue = new TicketQueue();
        ProcessingPolicySelector selector = new ProcessingPolicySelector();

        OrderTicket first = new OrderTicket(201, "1 cappuccino", 7.80, 2);
        OrderTicket second = new OrderTicket(203, "training: 2 lattes", 12.50, 3);

        queue.add(new ProcessTicketCommand(first, selector.select(first)));
        queue.add(new ProcessTicketCommand(second, selector.select(second)));

        String out = capture(queue::processAll);

        assertTrue(out.indexOf("IMMEDIATE MAKE 201") < out.indexOf("TRAINING 203"), out);
        assertTrue(capture(queue::processAll).isEmpty(), "the queue must be drained");
    }
}
