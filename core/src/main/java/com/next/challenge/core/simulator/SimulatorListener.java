package com.next.challenge.core.simulator;

import com.next.challenge.core.driver.Driver;
import com.next.challenge.core.passenger.Passenger;

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
