package com.sparta.videoservice.repository;

import com.sparta.videoservice.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    boolean existsById(Long videoId);
}