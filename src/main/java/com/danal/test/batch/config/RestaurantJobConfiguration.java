package com.danal.test.batch.config;

import com.danal.test.batch.listener.RestaurantJobListener;
import com.danal.test.batch.listener.RestaurantStepListener;
import com.danal.test.batch.listener.RestaurantWriteListener;
import com.danal.test.batch.partitioner.SimplePartitioner;
import com.danal.test.batch.processor.RestaurantItemProcessor;
import com.danal.test.batch.reader.RestaurantItemReader;
import com.danal.test.batch.writer.RestaurantItemWriter;
import com.danal.test.domain.restaurant.Restaurant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantJobConfiguration {

    private static final int CHUNK_SIZE = 1000;
    private static final int PARTITION_SIZE = 8;
    private static final int POOL_SIZE = 8;
    private static final int MAX_POOL_SIZE = 16;
    private static final int QUEUE_CAPACITY = 100;

    JobRepository jobRepository;
    PlatformTransactionManager transactionManager;
    RestaurantJobListener jobListener;
    RestaurantStepListener stepListener;
    RestaurantWriteListener writeListener;

    @Bean
    public Job restaurantJob() {
        return new JobBuilder("restaurantJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .start(masterStep())
                .build();
    }

    @Bean
    public Step masterStep() {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner("slaveStep", partitioner())
                .partitionHandler(partitionHandler())
                .build();
    }

    @Bean
    public Partitioner partitioner() {
        return new SimplePartitioner();
    }

    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(PARTITION_SIZE);
        handler.setTaskExecutor(taskExecutor());
        handler.setStep(slaveStep());
        return handler;
    }

    @Bean
    public Step slaveStep() {
        return new StepBuilder("slaveStep", jobRepository)
                .<Restaurant, Restaurant>chunk(CHUNK_SIZE, transactionManager)
                .reader(restaurantItemReader(null, null))
                .processor(restaurantItemProcessor())
                .writer(restaurantItemWriter(null))
                .listener(stepListener)
                .listener(writeListener)
                .faultTolerant()
                .skipLimit(10)
                .skip(Exception.class)
                .retryLimit(3)
                .retry(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public RestaurantItemReader restaurantItemReader(@Value("#{stepExecutionContext[fromId]}") Integer fromId,
                                                     @Value("#{stepExecutionContext[toId]}") Integer toId) {
        return new RestaurantItemReader(fromId, toId);
    }

    @Bean
    public ItemProcessor<Restaurant, Restaurant> restaurantItemProcessor() {
        return new RestaurantItemProcessor();
    }

    @Bean
    public RestaurantItemWriter restaurantItemWriter(DataSource dataSource) {
        return new RestaurantItemWriter(dataSource);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("restaurant-batch-");
        executor.initialize();
        return executor;
    }
}