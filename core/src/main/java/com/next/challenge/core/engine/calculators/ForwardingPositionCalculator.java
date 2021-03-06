package com.next.challenge.core.engine.calculators;

import com.next.challenge.core.common.SimulatorMap;
import com.next.challenge.core.common.Position;

import java.util.List;
import java.util.Objects;

public class ForwardingPositionCalculator  implements PositionCalculator {

    private final PositionCalculator positionCalculator;

    public ForwardingPositionCalculator(final PositionCalculator positionCalculator) {
        this.positionCalculator = Objects.requireNonNull(positionCalculator);
    }

    @Override
    public SimulatorMap getSimulatorMap() {
        return positionCalculator.getSimulatorMap();
    }

    @Override
    public List<? extends Position> path(final Position origin, final Position destination)
            throws UnfeasiblePathException {
        return positionCalculator.path(origin, destination);
    }

    @Override
    public Position randomPosition(final Position currentPosition, final List<Position> lastPositions) {
        return positionCalculator.randomPosition(currentPosition, lastPositions);
    }
}
