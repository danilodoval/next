package com.next.challenge.core.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the AMQP infrastructure for handling passengers events.
 */
@Configuration
class AmqpPassengerConfiguration {

    @Value("${PASSENGER_CREATION_QUEUE:passenger.creation}")
    private String passengerCreationQueue;

    @Value("${PASSENGER_CREATION_ROUTING_KEY:creation}")
    private String passengerCreationRoutingKey;

    @Value("${PASSENGER_ACCEPTED_QUEUE:passenger.accepted}")
    private String passengerAcceptedQueue;

    @Value("${PASSENGER_ACCEPTED_ROUTING_KEY:accepted}")
    private String passengerAcceptedRoutingKey;

    @Autowired
    private TopicExchange passengerExchange;

    @Bean
    Queue passengerCreationQueue() {
        return new Queue(passengerCreationQueue, true);
    }

    @Bean
    Queue passengerCreationQueueReply() {
        return new Queue(passengerCreationQueue + ".reply", true);
    }

    @Bean
    Queue passengerAcceptedQueue() {
        return new Queue(passengerAcceptedQueue, true);
    }

    @Bean
    Binding passengerCreationQueueBinding() {
        return BindingBuilder.bind(passengerCreationQueue()).to(passengerExchange).with(passengerCreationRoutingKey);
    }

    @Bean
    Binding passengerCreationQueueBindingReply() {
        return BindingBuilder.bind(passengerCreationQueueReply()).to(passengerExchange).with(passengerCreationRoutingKey + ".reply");
    }

    @Bean
    Binding passengerAcceptedQueueBinding() {
        return BindingBuilder.bind(passengerAcceptedQueue()).to(passengerExchange).with(passengerAcceptedRoutingKey);
    }
}
