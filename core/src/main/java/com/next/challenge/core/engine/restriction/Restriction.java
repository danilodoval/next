package com.next.challenge.core.engine.restriction;

import com.next.challenge.core.passenger.Passenger;
import com.next.challenge.core.driver.Driver;

/**
 * Implements of this interface impose restrictions on the transport
 * of a {@link Passenger} with {@link Driver}.
 */
public interface Restriction {

    /**
     * Determines whether the specified {@link Driver} is feasible
     * to carry the specified {@link Passenger}.
     */
    boolean isFeasible(Driver driver, Passenger passenger);

    /**
     * Standard implementations of {@link Restriction}s
     */
    enum StandardRestriction implements Restriction {

        /**
         * Only {@link Driver}s {@link Driver.Status#EMPTY} is feasible
         */
        DRIVER_STATUS {
            @Override
            public boolean isFeasible(final Driver driver, final Passenger passenger) {
                return driver.getStatus() == Driver.Status.EMPTY;
            }
        },

        /**
         * Only {@link Passenger}s {@link Passenger.Status#NO_DRIVER} are feasible
         */
        PASSENGER_STATUS {
            @Override
            public boolean isFeasible(final Driver driver, final Passenger passenger) {
                return passenger.getStatus() == Passenger.Status.NO_DRIVER;
            }
        }
    }
}
