package com.schedulesapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserCreateRequest {
    @NotBlank(message = "유저명은 필수 입력값입니다.")
    @Size(max=4, message = "유저명은 4글자 이내여야 합니다.")
    private String username;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min=8, message = "비밀번호는 8자 이상입니다.")
    private String password;
}
