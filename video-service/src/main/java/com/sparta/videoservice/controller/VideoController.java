package com.sparta.videoservice.controller;

import com.sparta.videoservice.dto.VideoRequest;
import com.sparta.videoservice.dto.VideoResponse;
import com.sparta.videoservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/video")
    public ResponseEntity<Void> createVideo(@RequestBody VideoRequest videoRequest,
                                            @RequestHeader("email") String email) {
        videoService.createVideo(videoRequest, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/internal/videos")
    public ResponseEntity<List<VideoResponse>> getAllVideos() {
        List<VideoResponse> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }
}