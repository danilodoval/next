package com.challenge.next.core.common;

/**
 * Represents entities that have {@link Position}s.
 */
public interface Discoverable {

    /**
     * Indicates the entity's current {@link Position}. It will never be null.
     */
    Position getPosition();

    /**
     * Modifies the entity's current {@link Position}.
     *
     * @param position New {@link Position} of entity.
     */
    void setPosition(Position position);

}
