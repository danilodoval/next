package com.test.core.engine.matcher;

import com.test.core.driver.Driver;
import com.test.core.passenger.Passenger;
import com.test.core.utils.Pair;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements of this interface are responsible for {@link Driver}s match and {@link Passenger}.
 */
public interface Matcher {

    /**
     * Match {@link Driver}s and {@link Passenger}s
     *
     * @param drivers
     * @param passengers
     * @throws IllegalArgumentException If {@link Driver}s or {@link Passenger}s specified
     *                                  are not in their initial state
     */
    MatcherResult match(Set<Driver> drivers, Set<Passenger> passengers);

    class MatcherResult {

        public static MatcherResult EMPTY = new MatcherResult(Collections.emptySet(), Collections.emptySet(),
                Collections.emptySet());
        private final Set<Pair<Driver, Passenger>> matches;
        private final Set<Driver> unmatchedDrivers;
        private final Set<Passenger> unmatchedPassengers;

        public MatcherResult(Set<Pair<Driver, Passenger>> matches, Set<Driver> unmatchedDrivers,
                             Set<Passenger> unmatchedPassengers) {
            this.matches = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(matches)));
            this.unmatchedDrivers = Collections.unmodifiableSet(Objects.requireNonNull(unmatchedDrivers));
            this.unmatchedPassengers = Collections.unmodifiableSet(Objects.requireNonNull(unmatchedPassengers));
        }

        /**
         * Utility method to validate the result. The invariants are:
         * All initial {@link Driver}s must be present in the result and only these.
         * All {@link Passenger}s must be present in the result and only these.
         * A {@link Driver} can not be both matched and unmatched
         * A {@link Passenger} can not be matched and unmatched at the same time
         *
         * @param initialDrivers      {@link Driver}s initials.
         * @param initialPassengers   {@link Passenger}s initials
         * @param matches             matches made
         * @param unmatchedPassengers {@link Driver}s without {@link Passenger}
         * @param unmatchedPassengers {@link Passenger} without {@link Driver}s
         */
        public static void validate(Set<Driver> initialDrivers, Set<Passenger> initialPassengers,
                                    Set<Pair<Driver, Passenger>> matches, Set<Driver> unmatchedDrivers, Set<Passenger> unmatchedPassengers) {
            final Set<Driver> matchesDrivers = matches.stream().map(Pair::getFirst).collect(Collectors.toSet());
            final Set<Passenger> matchesPassengers = matches.stream().map(Pair::getSecond).collect(Collectors.toSet());

            if (!Collections.disjoint(matchesDrivers, unmatchedPassengers)) {
                throw new IllegalStateException("Drivers cannot be match and unmatched in same time.");
            }

            if (!Collections.disjoint(matchesPassengers, unmatchedPassengers)) {
                throw new IllegalStateException("Passengers cannot be match and unmatched in same time.");
            }

            final Set<Driver> allDrivers = new HashSet<>(matchesDrivers);
            allDrivers.addAll(unmatchedDrivers);
            if (!initialDrivers.equals(allDrivers)) {
                throw new IllegalStateException("All drivers must be returned.");
            }

            final Set<Passenger> allPassengers = new HashSet<>(matchesPassengers);
            allPassengers.addAll(unmatchedPassengers);
            if (!initialPassengers.equals(allPassengers)) {
                throw new IllegalStateException("All drivers must be returned.");
            }
        }

        public Set<Pair<Driver, Passenger>> getMatches() {
            return matches;
        }

        public Set<Driver> getUnmatchedDrivers() {
            return unmatchedDrivers;
        }

        public Set<Passenger> getUnmatchedPassengers() {
            return unmatchedPassengers;
        }
    }
}
