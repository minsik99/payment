package com.sparta.statisticservice.batch;

import com.sparta.statisticservice.client.VideoClient;
import com.sparta.statisticservice.dto.VideoResponse;
import com.sparta.statisticservice.entity.Statistic;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VideoProcessor implements ItemProcessor<VideoResponse, Statistic> {

    private final VideoClient videoClient;

    public VideoProcessor(VideoClient videoClient) {
        this.videoClient = videoClient;
    }

    @Override
    public Statistic process(VideoResponse video) {
        Statistic statistic = new Statistic();
        statistic.setVideoId(video.getId());

        LocalDate now = LocalDate.now();
        statistic.setStartDate(now.minusWeeks(1));
        statistic.setEndDate(now);

        statistic.setTotalViews(video.getViewCount().intValue());
        Long totalPlayDuration = videoClient.getTotalPlayDuration(video.getId());
        statistic.setTotalWatch(totalPlayDuration);

        return statistic;
    }
}