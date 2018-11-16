package com.test.core.simulator;

import com.test.core.driver.Driver;
import com.test.core.passenger.Passenger;

import java.util.Set;

public interface SimulatorListener {

    /**
     * Called after completion of a simulation shift.
     *
     * @param drivers
     * @param passengers
     */
    void turnCompleted(Set<Driver> drivers, Set<Passenger> passengers);
}
