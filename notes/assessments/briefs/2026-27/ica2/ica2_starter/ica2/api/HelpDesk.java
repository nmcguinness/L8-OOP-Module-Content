package ica2.api;

import ica2.model.Agent;
import ica2.model.Status;
import ica2.model.Ticket;

/**
 * The operations every help desk must support.
 *
 * <p>Write one class that implements this interface. It must have a public
 * no-argument constructor.</p>
 *
 * <p>This interface is incomplete. The methods declared below are fixed: you
 * must not change or remove them. The TODO block at the end lists the
 * methods you must add yourself. For those, the name is fixed, but the
 * parameter and return types are your decision, and so is every collection
 * your help desk uses. Justify each choice in your README.</p>
 *
 * <p>Methods marked <b>Core</b> must be implemented. Methods marked
 * <b>Extended</b> are needed for the higher grade bands; the declared one
 * has a default that throws {@link UnsupportedOperationException}, so
 * override it when you implement it. You may also add default, static and
 * private methods.</p>
 *
 * <p>Rules for every method, declared or added:</p>
 * <ul>
 *   <li>no collection a method returns may let the caller change the
 *       desk's own collections (return a copy or an unmodifiable view);</li>
 *   <li>a command changes a ticket only by calling methods of the desk,
 *       never a ticket's setter directly, so that the desk can keep every
 *       collection it holds up to date;</li>
 *   <li>an unknown ticket id is not an error, and must never cause a
 *       {@code null} to be returned;</li>
 *   <li>a {@code null} argument throws {@link IllegalArgumentException}.
 *       Check for it yourself: {@code Objects.requireNonNull} throws
 *       {@link NullPointerException} instead;</li>
 *   <li>a ticket id is unique across intake, the desk and the archive.</li>
 * </ul>
 */
public interface HelpDesk {

    // ------------------------------------------------------------------
    // Agents and intake
    // ------------------------------------------------------------------

    /**
     * <b>Core.</b> Registers an agent.
     *
     * @return {@code false} if an equal agent is already registered
     */
    boolean addAgent(Agent agent);

    /**
     * <b>Core.</b> Places a new ticket in intake. The ticket is not on the
     * desk until {@link #processIntake(int)} moves it there.
     *
     * @return {@code false}, and the ticket is not placed in intake, if a
     *         ticket with the same id is already in intake, on the desk or
     *         in the archive
     */
    boolean submit(Ticket ticket);

    /**
     * <b>Core.</b> Moves up to {@code n} tickets from intake onto the desk,
     * in the order they were submitted (first in, first out). If intake
     * holds fewer than {@code n} tickets, it moves them all.
     *
     * @return the number of tickets added to the desk
     * @throws IllegalArgumentException if {@code n} is negative
     */
    int processIntake(int n);

    /**
     * <b>Core.</b> Adds one ticket directly to the desk.
     *
     * @return {@code false}, and the ticket is not added, if a ticket with
     *         the same id is already in intake, on the desk or in the archive
     */
    boolean add(Ticket ticket);

    // ------------------------------------------------------------------
    // Changes, undo and redo
    // ------------------------------------------------------------------

    /**
     * <b>Core.</b> Runs a command. If it succeeds, the command is recorded so
     * that it can be undone.
     *
     * <p>Extended constraint: the desk keeps at most the 20 most recent
     * commands for undo; when a 21st is recorded, the oldest is forgotten.
     * Running a new command clears anything that could be redone.</p>
     *
     * @return the result of {@link DeskCommand#execute(HelpDesk)}
     */
    boolean execute(DeskCommand command);

    /**
     * <b>Core.</b> Reverses the most recent command that has not been undone.
     *
     * @return {@code false} if there is nothing to undo
     */
    boolean undo();

    /**
     * <b>Extended.</b> Re-applies the most recently undone command.
     *
     * @return {@code false} if there is nothing to redo
     */
    default boolean redo() {
        throw new UnsupportedOperationException("redo not implemented");
    }

    /**
     * <b>Core.</b> Reports whether the workflow allows a ticket to move
     * from one status to another. You design the workflow, and your status
     * change command must refuse a move this method rejects.
     */
    boolean canTransition(Status from, Status to);

    // ------------------------------------------------------------------
    // Workflow
    // ------------------------------------------------------------------

    /**
     * <b>Core.</b> For every finished incident (status {@code DONE}) that
     * does not yet have a follow-up, adds a new feature request immediately
     * after that incident in desk order. The follow-up has a new id, and
     * the same priority, tags and posting date as the incident. It refers
     * to the incident's id, which is how the desk knows that the incident
     * already has a follow-up.
     *
     * @return the number of follow-ups added
     */
    int insertFollowUps();

    /**
     * <b>Core.</b> Removes every finished ticket (status {@code DONE}) from
     * the desk and keeps it in an archive. Archived tickets no longer appear
     * in any query except {@code timeline}.
     *
     * <p>Archiving cannot be undone, and it clears every command that could
     * be undone or redone, so that no command can change an archived
     * ticket.</p>
     *
     * @return the number of tickets archived
     */
    int archiveDone();

    // ------------------------------------------------------------------
    // Totals
    // ------------------------------------------------------------------

    /**
     * <b>Core.</b> Returns the total effort of every ticket on the desk.
     *
     * <p>Constraint: no {@code instanceof} checks or casts.</p>
     */
    int totalEffort();

    /**
     * <b>Core.</b> Reports every escalatable ticket that needs a manager,
     * one per line.
     */
    String escalationReport();

    // ------------------------------------------------------------------
    // TODO - add these methods to this interface, then implement them.
    //
    // Each name is fixed. The parameter and return types are your choice:
    // pick whatever best fits what the method does, and justify it in your
    // README. "Desk order" is the order of the tickets on the desk. A new
    // ticket goes at the end, except a follow-up from insertFollowUps,
    // which goes immediately after its incident.
    //
    // Agents and intake
    //   [Core]     add            An overload of add(Ticket) that adds
    //                             several tickets at once and reports how
    //                             many were accepted.
    //   [Core]     findById       Finds the ticket with a given id. The
    //                             caller must be able to tell when there is
    //                             no such ticket without receiving null.
    //                             Constraint: must not examine every ticket
    //                             on the desk.
    //   [Core]     findAll        Returns every ticket that matches a
    //                             TicketFilter, in desk order.
    //   [Extended] findAll        An overload that also takes an ordering
    //                             and returns the matches in that order.
    //
    // Workflow
    //   [Extended] timeline       Every ticket, on the desk and archived,
    //                             oldest posting date first.
    //                             Constraint: must not sort the combined
    //                             result. Combine two sequences that are
    //                             already in date order, in a single pass.
    //                             Desk order is not date order, so the desk
    //                             must also keep its tickets in date order,
    //                             and so must the archive.
    //   [Extended] partitionByAssignment
    //                             Splits the desk into assigned and
    //                             unassigned tickets, returned together.
    //                             Constraint: one pass over the desk; each
    //                             group keeps desk order.
    //
    // Queries
    //   [Core]     countByStatus  The number of tickets in each status.
    //                             Every status is included, even with a
    //                             count of zero.
    //   [Core]     allTags        Every distinct tag on the desk, in
    //                             alphabetical order.
    //   [Core]     groupByTag     The tickets that carry each tag. A ticket
    //                             with three tags appears in three groups.
    //   [Core]     postedBetween  The tickets posted between two calendar
    //                             days, both days inclusive, oldest first.
    //   [Core]     nextForTriage  The n most urgent open, unassigned
    //                             tickets: highest priority first, then
    //                             oldest first, then by id. The desk's own
    //                             order is unchanged.
    //   [Core]     bestAgentFor   For a given ticket id, the agent whose
    //                             skills overlap most with the ticket's
    //                             tags, ties broken by name (A to Z). An
    //                             agent with no matching skill is not a
    //                             match, so there may be no such agent.
    //   [Extended] compareTags    For two ticket ids, the tags they share,
    //                             the tags only the first has, the tags
    //                             only the second has, and all their tags
    //                             combined, returned together. Neither
    //                             ticket is changed.
    //   [Extended] workloadByAgent
    //                             The total effort of each agent's tickets
    //                             that are not finished. Agents with no such
    //                             tickets are left out.
    //   [Extended] oldestOpen     The open ticket posted earliest, if any.
    //                             Constraint: must not examine every ticket
    //                             on the desk.
    //   [Extended] effortRanking  Every ticket on the desk, highest effort
    //                             first. Tickets with equal effort must all
    //                             be present.
    // ------------------------------------------------------------------
}
