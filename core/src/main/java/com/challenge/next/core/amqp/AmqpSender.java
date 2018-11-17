package com.challenge.next.core.amqp;

import com.challenge.next.core.common.Event;

/**
 * Amqp Event sender.
 * It uses RabbitMQ.
 */
public interface AmqpSender {

    /**
     * Sends the Event to the exchange routing by the
     * routingKey.
     *
     * @param exchange   the exchange name
     * @param routingKey the routing key
     * @param event      the event to be sent
     */
    <T> void send(String exchange, String routingKey, Event<T> event);
}
