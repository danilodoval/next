package com.challenge.next.core.engine.calculators;

import com.challenge.next.core.utils.Pair;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.challenge.next.core.common.Position;
import com.challenge.next.core.common.Position2D;
import com.challenge.next.core.common.SimulatorMap2D;

import java.util.*;
import java.util.concurrent.ExecutionException;

abstract class AbstractPositionCalculator2D implements PositionCalculator {

    private final Random random = new Random();

    private final CacheLoader<Pair<Position2D, Position2D>, Object> cacheLoader = new CacheLoader<Pair<Position2D, Position2D>, Object>() {
        @Override
        public Object load(final Pair<Position2D, Position2D> key) throws Exception {
            try {
                return Collections.unmodifiableList(doPath(key.getFirst(), key.getSecond()));
            } catch (final UnfeasiblePathException e) {
                return e;
            }
        }
    };

    private final LoadingCache<Pair<Position2D, Position2D>, Object> cache = CacheBuilder.newBuilder().recordStats()
            .concurrencyLevel(100).maximumSize(100000).build(cacheLoader);

    private final SimulatorMap2D simulatorMap2D;

    public AbstractPositionCalculator2D(final SimulatorMap2D simulatorMap2D) {
        this.simulatorMap2D = Objects.requireNonNull(simulatorMap2D);
    }

    abstract List<Position2D> doPath(final Position2D origin, final Position2D destination)
            throws UnfeasiblePathException;

    @Override
    public final SimulatorMap2D getSimulatorMap() {
        return simulatorMap2D;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final List<Position2D> path(final Position origin, final Position destination)
            throws UnfeasiblePathException {
        try {
            final Object result = cache.get(Pair.newPair((Position2D) origin, (Position2D) destination));
            if (result instanceof UnfeasiblePathException) {
                throw (UnfeasiblePathException) result;
            }
            return (List<Position2D>) result;
        } catch (final ExecutionException e) {
            throw new UnfeasiblePathException("Cannot calculate path", e);
        }
    }

    @Override
    public final Position randomPosition(final Position currentPosition, final List<Position> lastPositions) {
        final Position2D currentPosition2D = (Position2D) currentPosition;
        final List<Position2D> validAdjacents = getValidAdjacents(currentPosition2D);
        if (validAdjacents.isEmpty()) {
            return currentPosition;
        }
        final List<Position2D> result = evictLastPositions(validAdjacents, lastPositions);
        return result.get(random.nextInt(result.size()));
    }

    private List<Position2D> getValidAdjacents(final Position2D position2D) {
        final List<Position2D> result = new ArrayList<>(4);
        for (final Position2D adjacent : position2D.getAdjacents()) {
            if (simulatorMap2D.isValid(adjacent)) {
                result.add(adjacent);
            }
        }
        return result;
    }

    /**
     * Try to avoid the last {@link Position} s traversed.
     * If you can not avoid all N {@link Position} s,
     * avoid N-1, if it is not possible, avoid N-2 and so on.
     *
     * @param validAdjacents
     * @param lastPositions
     * @return
     */
    private List<Position2D> evictLastPositions(final List<Position2D> validAdjacents,
                                                final List<Position> lastPositions) {
        if (lastPositions.isEmpty()) {
            return validAdjacents;
        }

        List<Position2D> result;
        int i = 0;
        do {
            result = new ArrayList<>(validAdjacents);
            result.removeAll(lastPositions.subList(i++, lastPositions.size()));
        } while (result.isEmpty());
        return result;
    }

}
