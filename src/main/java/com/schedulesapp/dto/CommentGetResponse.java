package com.schedulesapp.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentGetResponse {
    private final Long id;
    private final Long scheduleId;
    private final Long userId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentGetResponse(Long id, Long scheduleId, Long userId, String content,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
