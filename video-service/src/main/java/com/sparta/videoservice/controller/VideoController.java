package com.sparta.videoservice.controller;

import com.sparta.videoservice.dto.VideoRequest;
import com.sparta.videoservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<Void> createVideo(@RequestBody VideoRequest videoRequest,
                                            @RequestHeader("email") String email) {
        log.info("Video registration started with data: {}", videoRequest);
        log.info("Email address: {}", email);
        videoService.createVideo(videoRequest, email);
        return ResponseEntity.ok().build();
    }
}