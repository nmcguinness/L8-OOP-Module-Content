package t13_design_patterns_1.exercises.ex06;

public class Exercise {

    public static void run() {
        OrderTicket small = new OrderTicket(201, "1 cappuccino, 1 croissant", 7.80, 2);
        OrderTicket blank = new OrderTicket(202, "   ", 5.00, 1);
        OrderTicket practice = new OrderTicket(203, "2 lattes, 1 muffin", 12.50, 3);
        OrderTicket large = new OrderTicket(204, "8 americanos, 2 toasties", 42.00, 10);

        // In this part you choose each ticket's policy by hand; Exercise 07 automates it
        TicketQueue queue = new TicketQueue();
        queue.add(new ProcessTicketCommand(small, new ImmediateProcess()));
        queue.add(new ProcessTicketCommand(blank, new ValidatedProcess()));
        queue.add(new ProcessTicketCommand(practice, new DryRunTraining()));
        queue.add(new ProcessTicketCommand(large, new ValidatedProcess()));

        System.out.println("Queued: " + queue.size());
        queue.processAll();
        System.out.println("Queued: " + queue.size());
    }
}
