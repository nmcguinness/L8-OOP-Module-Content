package ica2.api;

import ica2.model.Ticket;

/**
 * A test that a ticket either passes or fails.
 *
 * <p>Pass filters to the help desk's {@code findAll} method (which you add
 * to {@link HelpDesk}) as lambdas.
 * You may add default, static and private methods to this interface.
 * You must not change or remove {@link #matches(Ticket)}.</p>
 */
@FunctionalInterface
public interface TicketFilter {

    /**
     * Tests one ticket.
     *
     * @param ticket the ticket to test; never {@code null}
     * @return {@code true} if the ticket passes this filter
     */
    boolean matches(Ticket ticket);
}
