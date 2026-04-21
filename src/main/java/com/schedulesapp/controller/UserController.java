package com.schedulesapp.controller;

import com.schedulesapp.dto.*;
import com.schedulesapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {
        LoginResponse response = userService.login(request);
        SessionUser sessionMember = new SessionUser(
                response.getId(),
                response.getEmail()
        );
        session.setAttribute("loginUser", sessionMember);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void>  logout(
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser,
            HttpSession session) {
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserCreateResponse> createUser(
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserCreateResponse result = userService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserGetResponse>> getAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserGetResponse> getOneUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOneUser(userId));
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserUpdateResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest request,
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser // 1. 세션을 파라미터로 받습니다.
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(sessionUser.getId(), request));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @Valid @RequestBody UserDeleteRequest request){
        userService.deleteUser(userId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
