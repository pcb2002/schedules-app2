package com.schedulesapp.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleGetDetailsResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final List<CommentGetResponse> comments;

    public ScheduleGetDetailsResponse(Long id, String title, String content, Long userId,
                                      LocalDateTime createdAt, LocalDateTime updatedAt, List<CommentGetResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
    }
}
