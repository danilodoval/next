package com.next.challenge.core.engine.restriction;

import com.next.challenge.core.passenger.Passenger;
import com.next.challenge.core.driver.Driver;

public class MaxDistanceRestriction implements Restriction {

    private final int maxDistance;

    public MaxDistanceRestriction(final int maxDistance) {
        if (maxDistance <= 0) {
            throw new IllegalArgumentException("Max distance cannot be negative or zero.");
        }
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean isFeasible(final Driver driver, final Passenger passenger) {
        final double distance = driver.getPosition().distanceTo(passenger.getPosition());
        return distance < maxDistance;
    }
}
