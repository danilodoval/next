package com.next.challenge.core.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.next.challenge.core.common.Event;
import com.next.challenge.core.context.EventContext;

/**
 * Produces some message related to Passenger
 */
@Component
public class AmqpPassengerProducer {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpSender sender;

    @Autowired
    public AmqpPassengerProducer(RabbitTemplate template) {
        this.rabbitTemplate = template;
    }

    public <T> void send(String exchange, EventContext context, T payload, AmqpEventType descriptor) {
        final Event<T> event = AmqpEventBuilder.<T>context(context).name(descriptor.getName()).payload(payload).build();
        sender.send(exchange, descriptor.getRoutingKey(), event);
    }

}
