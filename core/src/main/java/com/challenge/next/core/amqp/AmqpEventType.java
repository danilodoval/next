package com.challenge.next.core.amqp;

public enum AmqpEventType {

    DRIVER_CREATION("driver_creation", "driver.creation.reply"),
    DRIVER_ACCEPTED("driver_accepted", "driver_accepted.reply"),
    PASSENGER_CREATION("passenger_creation", "passenger.creation.reply"),
    PASSENGER_ACCEPTED("passenger_accepted", "passenger_accepted.reply");
    //ToDo implement queues for other operations

    private String name;
    private String routingKey;

    AmqpEventType(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }

    public String getName() {
        return name;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
