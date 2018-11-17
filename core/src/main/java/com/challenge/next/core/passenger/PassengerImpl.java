package com.challenge.next.core.passenger;

import com.challenge.next.core.common.AbstractDiscoverable;
import com.challenge.next.core.common.Position;
import com.challenge.next.core.engine.calculators.PositionCalculator;
import com.challenge.next.core.engine.calculators.PositionCalculator.UnfeasiblePathException;

import java.util.List;
import java.util.Objects;

public class PassengerImpl extends AbstractDiscoverable implements Passenger {

    private static int count;

    private final String identifier;

    private final Position destination;
    private Object pathToDestination;

    private Status status = Status.NO_DRIVER;

    public PassengerImpl(final PositionCalculator positionCalculator, final Position initialPosition,
                         final Position destination) {
        super(positionCalculator, initialPosition);
        identifier = Integer.toString(count++);
        this.destination = Objects.requireNonNull(destination);
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Position getDestination() {
        return destination;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized List<? extends Position> pathToDestination() throws UnfeasiblePathException {
        if (pathToDestination == null) {
            try {
                pathToDestination = positionCalculator.path(getPosition(), destination);
            } catch (final UnfeasiblePathException e) {
                pathToDestination = e;
            }
        }

        if (pathToDestination instanceof UnfeasiblePathException) {
            throw (UnfeasiblePathException) pathToDestination;
        }
        return (List<? extends Position>) pathToDestination;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void changeStatus(final Status newStatus) {
        Status.validateTransition(status, newStatus);
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return "position: " + getPosition() + "\ndestination: " + getDestination() + "\nstatus: " + status;
    }
}
