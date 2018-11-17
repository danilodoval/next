package com.challenge.next.core.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;

import com.challenge.next.core.CoreApplication;
import com.challenge.next.core.amqp.AmqpDriverProducer;
import com.challenge.next.core.amqp.AmqpEventContext;
import com.challenge.next.core.amqp.AmqpEventType;
import com.challenge.next.core.amqp.AmqpPassengerProducer;
import com.challenge.next.core.utils.Drivers;
import com.challenge.next.core.utils.Passengers;
import com.challenge.next.core.common.Position2D;
import com.challenge.next.core.common.SimulatorMap2D;
import com.challenge.next.core.driver.Driver;
import com.challenge.next.core.driver.DriverImpl;
import com.challenge.next.core.engine.calculators.ChainedPositionCalculator;
import com.challenge.next.core.engine.calculators.PathFinderPositionCalculator;
import com.challenge.next.core.engine.calculators.PositionCalculator;
import com.challenge.next.core.engine.calculators.PositionCalculator2DImpl;
import com.challenge.next.core.engine.matcher.Matcher;
import com.challenge.next.core.engine.matcher.MatcherImpl;
import com.challenge.next.core.engine.restriction.ChainedRestriction;
import com.challenge.next.core.engine.restriction.PathRestriction;
import com.challenge.next.core.engine.restriction.Restriction;
import com.challenge.next.core.engine.restriction.Restriction.StandardRestriction;
import com.challenge.next.core.engine.selector.Selector;
import com.challenge.next.core.passenger.Passenger;
import com.challenge.next.core.passenger.PassengerImpl;

public class SchedulerMatch implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    private static final int MAX_X = 300;                               //ToDo use environment variable
    private static final int MAX_Y = 300;                               //ToDo use environment variable
    private static final int NUMBER_OF_BLOCKED_HORIZONTAL_LINES = 3;    //ToDo use environment variable
    private static final int NUMBER_OF_BLOCKED_VERTICAL_LINES = 3;      //ToDo use environment variable
    private static final int MAX_BLOCKED_SIZE_LINE = 500;               //ToDo use environment variable

    private static final Random random = new Random(100);

    private static final SimulatorMap2D simulatorMap2D = new SimulatorMap2D(Position2D.ORIGIN,
            Position2D.getInstance(MAX_X, MAX_Y), randomBlockedPositions(random));

    private static final Selector selector = Selector.StandardSelector.MINIMIZE_DISTANCE;

    @Autowired
    AmqpDriverProducer driverProducer;

    @Autowired
    AmqpPassengerProducer passengerProducer;

    @Autowired
    private TopicExchange passengerExchange;

    @Autowired
    private TopicExchange driverExchange;

    private Set<Driver> drivers;
    private Set<Passenger> passengers;

    private final PositionCalculator positionCalculator;
    {
        final PositionCalculator positionCalculator2D = new PositionCalculator2DImpl(simulatorMap2D);
        positionCalculator = new ChainedPositionCalculator(positionCalculator2D,
                new PathFinderPositionCalculator(simulatorMap2D));
    }

    private final Restriction restriction = new ChainedRestriction(Arrays.asList(StandardRestriction.DRIVER_STATUS,
            StandardRestriction.PASSENGER_STATUS, new PathRestriction(positionCalculator)));

    public SchedulerMatch() {
    }

    private static List<Position2D> randomBlockedPositions(final Random random) {
        final List<Position2D> result = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_BLOCKED_HORIZONTAL_LINES; i++) {
            result.addAll(Position2D.createLine(Position2D.randomPosition(null, random, MAX_X, MAX_Y),
                    random.nextInt(MAX_BLOCKED_SIZE_LINE), true));
        }

        for (int i = 0; i < NUMBER_OF_BLOCKED_VERTICAL_LINES; i++) {
            result.addAll(Position2D.createLine(Position2D.randomPosition(null, random, MAX_X, MAX_Y),
                    random.nextInt(MAX_BLOCKED_SIZE_LINE), false));
        }

        return result;
    }

    public String addDriver(int originX, int originY) {
        final Driver driver = new DriverImpl(positionCalculator, Position2D.getInstance(originX, originY));
        this.drivers.add(driver);
        return driver.getIdentifier();
    }

    public String addPassenger(int originX, int originY, int destX, int destY) {
        final Position2D origin = Position2D.getInstance(originX, originY);
        final Position2D destination = Position2D.getInstance(destX, destY);
        final Passenger passenger = new PassengerImpl(positionCalculator, origin, destination);
        this.passengers.add(passenger);
        return passenger.getIdentifier();
    }

    @Override
    public void execute(JobExecutionContext arg0) {
        if (!drivers.isEmpty() && !passengers.isEmpty()) {
            final Matcher matcher = new MatcherImpl(restriction, selector);
            final Matcher.MatcherResult matcherResult;

            final Set<Driver> emptyDrivers;
            final Set<Passenger> noDriverPassengers;

            emptyDrivers = Drivers.filter(drivers, Driver.Status.EMPTY);
            noDriverPassengers = Passengers.filter(passengers, Passenger.Status.NO_DRIVER);

            matcherResult = matcher.match(emptyDrivers, noDriverPassengers);
            matcherResult.getMatches().forEach(match -> {
                //ToDo valid accept of the Driver, insert rpc for await response
                removeDriver(match.getFirst());
                removePassenger(match.getSecond());

                sendMessageAccepted(match.getFirst(), match.getSecond(), AmqpEventType.DRIVER_ACCEPTED);
                sendMessageAccepted(match.getFirst(), match.getSecond(), AmqpEventType.PASSENGER_ACCEPTED);

                try {
                    match.getFirst().accept(match.getSecond());
                } catch (Driver.UnfeasibleOperationException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void removeDriver(Driver driver) {
        drivers.remove(driver);
    }

    private void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
    }

    private void sendMessageAccepted(Driver driver, Passenger passenger, AmqpEventType eventType) {
        try {
            Object payload = "Driver: " + driver + ", Passenger: " + passenger;
            AmqpEventContext context = new AmqpEventContext(eventType.getName(), payload);

            switch (eventType) {
                case DRIVER_CREATION:
                    driverProducer.send(driverExchange.getName(), context, payload, eventType);
                    break;
                case PASSENGER_CREATION:
                    passengerProducer.send(passengerExchange.getName(), context, payload, eventType);
                    break;
                default:
                    throw new IllegalStateException("Cannot recognize type: " + eventType);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

}
