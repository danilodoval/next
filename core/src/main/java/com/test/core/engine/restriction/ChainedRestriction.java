package com.test.core.engine.restriction;

import com.test.core.driver.Driver;
import com.test.core.passenger.Passenger;

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
