package com.test.core.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Commons AMQP configurations.
 */
@Component
@SuppressWarnings("unused")
class AmqpConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * RabbitMQ URI.
     */
    @Value("${RABBIT_URI:amqp://guest:guest@localhost:5672}")
    private String uri;

    /**
     * The freeman exchange used by Freeman to send
     * database and table events.
     */
    @Value("${DRIVER_EXCHANGE:driver}")
    private String driverExchange;

    /**
     * The freeman exchange used by Freeman to send
     * database and table events.
     */
    @Value("${PASSENGER_EXCHANGE:passenger}")
    private String passengerExchange;

    /**
     * Used by {@link org.springframework.amqp.rabbit.core.RabbitTemplate}
     * to create the exchange at the RabbitMQ.
     *
     * @return the topic
     */
    @Bean
    TopicExchange driverExchange() {
        return new TopicExchange(driverExchange);
    }

    /**
     * Used by {@link org.springframework.amqp.rabbit.core.RabbitTemplate}
     * to create the exchange at the RabbitMQ.
     *
     * @return the topic
     */
    @Bean
    TopicExchange passengerExchange() {
        return new TopicExchange(passengerExchange);
    }

    /**
     * Json message converter.
     * <p>
     * Used by {@link org.springframework.amqp.rabbit.core.RabbitTemplate} to parse
     * json events (comes in the {@link String} type) to any java Object.
     *
     * @return the json converter
     * @see com.fasterxml.jackson.databind.ObjectMapper ObjectMapper
     * @see org.json.simple.JSONObject JSONObject
     */
    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    ConnectionFactory connectionFactory() throws URISyntaxException {
        return new CachingConnectionFactory(new URI(uri));
    }

    @Bean
    public AmqpAdmin amqpAdmin() throws URISyntaxException {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    RabbitTemplate rabbitTemplate() throws URISyntaxException {
        logger.info("Creating amqp connection {}", uri);
        return new RabbitTemplate(connectionFactory());
    }
}
