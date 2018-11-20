package com.next.challenger.passenger.passenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}")
public class PassengerController {

    @Autowired
    PassengerService passengerService;

    @GetMapping(value = "/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllDrivers(@PageableDefault(size = 1000) Pageable pageable) {
        Page<Passenger> drivers = passengerService.getAllPassenger(pageable);

        if (!drivers.hasContent())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(drivers);
    }

    @PostMapping(value = "/passengers/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity startOperation() {
        return passengerService.startOperation();
    }

    @PostMapping(value = "/passengers/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity acceptRunning() {
        return passengerService.acceptRunning();
    }

}