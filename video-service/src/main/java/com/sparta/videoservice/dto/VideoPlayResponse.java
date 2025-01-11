package com.sparta.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoPlayResponse {
    private Long id;
    private Long videoId;
    private Long userId;
    private Long playDuration;
}
