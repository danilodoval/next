package com.challenge.next.core.common;

import com.challenge.next.core.engine.calculators.PositionCalculator;

import java.util.Objects;

public abstract class AbstractDiscoverable implements Discoverable {

    protected final PositionCalculator positionCalculator;

    private Position position;

    public AbstractDiscoverable(final PositionCalculator positionCalculator, final Position position) {
        this.positionCalculator = Objects.requireNonNull(positionCalculator);
        setPosition(position);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(final Position position) {
        this.position = Objects.requireNonNull(position);
    }

    @Override
    public String toString() {
        return "position: " + position;
    }
}
