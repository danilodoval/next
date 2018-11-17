package com.next.challenge.core.engine.restriction;

import com.next.challenge.core.passenger.Passenger;
import com.next.challenge.core.driver.Driver;

import java.util.*;
import java.util.concurrent.*;

/**
 * Provides utilities related to {@link Restriction}
 */
public class Restrictions {

    private static final ThreadPoolExecutor executor;
    private static int threadCount;

    static {
        executor = new ThreadPoolExecutor(50, 100, 1, TimeUnit.HOURS,
                new ArrayBlockingQueue<>(50000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(final Runnable target) {
                        final Thread result = new Thread(target, "Restrictions " + threadCount++);
                        result.setDaemon(true);
                        return result;
                    }
                });
    }

    /**
     * This class is not intended to create objects.
     */
    private Restrictions() {
        throw new AssertionError();
    }

    /**
     * Defines which pairs {@link Driver} and {@link Passenger}s are possible,
     * those pairs that do not disrespect {@link Restriction} imposed.
     *
     * @param drivers
     * @param passengers
     * @param restriction
     * @return
     */
    public static Map<Driver, Set<Passenger>> getPossiblesMatches(final Set<Driver> drivers, final Set<Passenger> passengers,
                                                                  final Restriction restriction) {
        final Map<Driver, Set<Passenger>> result = new HashMap<>();
        final List<Future<?>> futures = new ArrayList<>();
        drivers.forEach(driver -> {
            final Set<Passenger> feasiblePassengers = Collections.synchronizedSet(new HashSet<>());
            passengers.stream().map(passenger -> executor.submit(() -> {
                if (restriction.isFeasible(driver, passenger)) {
                    feasiblePassengers.add(passenger);
                }
            })).forEach(futures::add);
            result.put(driver, Collections.unmodifiableSet(feasiblePassengers));
        });
        futures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        return Collections.unmodifiableMap(result);
    }
}
