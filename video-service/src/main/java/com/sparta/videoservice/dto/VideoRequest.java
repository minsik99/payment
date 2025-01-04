package com.sparta.videoservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoRequest {
    private String title;
    private int length;
    private String description;
}