package com.schedulesapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    @Size(max=100, message = "댓글 내용은 100자 이내여야 합니다.")
    private String content;

}
