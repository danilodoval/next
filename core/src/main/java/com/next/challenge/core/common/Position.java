package com.next.challenge.core.common;

/**
 * Represents a spatial position.
 * Implementations should ensure correctness of
 * {@link Object#equals(Object)} and {@link Object#hashCode()}
 */
public interface Position {

    double distanceTo(Position position);

}
