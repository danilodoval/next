package com.next.challenge.core.engine.matcher;

import com.next.challenge.core.engine.restriction.Restriction;
import com.next.challenge.core.engine.restriction.Restrictions;
import com.next.challenge.core.engine.selector.Selector;
import com.next.challenge.core.passenger.Passenger;
import com.next.challenge.core.utils.Pair;
import com.next.challenge.core.driver.Driver;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link Matcher} based on {@link Restriction} and {@link Selector}.
 */
public class MatcherImpl extends AbstractMatcher {

    private final Restriction restriction;
    private final Selector selector;

    public MatcherImpl(Restriction restriction, Selector selector) {
        this.restriction = Objects.requireNonNull(restriction);
        this.selector = Objects.requireNonNull(selector);
    }

    @Override
    protected Set<Pair<Driver, Passenger>> doMatch(Set<Driver> drivers, Set<Passenger> passengers) {
        final Map<Driver, Set<Passenger>> possiblePassengerByDriver =
                Restrictions.getPossiblesMatches(drivers, passengers, restriction);

        return selector.select(possiblePassengerByDriver);
    }
}
