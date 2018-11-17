package com.next.challenge.core.engine.selector;

import com.next.challenge.core.passenger.Passenger;
import com.next.challenge.core.utils.Pair;
import com.next.challenge.core.driver.Driver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Implementations of this interface use some criteria to determine
 * which are the best {@link Driver} and {@link Passenger} pairs to be effective.
 */
public interface Selector {

    /**
     * Given the set of possible matches, select the best matches.
     *
     * @param possiblePassengerByDriver
     */
    Set<Pair<Driver, Passenger>> select(Map<Driver, Set<Passenger>> possiblePassengerByDriver);

    /**
     * Standard implementations of {@link Selector}
     */
    enum StandardSelector implements Selector {

        /**
         * Selects the pairs with the shortest distance
         */
        MINIMIZE_DISTANCE {
            @Override
            public Set<Pair<Driver, Passenger>> select(final Map<Driver, Set<Passenger>> possiblePassengerByDriver) {

                final Map<Long, Set<Pair<Driver, Passenger>>> pairsByDistance = new TreeMap<>();
                possiblePassengerByDriver.forEach((driver, value) -> value.forEach(passenger -> {
                    final Pair<Driver, Passenger> pair = Pair.newPair(driver, passenger);
                    final Long distance = (long) driver.getPosition().distanceTo(passenger.getPosition()) * 100;
                    Set<Pair<Driver, Passenger>> pairsWithSameDistance = pairsByDistance.computeIfAbsent(distance, k -> new HashSet<>());
                    pairsWithSameDistance.add(pair);
                }));

                final Set<Driver> selectedDrivers = new HashSet<>();
                final Set<Passenger> selectedPassengers = new HashSet<>();

                final Set<Pair<Driver, Passenger>> result = new HashSet<>();
                pairsByDistance.keySet().stream().map(pairsByDistance::get).forEach(pairsWithSameDistance -> pairsWithSameDistance.forEach(pair -> {
                    final Driver driver = pair.getFirst();
                    final Passenger passenger = pair.getSecond();
                    if (!selectedDrivers.contains(driver) && !selectedPassengers.contains(passenger)) {
                        result.add(pair);
                        selectedDrivers.add(driver);
                        selectedPassengers.add(passenger);
                    }
                }));

                return result;
            }
        }
    }
}
