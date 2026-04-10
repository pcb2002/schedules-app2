package com.schedulesapp.dto;

import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private String content;
    private String author;
    private String password;
}
