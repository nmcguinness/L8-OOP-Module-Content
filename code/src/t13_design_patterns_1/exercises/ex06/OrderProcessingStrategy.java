package t13_design_patterns_1.exercises.ex06;

/**
 * A policy for how one ticket is handled.
 */
public interface OrderProcessingStrategy {

    void process(OrderTicket ticket);
}
