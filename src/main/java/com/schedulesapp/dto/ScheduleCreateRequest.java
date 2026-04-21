package com.schedulesapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleCreateRequest {
    @NotBlank(message = "일정 제목은 필수 입력값입니다.")
    @Size(max=30, message = "일정 제목은 30자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "일정 내용은 필수 입력값입니다.")
    @Size(max=200, message = "일정 내용은 200자 이내여야 합니다.")
    private String content;
}
