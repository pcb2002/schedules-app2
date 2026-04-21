package com.schedulesapp.service;

import com.schedulesapp.dto.*;
import com.schedulesapp.entity.User;
import com.schedulesapp.exception.CustomException;
import com.schedulesapp.exception.ErrorCode;
import com.schedulesapp.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public LoginResponse login(@Valid LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.checkPassword(request.getPassword());

        return new  LoginResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public UserCreateResponse saveUser(UserCreateRequest request) {
        User savedUser = userRepository.save(new User (
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        ));
        return new UserCreateResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public List<UserGetResponse> getAllUser() {
        List<User> users = userRepository.findAll();

        List<UserGetResponse> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(
                    new  UserGetResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getCreatedAt(),
                            user.getUpdatedAt()
                    )
            );
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public UserGetResponse getOneUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserGetResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public UserUpdateResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.update(request.getUsername(), request.getEmail());
        return new UserUpdateResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteUser(Long userId, UserDeleteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.checkPassword(request.getPassword());
        userRepository.delete(user);
    }
}
