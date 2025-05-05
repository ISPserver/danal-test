package com.danal.test.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
public class RestaurantStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step Started: {}", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step Completed: {}", stepExecution.getStepName());
        log.info("Step Status: {}", stepExecution.getStatus());
        log.info("Step Exit Status: {}", stepExecution.getExitStatus());
        log.info("Step Read Count: {}", stepExecution.getReadCount());
        log.info("Step Write Count: {}", stepExecution.getWriteCount());
        log.info("Step Skip Count: {}", stepExecution.getSkipCount());

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startTime = stepExecution.getStartTime();
        LocalDateTime endTime = stepExecution.getEndTime();

        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime.atZone(zoneId), endTime.atZone(zoneId));
            long executionMillis = duration.toMillis();
            log.info("Job Execution Time: {} ms", executionMillis);
        } else {
            log.info("Job Execution Time: Not available (start or end time was null)");
        }

        if (stepExecution.getStatus().isUnsuccessful()) {
            log.error("Step Failed with following exceptions:");
            stepExecution.getFailureExceptions().forEach(throwable -> 
                    log.error("Exception: {}", throwable.getMessage()));
        }

        return stepExecution.getExitStatus();
    }
} 