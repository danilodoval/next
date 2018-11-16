package com.test.core.engine.restriction;

import com.test.core.driver.Driver;
import com.test.core.engine.calculators.PositionCalculator;
import com.test.core.engine.calculators.PositionCalculator.UnfeasiblePathException;
import com.test.core.passenger.Passenger;

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
