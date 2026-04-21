package com.schedulesapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
}
