package com.next.challenge.driver.driver;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.next.challenge.driver.DriverApplication;

@Service
public class DriverService {

    private static final Logger logger = LoggerFactory.getLogger(DriverApplication.class);
    private static final Random random = new Random(100);
    private static final int MAX_BLOCKED_SIZE_LINE = 500;
    @Autowired
    DriverSource driverSource;
    @Autowired
    private DriverRepository driverRepository;

    Page<Driver> getAllDrivers(Pageable pageable) {
        return driverRepository.findAll(pageable);
    }

    public ResponseEntity startOperation() {
        logger.info("Creating origin points by Driver.");
        int originX = random.nextInt(MAX_BLOCKED_SIZE_LINE);
        int originY = random.nextInt(MAX_BLOCKED_SIZE_LINE);

        String payload = "originX: " + originX + ", originY:" + originY;

        logger.info("Creating mensage of the access for Driver.");
        driverSource.driverCreation().send(MessageBuilder.withPayload(payload).build());
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity acceptRunning() {
        logger.info("Send mensage of the accepted running to Driver.");
        driverSource.driverAccepted().send(MessageBuilder.withPayload("accepted").build());
        return ResponseEntity.accepted().build();
    }

}
