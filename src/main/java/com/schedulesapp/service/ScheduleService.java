package com.schedulesapp.service;

import com.schedulesapp.dto.*;
import com.schedulesapp.entity.Comment;
import com.schedulesapp.entity.Schedule;
import com.schedulesapp.entity.User;
import com.schedulesapp.exception.CustomException;
import com.schedulesapp.exception.ErrorCode;
import com.schedulesapp.repository.CommentRepository;
import com.schedulesapp.repository.ScheduleRepository;
import com.schedulesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ScheduleCreateResponse saveSchedule(Long userId, ScheduleCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                user
        );
        Schedule saved = scheduleRepository.save(schedule);
        return new ScheduleCreateResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getUser().getId(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public ScheduleGetListResponse getAllSchedule() {
        List<Schedule> schedules = scheduleRepository.findAll(
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );
        List<ScheduleGetOneResponse> dtos = new ArrayList<>();

        // for문과 동일한 기능.
//      List<ScheduleGetAllResponse> dtos = schedules.stream()
//            .map(ScheduleGetAllResponse::new)
//            .toList();

        for (Schedule schedule : schedules) {
            ScheduleGetOneResponse dto = new ScheduleGetOneResponse(
                    schedule.getId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getUser().getId(),
                    schedule.getCreatedAt(),
                    schedule.getUpdatedAt()
            );
            dtos.add(dto);
        }
        return new ScheduleGetListResponse(dtos);
    }

    @Transactional(readOnly = true)
    public ScheduleGetDetailsResponse getOneSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );

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

        return new ScheduleGetDetailsResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUser().getId(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt(),
                dtos
        );
    }

    @Transactional
    public ScheduleUpdateResponse updateSchedule(Long userId, Long scheduleId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        schedule.update(
                request.getTitle(),
                request.getContent()
        );
        return new ScheduleUpdateResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUser().getId(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
        scheduleRepository.delete(schedule);
    }
}
