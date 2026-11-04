package t13_design_patterns_1.exercises.ex06;

/**
 * Wraps a ticket and its policy into one piece of work that can wait in a queue.
 */
public class ProcessTicketCommand implements Command {

    private final OrderTicket ticket;
    private final OrderProcessingStrategy strategy;

    public ProcessTicketCommand(OrderTicket ticket, OrderProcessingStrategy strategy) {
        this.ticket = ticket;
        this.strategy = strategy;
    }

    @Override
    public void execute() {
        strategy.process(ticket);
    }
}
