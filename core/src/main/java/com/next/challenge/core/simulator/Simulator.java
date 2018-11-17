package com.next.challenge.core.simulator;

import com.next.challenge.core.CoreApplication;
import com.next.challenge.core.utils.Drivers;
import com.next.challenge.core.utils.Passengers;
import com.next.challenge.core.driver.Driver;
import com.next.challenge.core.driver.Driver.Status;
import com.next.challenge.core.driver.Driver.UnfeasibleOperationException;
import com.next.challenge.core.engine.matcher.Matcher;
import com.next.challenge.core.passenger.Passenger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Simulator {

    private static final Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    /**
     * Time between execution between shifts.
     */
    //ToDo use environment variable
    private static long TIME_BETWEEN_TURN = 10;

    /**
     * Number of cycles to remove {@link Passenger}s in the final state
     */
    //ToDo use environment variable
    private static long CYCLES_TO_REMOVE_PASSENGERS = 5000;
    private final Object lock = new Object();
    private final Matcher matcher;
    private final SimulatorListener listener;
    private final Set<Driver> drivers = new HashSet<>();
    private final Set<Passenger> passengers = new HashSet<>();
    private long cycles;

    public Simulator(final Matcher matcher, final SimulatorListener listener) {
        this.matcher = Objects.requireNonNull(matcher);
        this.listener = Objects.requireNonNull(listener);

        final Thread threadMatcher = new Thread(new MatcherTask(), "Matcher");
        threadMatcher.setDaemon(true);
        threadMatcher.start();

        final Thread threadMove = new Thread(new MoveTask(), "Move");
        threadMove.setDaemon(true);
        threadMove.start();
    }

    /**
     * Add a new {@link Driver} to simulation.
     * Only {@link Driver}s in the initial state are allowed.
     *
     * @param driver
     */
    public void add(final Driver driver) {
        Objects.requireNonNull(driver);
        if (!driver.getStatus().isInitialState()) {
            throw new IllegalArgumentException("Only driver in initial state are alloweds.");
        }
        logger.info("Driver added: " + driver);
        synchronized (lock) {
            drivers.add(driver);
        }
    }

    /**
     * Add a new {@link Passenger} to simulation.
     * Only {@link Passenger}s in the initial state are allowed.
     *
     * @param passenger
     */
    public void add(final Passenger passenger) {
        Objects.requireNonNull(passenger);
        if (!passenger.getStatus().isInitialState()) {
            throw new IllegalArgumentException("Only driver in initial state are alloweds.");
        }
        logger.info("Passenger added: " + passenger);
        synchronized (lock) {
            passengers.add(passenger);
        }
    }

    /**
     * Responsible task for joining {@link Driver}s and {@link Passenger}s
     */
    private class MatcherTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                final Matcher.MatcherResult matcherResult;
                final Set<Driver> emptyDrivers;
                final Set<Passenger> noDriverPassengers;
                synchronized (lock) {
                    emptyDrivers = Drivers.filter(drivers, Driver.Status.EMPTY);
                    noDriverPassengers = Passengers.filter(passengers, Passenger.Status.NO_DRIVER);
                }
                matcherResult = matcher.match(emptyDrivers, noDriverPassengers);
                matcherResult.getMatches().forEach(match -> {
                    try {
                        match.getFirst().accept(match.getSecond());
                    } catch (final UnfeasibleOperationException e) {
                        logger.warn(e.getMessage());
                    }
                });
                randomMove();
                try {
                    Thread.sleep(TIME_BETWEEN_TURN / 3);
                } catch (final InterruptedException e) {
                    logger.warn(e.getMessage());
                }
            }
        }

        /**
         * Randomly moves {@link Driver}s
         */
        private void randomMove() {
            Drivers.filter(drivers, Status.EMPTY).forEach(Driver::updatePosition);
        }

    }

    /**
     * Task responsible for moving {@link Driver}s
     */
    private class MoveTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                processNextTurn();
                listener.turnCompleted(Collections.unmodifiableSet(drivers), Collections.unmodifiableSet(passengers));
                try {
                    Thread.sleep(TIME_BETWEEN_TURN);
                } catch (final InterruptedException e) {
                    logger.warn(e.getMessage());
                }
            }
        }

        /**
         * Called to process the next turn.
         */
        private void processNextTurn() {
            moveDriver(Status.ON_THE_WAY);
            moveDriver(Status.BUSY);
            cycles++;
            if (cycles == CYCLES_TO_REMOVE_PASSENGERS) {
                cycles = 0;
                removeFinalStatePassengers();
            }
        }

        /**
         * Move {@link Driver}s to {@link Passenger#getPosition()}
         *
         * @param status If {@link Driver.Status#ON_THE_WAY} moves {@link Driver}s toward {@link Passenger#getPosition()}
         *               If {@link Driver.Status#BUSY} moves {@link Driver}s toward {@link Passenger#getDestination()}
         */
        private void moveDriver(Status status) {
            final Set<Driver> onTheWayOrBusyDrivers;
            synchronized (lock) {
                onTheWayOrBusyDrivers = Drivers.filter(drivers, status);
            }

            onTheWayOrBusyDrivers.forEach(onTheWayOrBusyDriver -> {
                onTheWayOrBusyDriver.updatePosition();
                try {
                    if (status == Status.ON_THE_WAY) {
                        if (onTheWayOrBusyDriver.getPosition().equals(onTheWayOrBusyDriver.getPassenger().getPosition()))
                            onTheWayOrBusyDriver.findPassenger();
                    } else {
                        if (onTheWayOrBusyDriver.getPosition().equals(onTheWayOrBusyDriver.getPassenger().getDestination()))
                            onTheWayOrBusyDriver.reachesDestination();
                    }
                } catch (final UnfeasibleOperationException e) {
                    logger.warn(e.getMessage());
                }
            });
        }

        /**
         * Remove {@link Passenger}s that are in the final state.
         */
        private void removeFinalStatePassengers() {
            synchronized (lock) {
                passengers.removeIf(passenger -> passenger.getStatus() == Passenger.Status.ARRIVE_DESTINATION);
            }
        }
    }
}
