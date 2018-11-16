package com.test.core.amqp;

public enum AmqpEventType {

    DRIVER_CREATION("driver_creation", "driver.creation.reply"),
    DRIVER_ACCEPTED("driver_accepted", "driver_accepted.reply"),
    PASSENGER_CREATION("oassenger_creation", "oassenger.creation.reply"),
    PASSENGER_ACCEPTED("oassenger_accepted", "oassenger_accepted.reply");
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
