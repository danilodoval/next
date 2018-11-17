package com.challenge.next.core.amqp;

import com.challenge.next.core.common.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Amqp Event sender.
 * <p>
 * It uses RabbitMQ.
 */
@Component
class AmqpSenderImpl implements AmqpSender {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @SuppressWarnings("unused")
    private RabbitTemplate rabbitTemplate;

    private AmqpSenderImpl() {
        /* prevents other classes to instantiate it */
    }

    @Override
    public <T> void send(String exchange, String routingKey, Event<T> event) {
        final Map<String, Object> headers = new HashMap<>();
        headers.put(AmqpHeaders.CORRELATION_ID, event.getCorrelationId());
        headers.put(AmqpHeaders.TYPE, event.getName());
        headers.put(AmqpHeaders.CONTENT_TYPE, "application/json");
        try {
            Message message = MessageBuilder.withBody(toJson(event.getPayload()).getBytes())
                    .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json")
                            .copyHeaders(headers).build())
                    .build();
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            logger.info("Message published to Exchange: {} with RoutingKey: {}, Message: {}",
                    exchange, routingKey, message);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing message body", e);
            throw new RuntimeException(e);
        }
    }

    private String toJson(Object payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(payload);
    }
}
