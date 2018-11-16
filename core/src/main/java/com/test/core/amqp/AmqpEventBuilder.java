package com.test.core.amqp;

import com.test.core.common.Event;
import com.test.core.context.EventContext;

/**
 * Constructs an event to send via Rabbit.
 */
public interface AmqpEventBuilder<T> {

    /**
     * Constructs a context event.
     *
     * @param context the Freeman context
     * @return the EventBuilder
     */
    static <T> AmqpEventBuilder<T> context(EventContext context) {
        return new AmqpEventBuilderImpl<>(context);
    }

    /**
     * Sets the name.
     *
     * @param name the event name
     * @return the builder
     */
    AmqpEventBuilder<T> name(String name);

    /**
     * Sets the payload.
     *
     * @param payload the payload
     * @return the builder
     */
    AmqpEventBuilder<T> payload(T payload);

    /**
     * Builds the event.
     *
     * @return the Event
     */
    Event<T> build();

    /**
     * Gets the event name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the event request ID.
     *
     * @return the request id
     */
    String getCorrelationId();

    /**
     * Gets the event payload.
     *
     * @return the payload
     */
    T getPayload();
}
