package t13_design_patterns_1.exercises.ex06;

import java.util.ArrayDeque;
import java.util.Queue;

public class Exercise
{
    public static void run()
    {
        ProcessingPolicySelector selector = new ProcessingPolicySelector();
        TicketQueue queue = new TicketQueue();

        OrderTicket t1 = new OrderTicket(201, "1 cappuccino, 1 croissant", 7.80, 2);
        OrderTicket t2 = new OrderTicket(202, "   ", 5.00, 1);
        OrderTicket t3 = new OrderTicket(203, "training: 2 lattes, 1 muffin", 12.50, 3);
        OrderTicket t4 = new OrderTicket(204, "8 americanos, 2 toasties", 42.00, 10);

        enqueue(queue, selector, t1);
        enqueue(queue, selector, t2);
        enqueue(queue, selector, t3);
        enqueue(queue, selector, t4);

        queue.processAll();
    }

    private static void enqueue(TicketQueue queue, ProcessingPolicySelector selector, OrderTicket ticket)
    {
        OrderProcessingStrategy strategy = selector.select(ticket);
        queue.add(new ProcessTicketCommand(ticket, strategy));
    }
}

interface Command
{
    void execute();
}

class ProcessTicketCommand implements Command
{
    private OrderTicket _ticket;
    private OrderProcessingStrategy _strategy;

    public ProcessTicketCommand(OrderTicket ticket, OrderProcessingStrategy strategy)
    {
        _ticket = ticket;
        _strategy = strategy;
    }

    @Override
    public void execute()
    {
        _strategy.process(_ticket);
    }
}

interface OrderProcessingStrategy
{
    void process(OrderTicket ticket);
}

class ImmediateProcess implements OrderProcessingStrategy
{
    @Override
    public void process(OrderTicket ticket)
    {
        System.out.println("IMMEDIATE MAKE " + ticket.getTicketId() + " " + ticket.getDescription() + " (\u20ac" + ticket.getTotalEuro() + ")");
    }
}

class ValidatedProcess implements OrderProcessingStrategy
{
    @Override
    public void process(OrderTicket ticket)
    {
        if (!ticket.isValid())
        {
            System.out.println("REJECT " + ticket.getTicketId() + " invalid");
            return;
        }

        System.out.println("VALIDATED MAKE " + ticket.getTicketId() + " " + ticket.getDescription() + " (\u20ac" + ticket.getTotalEuro() + ")");
    }
}

class DryRunTraining implements OrderProcessingStrategy
{
    @Override
    public void process(OrderTicket ticket)
    {
        System.out.println("TRAINING " + ticket.getTicketId() + " would make: " + ticket.getDescription() + " (\u20ac" + ticket.getTotalEuro() + ")");
    }
}

class ProcessingPolicySelector
{
    private OrderProcessingStrategy _immediate = new ImmediateProcess();
    private OrderProcessingStrategy _validated = new ValidatedProcess();
    private OrderProcessingStrategy _training = new DryRunTraining();

    public OrderProcessingStrategy select(OrderTicket ticket)
    {
        String desc = ticket.getDescription();

        if (desc != null && desc.toLowerCase().contains("training"))
            return _training;

        if (ticket.getTotalEuro() >= 30.0 || ticket.getItemCount() >= 6)
            return _validated;

        return _immediate;
    }
}

class TicketQueue
{
    private Queue<Command> _queue = new ArrayDeque<>();

    public void add(Command command)
    {
        _queue.add(command);
    }

    public void processAll()
    {
        while (!_queue.isEmpty())
        {
            _queue.poll().execute();
        }
    }
}

class OrderTicket
{
    private int _ticketId;
    private String _description;
    private double _totalEuro;
    private int _itemCount;

    public OrderTicket(int ticketId, String description, double totalEuro, int itemCount)
    {
        _ticketId = ticketId;
        _description = description;
        _totalEuro = totalEuro;
        _itemCount = itemCount;
    }

    public int getTicketId()       { return _ticketId; }
    public String getDescription() { return _description; }
    public double getTotalEuro()   { return _totalEuro; }
    public int getItemCount()      { return _itemCount; }

    public boolean isValid()
    {
        if (_description == null)
            return false;

        if (_totalEuro <= 0)
            return false;

        if (_itemCount <= 0)
            return false;

        return !_description.trim().isEmpty();
    }
}
