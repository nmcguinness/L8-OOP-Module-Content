package t13_design_patterns_1.exercises.ex06;

public class ImmediateProcess implements OrderProcessingStrategy {

    @Override
    public void process(OrderTicket ticket) {
        System.out.println("IMMEDIATE MAKE " + ticket.summary());
    }
}
