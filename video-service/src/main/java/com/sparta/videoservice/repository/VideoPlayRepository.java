package com.sparta.videoservice.repository;

import com.sparta.videoservice.entity.VideoPlay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoPlayRepository extends JpaRepository<VideoPlay, Long> {
    Optional<VideoPlay> findByUserIdAndVideoId(Long userId, Long videoId);
}
