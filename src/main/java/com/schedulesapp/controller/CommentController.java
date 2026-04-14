package com.schedulesapp.controller;


import com.schedulesapp.dto.CommentCreateRequest;
import com.schedulesapp.dto.CommentCreateResponse;
import com.schedulesapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment (@RequestBody CommentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.saveComment(request));
    }
}
