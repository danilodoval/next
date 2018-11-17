package com.next.challenge.core.driver;

import com.next.challenge.core.common.Discoverable;
import com.next.challenge.core.passenger.Passenger;
import com.next.challenge.core.utils.Pair;
import com.next.challenge.core.common.Position;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a driver
 */
public interface Driver extends Discoverable {

    String getIdentifier();

    /**
     * Returns the current {@link Status} of {@link Driver}. It will never be null.
     */
    Status getStatus();

    /**
     Returns the {@link Passenger} that requested {@link Driver} or {@link Passenger} currently on the road.
     Null indicates not {@link Passenger} s linked to {@link Driver}
     */
    Passenger getPassenger();

    /**
     * Called when this {@link Driver} accepts a {@link Passenger}
     *
     * @param passenger
     * @throws UnfeasibleOperationException if the driver is unable to accept {@link Passenger}
     */
    void accept(Passenger passenger) throws UnfeasibleOperationException;

    /**
     * Invoked when {@link Driver} hits {@link Passenger}.
     *
     * @throws UnfeasibleOperationException If there is no {@link Passenger} linked to {@link Driver}
     *                                      or if {@link Driver#getPosition()} is different from
     *                                      {@link Passenger#getPosition()}
     */
    void findPassenger() throws UnfeasibleOperationException;

    /**
     * Called when {@link Driver} reaches {@link Passenger}.
     *
     * @throws UnfeasibleOperationException If there is no {@link Passenger} linked to {@link Driver}
     *                                      or if {@link Driver#getPosition()} is different from
     *                                      {@link Passenger#getDestination()}
     */
    void reachesDestination() throws UnfeasibleOperationException;

    /**
     * Updates the current {@link Position} of {@link Driver}.
     * It may also update the {@link Position} of a {@link Passenger}.
     */
    void updatePosition();

    /**
     * Represents the possible states of a {@link Driver}
     */
    enum Status {

        /**
         * {@link Driver} is empty. Initial status of the state machine.
         */
        EMPTY(true, true),

        /**
         * {@link Driver} is on the way to a {@link Passenger}
         */
        ON_THE_WAY(false, false),

        /**
         * {@link Driver} is busy transporting {@link Passenger} to your destination
         */
        BUSY(false, false);

        private static final Set<Pair<Status, Status>> validTransitions;

        /**
         * Starts the valid state machine state transitions.
         */
        static {
            final Set<Pair<Status, Status>> validTransitionsTemp = new HashSet<>();
            validTransitionsTemp.add(Pair.newPair(EMPTY, ON_THE_WAY));
            validTransitionsTemp.add(Pair.newPair(ON_THE_WAY, BUSY));
            validTransitionsTemp.add(Pair.newPair(BUSY, EMPTY));

            validTransitions = Collections.unmodifiableSet(validTransitionsTemp);
        }

        private final boolean initialState;
        private final boolean acceptPassenger;

        Status(final boolean initialState, final boolean acceptPassenger) {
            this.initialState = initialState;
            this.acceptPassenger = acceptPassenger;
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

        public boolean acceptPassenger() {
            return acceptPassenger;
        }
    }

    /**
     * Exception used to indicate the impossibility of an operation.
     */
    class UnfeasibleOperationException extends Exception {

        private static final long serialVersionUID = -794175677552184441L;

        public UnfeasibleOperationException(final String message) {
            super(message);
        }

        public UnfeasibleOperationException(final String message, final Throwable throwable) {
            super(message, throwable);
        }
    }
}
