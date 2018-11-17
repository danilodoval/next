package com.challenge.next.core.amqp;

import com.challenge.next.core.common.Event;
import com.challenge.next.core.context.EventContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Produces some message related to Driver
 */
@Component
public class AmqpDriverProducer {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpSender sender;

    @Autowired
    public AmqpDriverProducer(RabbitTemplate template) {
        this.rabbitTemplate = template;
    }

    public <T> void send(String exchange, EventContext context, T payload, AmqpEventType descriptor) {
        final Event<T> event = AmqpEventBuilder.<T>context(context).name(descriptor.getName()).payload(payload).build();
        sender.send(exchange, descriptor.getRoutingKey(), event);
    }
}
