package com.sparta.videoservice.controller;

import com.sparta.videoservice.dto.VideoPlayRequest;
import com.sparta.videoservice.dto.VideoPlayResponse;
import com.sparta.videoservice.service.VideoPlayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video/play")
@RequiredArgsConstructor
@Slf4j
public class VideoPlayController {
    private final VideoPlayService videoPlayService;

    @PostMapping
    public ResponseEntity<VideoPlayResponse> playVideo(
            @RequestBody VideoPlayRequest videoPlayRequest,
            @RequestHeader("email") String email) {
        VideoPlayResponse response = videoPlayService.playVideo(videoPlayRequest, email);
        return ResponseEntity.ok(response);
    }
}