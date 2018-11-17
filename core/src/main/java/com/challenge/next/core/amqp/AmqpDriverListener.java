package com.challenge.next.core.amqp;

import com.challenge.next.core.common.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.challenge.next.core.context.ContextDriverPoints;
import com.challenge.next.core.context.EventContext;
import com.challenge.next.core.scheduler.SchedulerMatch;
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
 * write events about Driver.
 */
@Component
@SuppressWarnings("unused")
class AmqpDriverListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AmqpSender sender;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange driverExchange;

    private SchedulerMatch schedulerMatch;

    @SuppressWarnings("rawtypes")
    @RabbitListener(queues = "${DRIVER_CREATION_QUEUE:driver.creation}")
    public void driverCreationListener(
            Message message,
            @Header(AmqpHeaders.CHANNEL) Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {

        final AmqpEventContext context = new AmqpEventContext(message);
        logger.info("Event consumed Context={}", context);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ContextDriverPoints contextDriverPoints = mapper.convertValue(message.getPayload(), ContextDriverPoints.class);

            if (contextDriverPoints.isEmpty()) {
                Response response = Response.status(Response.Status.BAD_REQUEST).build();
                send(driverExchange.getName(), context, response, AmqpEventType.DRIVER_CREATION);
                channel.basicAck(deliveryTag, false);
            } else {
                String identifier = schedulerMatch.addDriver(contextDriverPoints.getOriginX(), contextDriverPoints.getOriginY());
                if (!identifier.isEmpty()) {
                    Response response = Response.status(Response.Status.ACCEPTED).entity(identifier).build();
                    send(driverExchange.getName(), context, response, AmqpEventType.DRIVER_CREATION);
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
