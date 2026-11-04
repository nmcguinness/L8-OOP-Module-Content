package t13_design_patterns_1.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t13_design_patterns_1.exercises.ex06.DryRunTraining;
import t13_design_patterns_1.exercises.ex06.ImmediateProcess;
import t13_design_patterns_1.exercises.ex06.OrderTicket;
import t13_design_patterns_1.exercises.ex06.ValidatedProcess;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("t13 ex07 - café tickets: choosing the policy")
class ExerciseTest {

    private final ProcessingPolicySelector selector = new ProcessingPolicySelector();

    @Test
    void select_ordinaryTicket_choosesImmediateProcessing() {
        OrderTicket t = new OrderTicket(201, "1 cappuccino", 7.80, 2);
        assertInstanceOf(ImmediateProcess.class, selector.select(t));
    }

    @Test
    void select_trainingInTheDescription_winsOverEveryOtherRule() {
        OrderTicket t = new OrderTicket(203, "Training: 8 lattes", 99.00, 20);
        assertInstanceOf(DryRunTraining.class, selector.select(t));
    }

    @Test
    void select_highValueTicket_choosesValidatedProcessing() {
        OrderTicket t = new OrderTicket(204, "8 americanos", 42.00, 2);
        assertInstanceOf(ValidatedProcess.class, selector.select(t));
    }

    @Test
    void select_manyItems_choosesValidatedProcessing() {
        OrderTicket t = new OrderTicket(205, "6 teas", 9.00, 6);
        assertInstanceOf(ValidatedProcess.class, selector.select(t));
    }

    @Test
    void select_invalidTicket_choosesValidatedSoItIsRejected() {
        OrderTicket t = new OrderTicket(202, "   ", 5.00, 1);
        assertInstanceOf(ValidatedProcess.class, selector.select(t));
    }

    @Test
    void select_justBelowBothThresholds_staysImmediate() {
        OrderTicket t = new OrderTicket(206, "5 teas", 29.99, 5);
        assertInstanceOf(ImmediateProcess.class, selector.select(t));
    }
}
