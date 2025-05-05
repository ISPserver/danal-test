package com.danal.test.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
public class RestaurantJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Restaurant Job Started: {}", jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Restaurant Job Completed: {}", jobExecution.getJobId());
        log.info("Job Status: {}", jobExecution.getStatus());
        log.info("Job Exit Status: {}", jobExecution.getExitStatus());


        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startTime = jobExecution.getStartTime();
        LocalDateTime endTime = jobExecution.getEndTime();

        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime.atZone(zoneId), endTime.atZone(zoneId));
            long executionMillis = duration.toMillis();
            log.info("Job Execution Time: {} ms", executionMillis);
        } else {
            log.info("Job Execution Time: Not available (start or end time was null)");
        }

        if (jobExecution.getStatus().isUnsuccessful()) {
            log.error("Job Failed with following exceptions:");
            jobExecution.getAllFailureExceptions().forEach(throwable -> 
                    log.error("Exception: {}", throwable.getMessage()));
        }
    }
} 