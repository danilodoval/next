package com.next.challenger.passenger.passenger;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.next.challenger.passenger.PassengerApplication;

@Service
public class PassengerService {

    private static final Logger logger = LoggerFactory.getLogger(PassengerApplication.class);
    private static final Random random = new Random(100);
    private static final int MAX_BLOCKED_SIZE_LINE = 500;
    @Autowired
    PassengerSource passengerSource;
    @Autowired
    private PassengerRepository passengerRepository;

    Page<Passenger> getAllPassenger(Pageable pageable) {
        return passengerRepository.findAll(pageable);
    }

    public ResponseEntity startOperation() {
        logger.info("Creating origin points by Passenger.");
        int originX = random.nextInt(MAX_BLOCKED_SIZE_LINE);
        int originY = random.nextInt(MAX_BLOCKED_SIZE_LINE);
        int destX = random.nextInt(MAX_BLOCKED_SIZE_LINE);
        int destY = random.nextInt(MAX_BLOCKED_SIZE_LINE);

        String payload = "originX: " + originX + ", originY:" + originY +
                ", destX:" + destX + ", destY:" + destY;

        logger.info("Creating mensage of the access for Passenger.");
        passengerSource.passengerCreation().send(MessageBuilder.withPayload(payload).build());
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity acceptRunning() {
        logger.info("Send mensage of the accepted running to Passenger.");
        passengerSource.passengerAccepted().send(MessageBuilder.withPayload("accepted").build());
        return ResponseEntity.accepted().build();
    }

}
