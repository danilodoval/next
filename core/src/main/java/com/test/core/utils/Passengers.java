package com.test.core.utils;

import com.test.core.passenger.Passenger;
import com.test.core.passenger.Passenger.Status;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Offers utilities related to {@link Passenger}
 */
public class Passengers {

    /**
     * Do not have the purpose of creating objects.
     */
    private Passengers() {
        throw new AssertionError();
    }

    public static <P extends Passenger> Set<P> filter(final Collection<P> passengers, final Status status) {
        return passengers.stream().filter(p -> p.getStatus() == status).collect(Collectors.toSet());
    }
}
