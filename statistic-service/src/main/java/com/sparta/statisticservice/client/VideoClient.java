package com.sparta.statisticservice.client;

import com.sparta.statisticservice.dto.VideoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "video-service")
public interface VideoClient {

    @GetMapping("/internal/videos")
    List<VideoResponse> getAllVideos();

    @GetMapping("/internal/play/duration/{videoId}")
    Long getTotalPlayDuration(@PathVariable("videoId") Long videoId);
}