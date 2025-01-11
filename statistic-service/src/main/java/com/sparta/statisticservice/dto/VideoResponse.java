package com.sparta.statisticservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoResponse {
    private Long id;
    private String title;
    private Long viewCount = 0L;
    private Integer length;
}