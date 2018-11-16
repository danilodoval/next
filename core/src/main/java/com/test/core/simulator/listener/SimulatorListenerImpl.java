package com.test.core.simulator.listener;

import com.test.core.common.SimulatorMap2D;
import com.test.core.driver.Driver;
import com.test.core.passenger.Passenger;
import com.test.core.simulator.SimulatorListener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SimulatorListenerImpl implements SimulatorListener {

    private static final long serialVersionUID = -2602902438192375955L;
    private final SimulatorMap2D simulatorMap2D;
    private Set<Driver> drivers;
    private Set<Passenger> passengers;

    public SimulatorListenerImpl(final SimulatorMap2D simulatorMap2D) {
        this.simulatorMap2D = Objects.requireNonNull(simulatorMap2D);
    }
    @Override
    public void turnCompleted(final Set<Driver> drivers, final Set<Passenger> passengers) {
        this.drivers = new HashSet<>(drivers);
        this.passengers = new HashSet<>(passengers);
    }
}
