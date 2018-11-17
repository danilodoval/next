package com.challenge.next.core.common;

import java.util.Collection;

/**
 * It represents a map and its restrictions.
 */
public interface SimulatorMap {

    boolean containsPosition(Position position);

    Collection<? extends Position> getBloquedPositions();

    /**
     * Determines whether the {@link Position} specified is valid,
     * is contained in {@link SimulatorMap} and is not blocked.
     *
     * @param position
     * @return
     */
    boolean isValid(Position position);

}