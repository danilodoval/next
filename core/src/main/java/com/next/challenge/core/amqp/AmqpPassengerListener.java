package com.next.challenge.core.amqp;

import com.next.challenge.core.common.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.next.challenge.core.context.ContextPassengerPoints;
import com.next.challenge.core.context.EventContext;
import com.next.challenge.core.scheduler.SchedulerMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Implements an RabbitListener for handling
 * write events about Passenger.
 */
@Component
@SuppressWarnings("unused")
public class AmqpPassengerListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AmqpSender sender;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange passengerExchange;

    private SchedulerMatch schedulerMatch;

    @SuppressWarnings("rawtypes")
    @RabbitListener(queues = "${PASSENGER_CREATION_QUEUE:passenger.creation}")
    public void passengerCreationListener(
            Message message,
            @Header(AmqpHeaders.CHANNEL) Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {

        final AmqpEventContext context = new AmqpEventContext(message);
        logger.info("Event consumed Context={}", context);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ContextPassengerPoints contextPassengerPoints = mapper.convertValue(message.getPayload(), ContextPassengerPoints.class);

            if (contextPassengerPoints.isEmpty()) {
                Response response = Response.status(Response.Status.BAD_REQUEST).build();
                send(passengerExchange.getName(), context, response, AmqpEventType.PASSENGER_CREATION);
                channel.basicAck(deliveryTag, false);
            } else {
                String identifier = schedulerMatch.addPassenger(
                        contextPassengerPoints.getOriginX(), contextPassengerPoints.getOriginY(),
                        contextPassengerPoints.getDestX(), contextPassengerPoints.getDestY());

                if (!identifier.isEmpty()) {
                    Response response = Response.status(Response.Status.ACCEPTED).entity(identifier).build();
                    send(passengerExchange.getName(), context, response, AmqpEventType.PASSENGER_CREATION);
                    channel.basicAck(deliveryTag, false);
                } else {
                    logger.error(String.format("Could not handle Message=%s", message.toString()));
                    channel.basicNack(deliveryTag, false, true);
                }
            }
        } catch (Throwable e) {
            logger.error(String.format("Could not handle Message=%s", message.toString()), e);
            channel.basicNack(deliveryTag, false, true);
        }
    }

    private <T> void send(String exchange, EventContext context, T payload, AmqpEventType descriptor) {
        final Event<T> event = AmqpEventBuilder.<T>context(context).name(descriptor.getName()).payload(payload).build();
        sender.send(exchange, descriptor.getRoutingKey(), event);
    }
}
