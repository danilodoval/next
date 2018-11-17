package com.next.challenge.core.engine.calculators;

import com.next.challenge.core.common.SimulatorMap;
import com.next.challenge.core.common.Position;

import java.util.Collections;
import java.util.List;

public interface PositionCalculator {

    /**
     * Returns the {@link SimulatorMap} to which {@link PositionCalculator} is subject.
     */
    SimulatorMap getSimulatorMap();

    /**
     * Indicate the {@link Position}s sequence to exit the source {@link Position}
     * and reach the destination {@link Position}.
     * The result must contain as the first origin element and as the last destination element.
     *
     * @param origin
     * @param destination
     * @throws UnfeasiblePathException if it is impossible to draw a path between {@link Position} s indicated
     */
    List<? extends Position> path(Position origin, Position destination) throws UnfeasiblePathException;

    /**
     * Indicates a random @link Position adjacent to {@link Position} specified.
     *
     * @param currentPosition
     * @param lastPositions   Indicates the last {@link Position}s traversed.
     *                        Since the first elements are the oldest.
     */
    Position randomPosition(Position currentPosition, List<Position> lastPositions);

    /**
     * Indicates the impossibility of finding a path. Among the reasons for this impossibility we can cite.
     * The lack of a possible path due to {@link SimulatorMap#getBloquedPositions()}
     * The inability of the search algorithm to find a possible path in an acceptable time.
     */
    class UnfeasiblePathException extends Exception {

        private static final long serialVersionUID = -8862476740899139436L;

        private final List<? extends Position> partialPositions;

        public UnfeasiblePathException(final String message, final List<? extends Position> partialPositions) {
            super(message);
            this.partialPositions = Collections.unmodifiableList(partialPositions);
        }

        public UnfeasiblePathException(final String message, final Throwable throwable) {
            this(message, throwable, Collections.emptyList());
        }

        public UnfeasiblePathException(final String message, final Throwable throwable,
                                       final List<? extends Position> partialPositions) {
            super(message, throwable);
            this.partialPositions = Collections.unmodifiableList(partialPositions);
        }

        /**
         * Indicates the partial path found before the exception was thrown.
         */
        public List<? extends Position> getPartialPositions() {
            return partialPositions;
        }
    }
}
