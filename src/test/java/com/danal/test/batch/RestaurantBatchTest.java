package com.danal.test.batch;

import com.danal.test.TestApplication;
import com.danal.test.batch.config.RestaurantJobConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = {
                TestApplication.class,
                RestaurantJobConfiguration.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@SpringBatchTest
@ActiveProfiles("test")
class RestaurantBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("restaurantJob")
    private Job restaurantJob;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJob(restaurantJob);
    }

    @Test
    void testRestaurantJob() throws Exception {
        // Given
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // Then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        
        // Verify step execution
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertEquals(ExitStatus.COMPLETED, stepExecution.getExitStatus());
        
        // Verify read and write counts
        assertTrue(stepExecution.getReadCount() > 0);
        assertTrue(stepExecution.getWriteCount() > 0);
        assertEquals(stepExecution.getReadCount(), stepExecution.getWriteCount() + stepExecution.getSkipCount());
    }

    @Test
    void testRestaurantStep() {
        // Given
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("slaveStep", jobParameters);

        // Then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        
        // Verify step execution
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertEquals(ExitStatus.COMPLETED, stepExecution.getExitStatus());
        
        // Verify read and write counts
        assertTrue(stepExecution.getReadCount() > 0);
        assertTrue(stepExecution.getWriteCount() > 0);
        assertEquals(stepExecution.getReadCount(), stepExecution.getWriteCount() + stepExecution.getSkipCount());
    }
} 