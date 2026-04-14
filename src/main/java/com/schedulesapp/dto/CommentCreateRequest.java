package com.schedulesapp.dto;

import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private Long scheduleId;
    private String content;
    private String author;
    private String password;
}
