package com.next.challenge.core.passenger;

import com.next.challenge.core.common.Discoverable;
import com.next.challenge.core.utils.Pair;
import com.next.challenge.core.common.Position;
import com.next.challenge.core.driver.Driver;
import com.next.challenge.core.engine.calculators.PositionCalculator.UnfeasiblePathException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a passenger
 */
public interface Passenger extends Discoverable {

    String getIdentifier();

    /**
     * Return to {@link Passenger} {@link Position}. It will never be null.
     */
    Position getDestination();

    /**
     * Indicates the path between {@link Passenger#getPosition()} and {@link Passenger#getDestination()}
     *
     * @throws UnfeasiblePathException
     */
    List<? extends Position> pathToDestination() throws UnfeasiblePathException;

    /**
     * Returns the current {@link Status} of {@link Passenger}. It will never be null.
     */
    Status getStatus();

    /**
     * Modify {@link Passenger} {@link Status}.
     *
     * @param newStatus
     * @throws {@link IllegalArgumentException} if not a valid transition.
     */
    void changeStatus(Status newStatus);

    /**
     * Represents the possible states of a {@link Passenger}
     */
    enum Status {

        /**
         * None {@link Driver} is on the way to this {@link Passenger}.
         * Initial status of the state machine.
         */
        NO_DRIVER(true),

        /**
         * {@Link Driver} is on the way to {@link Passenger}.
         */
        WAITING_DRIVER(false),

        /**
         * {@link Passenger} is inside a {@link Driver} en route to the destination.
         */
        WITH_DRIVER(false),

        /**
         * {@link Passenger} has reached its destination. State of the state machine.
         */
        ARRIVE_DESTINATION(false);

        private static final Set<Pair<Status, Status>> validTransitions;

        /**
         * Start the valid state machine status transitions.
         */
        static {
            final Set<Pair<Status, Status>> validTransitionsTemp = new HashSet<>();
            validTransitionsTemp.add(Pair.newPair(NO_DRIVER, WAITING_DRIVER));
            validTransitionsTemp.add(Pair.newPair(WAITING_DRIVER, WITH_DRIVER));
            validTransitionsTemp.add(Pair.newPair(WITH_DRIVER, ARRIVE_DESTINATION));

            validTransitions = Collections.unmodifiableSet(validTransitionsTemp);
        }

        private final boolean initialState;

        Status(final boolean initialState) {
            this.initialState = initialState;
        }

        public static void validateTransition(final Status currentStatus, final Status nextStatus) {
            final Pair<Status, Status> transition = Pair.newPair(currentStatus, nextStatus);
            if (!validTransitions.contains(transition)) {
                throw new IllegalArgumentException("Invalid Transition: " + currentStatus + " to " + nextStatus);
            }
        }

        public boolean isInitialState() {
            return initialState;
        }
    }
}
