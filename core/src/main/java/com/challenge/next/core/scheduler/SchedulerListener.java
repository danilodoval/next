package com.challenge.next.core.scheduler;

import com.challenge.next.core.CoreApplication;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerListener {

    private static final Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    public static void contextCreated() {
        try {

            logger.info("Setup the Job class and the Job group");
            JobDetail job = JobBuilder.newJob(SchedulerMatch.class).withIdentity("CronQuartzJob", "Group").build();

            logger.info("Create a Trigger that fires every 5 minutes");
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("TriggerName", "Group2")
                    .withSchedule(CronScheduleBuilder.cronSchedule(SchedulerCronType.ONE_MINUTE.getValue()))
                    .build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
        }
    }
}
