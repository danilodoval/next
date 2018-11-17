package com.challenge.next.core.simulator;

import com.challenge.next.core.driver.Driver;
import com.challenge.next.core.passenger.Passenger;

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
