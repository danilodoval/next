package com.challenge.next.core.amqp;

import com.challenge.next.core.common.Event;
import com.challenge.next.core.context.EventContext;

/**
 * Constructs an event to send via Rabbit.
 */
class AmqpEventBuilderImpl<T> implements AmqpEventBuilder<T> {
    /**
     * Constructs {@link AmqpEvent#name}
     */
    private String name;

    /**
     * Constructs {@link AmqpEvent#correlationId}
     */
    private String correlationId;

    /**
     * Constructs {@link AmqpEvent#payload}
     */
    private T payload;

    AmqpEventBuilderImpl(EventContext context) {
        this.name = context.getEventName();
        this.correlationId = context.getCorrelationId();
    }

    @Override
    public AmqpEventBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public AmqpEventBuilder<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public Event<T> build() {
        return new AmqpEvent<>(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public T getPayload() {
        return payload;
    }
}
