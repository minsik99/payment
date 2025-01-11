package com.sparta.statisticservice.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job statisticJob;

    public BatchJobScheduler(JobLauncher jobLauncher, Job statisticJob) {
        this.jobLauncher = jobLauncher;
        this.statisticJob = statisticJob;
    }

    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시에 실행
    public void runBatchJob() throws Exception {
        jobLauncher.run(statisticJob, new org.springframework.batch.core.JobParameters());
    }
}