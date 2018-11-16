package com.test.core.amqp;

import com.test.core.context.EventContext;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Context for every event.
 */
public class AmqpEventContext implements EventContext {
    private String eventName;
    private String correlationId;
    private Object payload;

    public AmqpEventContext(String eventName, Object payload) {
        this.eventName = eventName;
        this.payload = payload;
        this.correlationId = getCorrelationId(null);
    }

    AmqpEventContext(Message message) {
        this.eventName = message.getHeaders().get(AmqpHeaders.TYPE, String.class);
        String correlationId;
        if (message.getHeaders().get(AmqpHeaders.CORRELATION_ID) instanceof byte[]) {
            correlationId = new String((byte[]) message.getHeaders().get(AmqpHeaders.CORRELATION_ID));
        } else {
            correlationId = (String) message.getHeaders().get(AmqpHeaders.CORRELATION_ID);
        }
        this.correlationId = correlationId != null ? correlationId : UUID.randomUUID().toString();
        this.payload = message.getPayload();
    }

    String getCorrelationId(byte[] correlationIdBytes) {
        if (correlationIdBytes != null) {
            return new String(correlationIdBytes, StandardCharsets.UTF_8);
        }
        return UUID.randomUUID().toString();
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "AmqpEventContext{" +
                "eventName='" + eventName + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}
