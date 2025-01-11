package com.sparta.statisticservice.batch;

import com.sparta.statisticservice.client.VideoClient;
import com.sparta.statisticservice.dto.VideoResponse;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class VideoItemReader implements ItemReader<VideoResponse> {

    private final VideoClient videoClient;
    private Iterator<VideoResponse> videoIterator;

    public VideoItemReader(VideoClient videoClient) {
        this.videoClient = videoClient;
    }

    @Override
    public VideoResponse read() {
        if (videoIterator == null || !videoIterator.hasNext()) {
            List<VideoResponse> videos = videoClient.getAllVideos();
            videoIterator = videos.iterator();
        }
        return videoIterator.hasNext() ? videoIterator.next() : null;
    }
}