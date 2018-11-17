package com.next.challenge.core.amqp;

import com.next.challenge.core.common.Event;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Class used by jackson to hold the event properties.
 * <p>
 * Jackson can set the protected properties.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AmqpEvent<T> implements Event<T> {

    /**
     * The event name.
     * <p>
     * Directly related to the system change or reaction to something.
     * If some resource was created or deleted, the event name would be
     * something like <b>resource_created</b> or <b>resource_deleted</b>,
     * respectively.
     */
    String name;

    /**
     * The external request that generated all events related to this event.
     */
    String correlationId;

    /**
     * The event payload.
     */
    T payload;

    @SuppressWarnings("unused")
    AmqpEvent() {/* default constructor for Jackson */}

    AmqpEvent(AmqpEventBuilder<T> builder) {
        this.name = builder.getName();
        this.correlationId = builder.getCorrelationId();
        this.payload = builder.getPayload();
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

    @Override
    public String toString() {
        return "EugenioEvent{" +
                "name='" + name + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", payload=" + payload +
                '}';
    }
}
