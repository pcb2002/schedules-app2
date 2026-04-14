package com.schedulesapp.service;

import com.schedulesapp.dto.*;
import com.schedulesapp.entity.Comment;
import com.schedulesapp.entity.Schedule;
import com.schedulesapp.exception.CustomException;
import com.schedulesapp.exception.ErrorCode;
import com.schedulesapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleCreateResponse saveSchedule(ScheduleCreateRequest request) {
        // 제목 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TITLE_EMPTY);
        }
        if (request.getTitle().length() > 30) {
            throw new CustomException(ErrorCode.INVALID_TITLE_LENGTH);
        }

        // 내용 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_EMPTY);
        }
        if (request.getContent().length() > 200) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_LENGTH);
        }

        // 작성자 및 비밀번호 필수값 검증
        if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
            throw new CustomException(ErrorCode.AUTHOR_REQUIRED);
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED);
        }

        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getAuthor(),
                request.getPassword()
        );
        Schedule saved = scheduleRepository.save(schedule);
        return new ScheduleCreateResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthor(),
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
                    schedule.getAuthor(),
                    schedule.getCreatedAt(),
                    schedule.getUpdatedAt()
            );
            dtos.add(dto);
        }
        return new ScheduleGetListResponse(dtos);
    }

    @Transactional(readOnly = true)
    public ScheduleGetDetailsResponse getOneSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );

        List<CommentGetResponse> dtos = new ArrayList<>();

        for(Comment comment : schedule.getComments()) {
            CommentGetResponse dto = new CommentGetResponse(
                    comment.getId(),
                    comment.getSchedule().getId(),
                    comment.getContent(),
                    comment.getAuthor(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
            );
            dtos.add(dto);
        }

        return new ScheduleGetDetailsResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt(),
                dtos
        );
    }

    @Transactional
    public ScheduleUpdateResponse updateSchedule(Long scheduleId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        // 제목 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TITLE_EMPTY);
        }
        if (request.getTitle().length() > 30) {
            throw new CustomException(ErrorCode.INVALID_TITLE_LENGTH);
        }
        // 작성자 검증
        if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
            throw new CustomException(ErrorCode.AUTHOR_REQUIRED);
        }
        // 비밀번호 검증
        schedule.checkPassword(request.getPassword());

        schedule.update(
                request.getTitle(),
                request.getAuthor()
        );
        return new ScheduleUpdateResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, ScheduleDeleteRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        schedule.checkPassword(request.getPassword());
        scheduleRepository.delete(schedule);
    }
}
