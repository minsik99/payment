package com.sparta.videoservice.service;

import com.sparta.videoservice.client.UserClient;
import com.sparta.videoservice.dto.VideoPlayRequest;
import com.sparta.videoservice.dto.VideoPlayResponse;
import com.sparta.videoservice.entity.VideoPlay;
import com.sparta.videoservice.repository.VideoPlayRepository;
import com.sparta.videoservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoPlayService {

    private final UserClient userClient;
    private final VideoService videoService;
    private final VideoPlayRepository videoPlayRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public VideoPlayResponse playVideo(VideoPlayRequest request, String email) {
        try {
            validateVideoExists(request.getVideoId());
            validatePlaybackTime(request.getPlayStart(), request.getPlayEnd());

            Long userId = getUserIdByEmail(email);
            VideoPlay existingPlay = videoPlayRepository.findByUserIdAndVideoId(userId, request.getVideoId()).orElse(null);

            if (existingPlay != null) {
                return handleExistingPlay(existingPlay, request);
            }

            return handleNewPlay(request, userId);

        } catch (RuntimeException e) {
            log.error("Error during video play processing: {}", e.getMessage());
            throw e;
        }
    }

    private Long getUserIdByEmail(String email) {
        return userClient.getUserIdByEmail(email);
    }

    private long calculateDuration(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).getSeconds();
    }

    private void validateVideoExists(Long videoId) {
        boolean videoExists = videoRepository.existsById(videoId);
        if (!videoExists) {
            throw new RuntimeException("Video not found: " + videoId);
        }
    }

    private void validatePlaybackTime(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new RuntimeException("Invalid playback time range: start=" + start + ", end=" + end);
        }
    }

    private VideoPlayResponse handleExistingPlay(VideoPlay existingPlay, VideoPlayRequest request) {
        long additionalDuration = calculateDuration(request.getPlayStart(), request.getPlayEnd());
        existingPlay.setPlayDuration(existingPlay.getPlayDuration() + additionalDuration);
        existingPlay.setUpdatedAt(LocalDateTime.now());
        videoPlayRepository.save(existingPlay);

        return new VideoPlayResponse(existingPlay.getId(), existingPlay.getVideoId(),
                existingPlay.getUserId(), existingPlay.getPlayDuration());
    }

    private VideoPlayResponse handleNewPlay(VideoPlayRequest request, Long userId) {
        videoService.incrementViewCount(request.getVideoId());

        VideoPlay videoPlay = VideoPlay.builder()
                .userId(userId)
                .videoId(request.getVideoId())
                .playStart(request.getPlayStart())
                .playEnd(request.getPlayEnd())
                .playDuration(calculateDuration(request.getPlayStart(), request.getPlayEnd()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        videoPlayRepository.save(videoPlay);

        return new VideoPlayResponse(videoPlay.getId(), videoPlay.getVideoId(),
                videoPlay.getUserId(), videoPlay.getPlayDuration());
    }

    public Long getTotalPlayDuration(Long videoId) {
        return videoPlayRepository.findByVideoId(videoId).stream()
                .mapToLong(VideoPlay::getPlayDuration)
                .sum();
    }
}