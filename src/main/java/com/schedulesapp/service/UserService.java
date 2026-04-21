package com.schedulesapp.service;

import com.schedulesapp.dto.*;
import com.schedulesapp.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public LoginResponse login(@Valid LoginRequest request) {
    }

    public UserCreateResponse createUser(UserCreateRequest request) {
    }

    public List<UserGetResponse> findAllUser() {
    }

    public UserUpdateResponse updateUser(Long id, UserUpdateRequest request) {
    }

    public void deleteUser(Long userId, ScheduleDeleteRequest request) {
    }
}
