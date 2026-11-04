package t13_design_patterns_1.exercises.ex06;

public class DryRunTraining implements OrderProcessingStrategy {

    @Override
    public void process(OrderTicket ticket) {
        System.out.println("TRAINING would make " + ticket.summary());
    }
}
