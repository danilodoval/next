package com.challenge.next.core;

import com.challenge.next.core.scheduler.SchedulerListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(CoreApplication.class, args);
        SchedulerListener.contextCreated();
    }
}
