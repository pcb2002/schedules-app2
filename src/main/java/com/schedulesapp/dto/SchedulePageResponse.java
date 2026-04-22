package com.schedulesapp.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SchedulePageResponse {
    private final String title;
    private final String content;
    private final Long commentCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String username;

    public SchedulePageResponse(String title, String content, Long commentCount, LocalDateTime createdAt, LocalDateTime updatedAt, String username) {
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
    }
}
