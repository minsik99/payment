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
public class VideoPlayRequest {
    private Long videoId;
    private LocalDateTime playStart;
    private LocalDateTime playEnd;
}