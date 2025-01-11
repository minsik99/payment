package com.sparta.statisticservice.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchJobRunner {

    private final JobLauncher jobLauncher;
    private final Job statisticJob;

    public BatchJobRunner(JobLauncher jobLauncher, Job statisticJob) {
        this.jobLauncher = jobLauncher;
        this.statisticJob = statisticJob;
    }

    @Bean
    public CommandLineRunner runJobManually() {
        return args -> {
            try {
                jobLauncher.run(statisticJob, new JobParameters());
                System.out.println("Batch job executed successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to execute batch job: " + e.getMessage());
            }
        };
    }
}