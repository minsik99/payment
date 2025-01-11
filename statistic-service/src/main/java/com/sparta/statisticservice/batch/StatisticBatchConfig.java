package com.sparta.statisticservice.batch;

import com.sparta.statisticservice.dto.VideoResponse;
import com.sparta.statisticservice.entity.Statistic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class StatisticBatchConfig {

    @Bean
    public Job statisticJob(JobRepository jobRepository, Step statisticStep) {
        return new JobBuilder("statisticJob", jobRepository)
                .start(statisticStep)
                .build();
    }

    @Bean
    public Step statisticStep(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              ItemReader<VideoResponse> videoReader,
                              ItemProcessor<VideoResponse, Statistic> videoProcessor,
                              ItemWriter<Statistic> statisticWriter) {
        return new StepBuilder("statisticStep", jobRepository)
                .<VideoResponse, Statistic>chunk(10, transactionManager)
                .reader(videoReader)
                .processor(videoProcessor)
                .writer(statisticWriter)
                .build();
    }
}