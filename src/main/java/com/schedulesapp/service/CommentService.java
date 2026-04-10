package com.schedulesapp.service;


import com.schedulesapp.dto.CommentCreateRequest;
import com.schedulesapp.dto.CommentCreateResponse;
import com.schedulesapp.entity.Comment;
import com.schedulesapp.entity.Schedule;
import com.schedulesapp.exception.CustomException;
import com.schedulesapp.exception.ErrorCode;
import com.schedulesapp.repository.CommentRepository;
import com.schedulesapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentCreateResponse saveComment(Long scheduleId, CommentCreateRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (schedule.getComments().size() >= 10) {
            throw new CustomException(ErrorCode.COMMENT_LIMIT_EXCEEDED);
        }

        Comment comment = new Comment(
                request.getContent(),
                request.getAuthor(),
                request.getPassword(),
                schedule
        );

        Comment saved = commentRepository.save(comment);
        return new CommentCreateResponse(
                saved.getId(),
                saved.getSchedule().getId(),
                saved.getContent(),
                saved.getAuthor(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
