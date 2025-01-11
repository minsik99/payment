package com.sparta.statisticservice.service;

import com.sparta.statisticservice.client.VideoClient;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    private final VideoClient videoClient;

    public StatisticService(VideoClient videoClient) {
        this.videoClient = videoClient;
    }
}