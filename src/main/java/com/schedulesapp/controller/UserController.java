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
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionMember,
            HttpSession session) {
        if (sessionMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/users")
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody UserCreateRequest request
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
            @RequestBody UserUpdateRequest request,
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser // 1. 세션을 파라미터로 받습니다.
    ) {
        // 2. 세션에 "loginMember" 정보가 있는지 확인합니다.
        if (sessionUser == null) {
            // 3. 없다면 401 Unauthorized 에러를 던집니다.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(sessionUser.getId(), request));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId, @RequestBody ScheduleDeleteRequest request){
        userService.deleteUser(userId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
