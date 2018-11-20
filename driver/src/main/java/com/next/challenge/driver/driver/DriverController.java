package com.next.challenge.driver.driver;

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
public class DriverController {

    @Autowired
    DriverService driverService;

    @GetMapping(value = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllDrivers(@PageableDefault(size = 1000) Pageable pageable) {
        Page<Driver> drivers = driverService.getAllDrivers(pageable);

        if (!drivers.hasContent())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(drivers);
    }

    @PostMapping(value = "/drivers/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity startOperation() {
        return driverService.startOperation();
    }

    @PostMapping(value = "/drivers/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity acceptRunning() {
        return driverService.acceptRunning();
    }

}