package com.next.challenge.driver.driver;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface DriverSource {

    @Output("driverAcceptedChannel")
    MessageChannel driverAccepted();

    @Output("driverCreationChannel")
    MessageChannel driverCreation();

}
