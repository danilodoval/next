package com.challenge.next.core.engine.restriction;

import com.challenge.next.core.passenger.Passenger;
import com.challenge.next.core.driver.Driver;

import java.util.List;

/**
 * Lets you chain multiple {@link Restriction}s
 */
public class ChainedRestriction implements Restriction {

    private final List<Restriction> restricions;

    public ChainedRestriction(final List<Restriction> restricions) {
        if (restricions == null || restricions.isEmpty()) {
            throw new IllegalArgumentException("Restriction cannot be null or empty.");
        }
        this.restricions = restricions;
    }

    @Override
    public boolean isFeasible(final Driver driver, final Passenger passenger) {
        return restricions.stream().allMatch(restriction -> restriction.isFeasible(driver, passenger));
    }
}
