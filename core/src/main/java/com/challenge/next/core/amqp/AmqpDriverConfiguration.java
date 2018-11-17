package com.challenge.next.core.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the AMQP infrastructure for handling drivers events.
 */
@Configuration
class AmqpDriverConfiguration {

    @Value("${DRIVER_CREATION_QUEUE:driver.creation}")
    private String driverCreationQueue;

    @Value("${DRIVER_CREATION_ROUTING_KEY:creation}")
    private String driverCreationRoutingKey;

    @Value("${DRIVER_ACCEPTED_QUEUE:driver.accepted}")
    private String driverAcceptedQueue;

    @Value("${DRIVER_ACCEPTED_ROUTING_KEY:accepted}")
    private String driverAcceptedRoutingKey;

    @Autowired
    private TopicExchange driverExchange;

    @Bean
    Queue driverCreationQueue() {
        return new Queue(driverCreationQueue, true);
    }

    @Bean
    Queue driverCreationQueueReply() {
        return new Queue(driverCreationQueue + ".reply", true);
    }

    @Bean
    Queue driverAcceptedQueue() {
        return new Queue(driverAcceptedQueue, true);
    }

    @Bean
    Binding driveCreationQueueBinding() {
        return BindingBuilder.bind(driverCreationQueue()).to(driverExchange).with(driverCreationRoutingKey);
    }

    @Bean
    Binding driveCreationQueueBindingReply() {
        return BindingBuilder.bind(driverCreationQueueReply()).to(driverExchange).with(driverCreationRoutingKey + ".reply");
    }

    @Bean
    Binding driveAcceptedQueueBinding() {
        return BindingBuilder.bind(driverAcceptedQueue()).to(driverExchange).with(driverAcceptedRoutingKey);
    }
}
