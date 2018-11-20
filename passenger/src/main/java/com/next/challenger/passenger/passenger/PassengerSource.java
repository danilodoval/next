package com.next.challenger.passenger.passenger;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PassengerSource {

    @Output("driverAcceptedChannel")
    MessageChannel passengerAccepted();

    @Output("driverCreationChannel")
    MessageChannel passengerCreation();

}
