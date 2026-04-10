package com.schedulesapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "일정 제목은 필수 입력값입니다."),
    INVALID_TITLE_LENGTH(HttpStatus.BAD_REQUEST, "일정 제목은 최대 30자 이내여야 합니다."),

    INVALID_CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "일정 내용은 필수 입력값입니다."),
    INVALID_CONTENT_LENGTH(HttpStatus.BAD_REQUEST, "일정 내용은 최대 200자 이내여야 합니다."),

    INVALID_COMMENT_EMPTY(HttpStatus.BAD_REQUEST, "댓글 내용은 필수 입력값입니다."),
    INVALID_COMMENT_LENGTH(HttpStatus.BAD_REQUEST, "댓글 내용은 최대 100자 이내여야 합니다."),

    AUTHOR_REQUIRED(HttpStatus.BAD_REQUEST, "작성자명은 필수 입력값입니다."),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "비밀번호는 필수 입력값입니다."),

    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."),
    COMMENT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "댓글은 최대 10개 입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
