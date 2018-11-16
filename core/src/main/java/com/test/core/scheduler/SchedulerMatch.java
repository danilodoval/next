package com.test.core.scheduler;

import com.test.core.CoreApplication;
import com.test.core.amqp.*;
import com.test.core.common.Position2D;
import com.test.core.common.SimulatorMap2D;
import com.test.core.driver.Driver;
import com.test.core.driver.DriverImpl;
import com.test.core.engine.calculators.ChainedPositionCalculator;
import com.test.core.engine.calculators.PathFinderPositionCalculator;
import com.test.core.engine.calculators.PositionCalculator;
import com.test.core.engine.calculators.PositionCalculator2DImpl;
import com.test.core.engine.matcher.Matcher;
import com.test.core.engine.matcher.MatcherImpl;
import com.test.core.engine.restriction.ChainedRestriction;
import com.test.core.engine.restriction.PathRestriction;
import com.test.core.engine.restriction.Restriction;
import com.test.core.engine.restriction.Restriction.StandardRestriction;
import com.test.core.engine.selector.Selector;
import com.test.core.passenger.Passenger;
import com.test.core.passenger.PassengerImpl;
import com.test.core.utils.Drivers;
import com.test.core.utils.Passengers;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class SchedulerMatch implements Job {

    @Autowired
    private TopicExchange passengerExchange;

    @Autowired
    private TopicExchange driverExchange;

    @Autowired
    AmqpDriverProducer driverProducer;

    @Autowired
    AmqpPassengerProducer passengerProducer;

    private static final Logger logger = LoggerFactory.getLogger(CoreApplication.class);
    private static final int MAX_X = 300;
    private static final int MAX_Y = 300;

    private static final int NUMBER_OF_BLOCKED_HORIZONTAL_LINES = 3;
    private static final int NUMBER_OF_BLOCKED_VERTICAL_LINES = 3;
    private static final int MAX_BLOCKED_SIZE_LINE = 500;

    private static final Random random = new Random(100);

    private static final SimulatorMap2D simulatorMap2D = new SimulatorMap2D(Position2D.ORIGIN,
            Position2D.getInstance(MAX_X, MAX_Y), randomBlockedPositions(random));

    private static final Selector selector = Selector.StandardSelector.MINIMIZE_DISTANCE;


    final PositionCalculator positionCalculator;
    {
        final PositionCalculator positionCalculator2D = new PositionCalculator2DImpl(simulatorMap2D);
        positionCalculator = new ChainedPositionCalculator(positionCalculator2D,
                new PathFinderPositionCalculator(simulatorMap2D));
    }

    final Restriction restriction = new ChainedRestriction(Arrays.asList(StandardRestriction.DRIVER_STATUS,
            StandardRestriction.PASSENGER_STATUS, new PathRestriction(positionCalculator)));

    private Collection<Driver> drivers = new ArrayList<>();
    private Collection<Passenger> passengers = new ArrayList<>();

    public static List<Position2D> randomBlockedPositions(final Random random) {
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
        try {
            final Driver driver = new DriverImpl(positionCalculator, Position2D.getInstance(originX, originY));

            this.drivers.add(driver);

            return driver.getIdentifier();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return "";
        }
    }

    public String addPassenger(int originX, int originY, int destX, int destY) {
        try {
            final Position2D origin = Position2D.getInstance(originX, originY);
            final Position2D destination = Position2D.getInstance(destX, destY);
            final Passenger passenger = new PassengerImpl(positionCalculator, origin, destination);

            this.passengers.add(passenger);

            return passenger.getIdentifier();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return "";
        }
    }

    @Override
    public void execute(JobExecutionContext arg0) {
        try {
            System.out.println("Validando dados duplicados no banco. At " + new Date());
            System.out.println("Deletando registros com mais de 10 dias sem uso. At " + new Date());

            if (!drivers.isEmpty() && !passengers.isEmpty()) {

                final Matcher matcher = new MatcherImpl(restriction, selector);

                final Matcher.MatcherResult matcherResult;

                final Set<Driver> emptyDrivers;
                final Set<Passenger> noDriverPassengers;

                emptyDrivers = Drivers.filter(drivers, Driver.Status.EMPTY);
                noDriverPassengers = Passengers.filter(passengers, Passenger.Status.NO_DRIVER);

                matcherResult = matcher.match(emptyDrivers, noDriverPassengers);
                matcherResult.getMatches().forEach(match -> {
                    try {
                        removeDriver(match.getFirst());
                        removePassenger(match.getSecond());
                        sendMessageForDriverAccepted(match.getFirst().getIdentifier(), match.getSecond().getIdentifier());
                        sendMessageForPassengerAccepted(match.getSecond().getIdentifier(), match.getFirst().getIdentifier());
                        match.getFirst().accept(match.getSecond());
                    } catch (final Driver.UnfeasibleOperationException e) {
                        logger.warn(e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void removeDriver(Driver driver) {
        try {
            drivers.remove(driver);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    private void removePassenger(Passenger passenger) {
        try {
            passengers.remove(passenger);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    private void sendMessageForDriverAccepted(String driver, String passenger) {
        Object payload = "Driver: " + driver + ", Passenger: " + passenger;
        AmqpEventContext context = new AmqpEventContext(AmqpEventType.DRIVER_ACCEPTED.getName(), payload);
        driverProducer.send(driverExchange.getName(), context, payload, AmqpEventType.DRIVER_ACCEPTED);
    }

    private void sendMessageForPassengerAccepted(String passenger, String driver) {
        Object payload = "Passenger: " + passenger + ", Driver: " + driver;
        AmqpEventContext context = new AmqpEventContext(AmqpEventType.PASSENGER_ACCEPTED.getName(), payload);
        driverProducer.send(passengerExchange.getName(), context, payload, AmqpEventType.PASSENGER_ACCEPTED);
    }
}
