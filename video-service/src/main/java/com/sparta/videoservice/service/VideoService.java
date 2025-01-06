package com.sparta.videoservice.service;

import com.sparta.videoservice.client.UserClient;
import com.sparta.videoservice.dto.VideoRequest;
import com.sparta.videoservice.entity.Video;
import com.sparta.videoservice.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserClient userClient;

    @Transactional
    public void createVideo(VideoRequest videoRequest, String email) {
        try {
            // 이메일로 userId 조회
            Long userId = userClient.getUserIdByEmail(email);

            // 비디오 등록
            Video video = new Video();
            video.setUserId(userId);
            video.setTitle(videoRequest.getTitle());
            video.setLength(videoRequest.getLength());
            video.setDescription(videoRequest.getDescription());
            videoRepository.save(video);

            // 역할 업데이트
            userClient.updateRoleToCreator(userId);
        } catch (Exception e) {
            log.error("Error during video creation: {}", e.getMessage());
            throw new RuntimeException("Failed to create video", e);
        }
    }

    public void incrementViewCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));
        video.setViewCount(video.getViewCount() + 1);
        videoRepository.save(video);
    }
}