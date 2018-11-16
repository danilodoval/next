package com.test.core.engine.matcher;

import com.test.core.driver.Driver;
import com.test.core.passenger.Passenger;
import com.test.core.utils.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * Structure of {@link Matcher}.
 */
public abstract class AbstractMatcher implements Matcher {

    @Override
    public final MatcherResult match(final Set<Driver> drivers, final Set<Passenger> passengers) {

        if (drivers.isEmpty() || passengers.isEmpty()) {
            return MatcherResult.EMPTY;
        }

        final Set<Pair<Driver, Passenger>> matches = doMatch(drivers, passengers);

        final Set<Driver> matchedDrivers = new HashSet<>();
        final Set<Passenger> matchedPassengers = new HashSet<>();

        matches.forEach(selected -> {
            matchedDrivers.add(selected.getFirst());
            matchedPassengers.add(selected.getSecond());
        });

        final Set<Driver> unmatchedDrivers = new HashSet<>(drivers);
        unmatchedDrivers.removeAll(matchedDrivers);

        final Set<Passenger> unmatchedPassengers = new HashSet<>(passengers);
        unmatchedPassengers.removeAll(matchedPassengers);

        MatcherResult.validate(drivers, passengers, matches, unmatchedDrivers, unmatchedPassengers);

        return new MatcherResult(matches, unmatchedDrivers, unmatchedPassengers);
    }

    protected abstract Set<Pair<Driver, Passenger>> doMatch(Set<Driver> drivers, Set<Passenger> passengers);
}