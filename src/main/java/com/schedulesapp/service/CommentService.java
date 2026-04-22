package com.schedulesapp.service;


import com.schedulesapp.dto.CommentCreateRequest;
import com.schedulesapp.dto.CommentCreateResponse;
import com.schedulesapp.dto.CommentGetResponse;
import com.schedulesapp.entity.Comment;
import com.schedulesapp.entity.Schedule;
import com.schedulesapp.entity.User;
import com.schedulesapp.exception.CustomException;
import com.schedulesapp.exception.ErrorCode;
import com.schedulesapp.repository.CommentRepository;
import com.schedulesapp.repository.ScheduleRepository;
import com.schedulesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository; // 유저 정보 조회를 위해 추가

    @Transactional
    public CommentCreateResponse saveComment(Long scheduleId, Long userId, CommentCreateRequest request) {

        // 1. 스케줄 존재 여부 확인
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        // 2. 유저 존재 여부 확인 (세션에서 가져온 ID로 실제 유저 객체를 찾습니다)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 3. 댓글 개수 검증 (단방향 해결책: Repository의 count 메서드 활용)
        long commentCount = commentRepository.countByScheduleId(scheduleId);
        if (commentCount >= 10) {
            throw new CustomException(ErrorCode.COMMENT_LIMIT_EXCEEDED);
        }

        // 4. 댓글 생성
        Comment comment = new Comment(
                request.getContent(),
                schedule,
                user
        );

        Comment saved = commentRepository.save(comment);

        // 5. 응답 반환
        return new CommentCreateResponse(
                saved.getId(),
                saved.getSchedule().getId(),
                saved.getUser().getId(),
                saved.getContent(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional
    public List<CommentGetResponse> getAllComment(Long scheduleId) {

        List<Comment> comments = commentRepository.findByScheduleId(scheduleId);
        List<CommentGetResponse> dtos = new ArrayList<>();

        for(Comment comment : comments) {
            CommentGetResponse dto = new CommentGetResponse(
                    comment.getId(),
                    comment.getSchedule().getId(),
                    comment.getSchedule().getUser().getId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
            );
            dtos.add(dto);
        }

        return dtos;
    }
}
