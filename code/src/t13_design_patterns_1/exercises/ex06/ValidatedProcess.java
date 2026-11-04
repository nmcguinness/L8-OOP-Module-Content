package t13_design_patterns_1.exercises.ex06;

public class ValidatedProcess implements OrderProcessingStrategy {

    @Override
    public void process(OrderTicket ticket) {
        if (!ticket.isValid()) {
            System.out.println("REJECT " + ticket.getTicketId() + " invalid");
            return;
        }
        System.out.println("VALIDATED MAKE " + ticket.summary());
    }
}
