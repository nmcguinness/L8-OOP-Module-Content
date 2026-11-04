package t13_design_patterns_1.exercises.ex07;

import t13_design_patterns_1.exercises.ex06.DryRunTraining;
import t13_design_patterns_1.exercises.ex06.ImmediateProcess;
import t13_design_patterns_1.exercises.ex06.OrderProcessingStrategy;
import t13_design_patterns_1.exercises.ex06.OrderTicket;
import t13_design_patterns_1.exercises.ex06.ValidatedProcess;

import java.util.Locale;

/**
 * The one place that decides which policy a ticket gets.
 */
public class ProcessingPolicySelector {

    private final OrderProcessingStrategy immediate = new ImmediateProcess();
    private final OrderProcessingStrategy validated = new ValidatedProcess();
    private final OrderProcessingStrategy training = new DryRunTraining();

    public OrderProcessingStrategy select(OrderTicket ticket) {
        String description = ticket.getDescription();
        if (description != null && description.toLowerCase(Locale.ROOT).contains("training")) {
            return training;
        }
        if (!ticket.isValid() || ticket.getTotalEuro() >= 30.0 || ticket.getItemCount() >= 6) {
            return validated;
        }
        return immediate;
    }
}
