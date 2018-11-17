package com.challenge.next.core.engine.restriction;

import com.challenge.next.core.passenger.Passenger;
import com.challenge.next.core.driver.Driver;
import com.challenge.next.core.engine.calculators.PositionCalculator;
import com.challenge.next.core.engine.calculators.PositionCalculator.UnfeasiblePathException;

public class PathRestriction implements Restriction {

    private final PositionCalculator positionCalculator;

    public PathRestriction(final PositionCalculator positionCalculator) {
        this.positionCalculator = positionCalculator;
    }

    @Override
    public boolean isFeasible(final Driver driver, final Passenger passenger) {
        try {
            return passenger.pathToDestination() != null
                    && positionCalculator.path(driver.getPosition(), passenger.getPosition()) != null;
        } catch (final UnfeasiblePathException e) {
            return false;
        }
    }
}
