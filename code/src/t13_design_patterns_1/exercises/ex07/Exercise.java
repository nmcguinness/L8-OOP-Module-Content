package t13_design_patterns_1.exercises.ex07;

import t13_design_patterns_1.exercises.ex06.OrderTicket;
import t13_design_patterns_1.exercises.ex06.ProcessTicketCommand;
import t13_design_patterns_1.exercises.ex06.TicketQueue;

import java.util.List;

public class Exercise {

    public static void run() {
        ProcessingPolicySelector selector = new ProcessingPolicySelector();
        TicketQueue queue = new TicketQueue();

        List<OrderTicket> tickets = List.of(
                new OrderTicket(201, "1 cappuccino, 1 croissant", 7.80, 2),
                new OrderTicket(202, "   ", 5.00, 1),
                new OrderTicket(203, "training: 2 lattes, 1 muffin", 12.50, 3),
                new OrderTicket(204, "8 americanos, 2 toasties", 42.00, 10));

        for (OrderTicket ticket : tickets) {
            queue.add(new ProcessTicketCommand(ticket, selector.select(ticket)));
        }
        queue.processAll();
    }
}
