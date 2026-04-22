package com.schedulesapp.controller;


import com.schedulesapp.dto.CommentCreateRequest;
import com.schedulesapp.dto.CommentCreateResponse;
import com.schedulesapp.dto.CommentGetResponse;
import com.schedulesapp.dto.SessionUser;
import com.schedulesapp.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules/{scheduleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment (
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentCreateRequest request,
            @SessionAttribute(name = "loginUser") SessionUser loginUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.saveComment(scheduleId, loginUser.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<CommentGetResponse>> getComments (
            @PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllComment(scheduleId));
    }

}
