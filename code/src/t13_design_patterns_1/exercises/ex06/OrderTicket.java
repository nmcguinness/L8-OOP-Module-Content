package t13_design_patterns_1.exercises.ex06;

/**
 * One café order, waiting to be made.
 */
public class OrderTicket {

    private final int ticketId;
    private final String description;
    private final double totalEuro;
    private final int itemCount;

    public OrderTicket(int ticketId, String description, double totalEuro, int itemCount) {
        this.ticketId = ticketId;
        this.description = description;
        this.totalEuro = totalEuro;
        this.itemCount = itemCount;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getDescription() {
        return description;
    }

    public double getTotalEuro() {
        return totalEuro;
    }

    public int getItemCount() {
        return itemCount;
    }

    public boolean isValid() {
        return description != null && !description.isBlank() && totalEuro > 0 && itemCount > 0;
    }

    // The text every policy prints, for example "201 1 cappuccino (€7.80)"
    public String summary() {
        return String.format("%d %s (€%.2f)", ticketId, description, totalEuro);
    }
}
