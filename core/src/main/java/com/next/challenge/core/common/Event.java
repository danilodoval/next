package com.next.challenge.core.common;

/**
 * The AMQP event API.
 * <p>
 * It is the interface for getting all the information of an event
 * received by the AMQP broker or other communications.
 */
public interface Event<T> {

    /**
     * Gets the event name.
     *
     * @return the event name
     */
    String getName();

    /**
     * Gets the correlation id.
     *
     * @return the correlation id
     */
    String getCorrelationId();

    /**
     * Gets the payload of the event.
     *
     * @return the payload
     */
    T getPayload();
}
