package com.schedulesapp.controller;

import com.schedulesapp.dto.*;
import com.schedulesapp.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleCreateResponse> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request,
            @SessionAttribute(name = "loginUser") SessionUser loginUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.saveSchedule(loginUser.getId(), request));
    }

    @GetMapping
    public ResponseEntity<CustomPageResponse<SchedulePageResponse>> getSchedules(
            @PageableDefault(size = 10, sort = "updatedAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        // 1. 서비스에서 기존과 똑같이 Page 객체를 받아온다.
        Page<SchedulePageResponse> pageResult = scheduleService.getAllSchedule(pageable);

        // 2. 커스텀 DTO에 담아서 포장한다.
        CustomPageResponse<SchedulePageResponse> response = new CustomPageResponse<>(pageResult);

        // 3. 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleGetDetailsResponse> getOneSchedule(
            @PathVariable Long scheduleId,
            @SessionAttribute(name = "loginUser") SessionUser loginUser) {
        return  ResponseEntity.status(HttpStatus.OK).body(scheduleService.getOneSchedule(
                loginUser.getId(), scheduleId));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleUpdateResponse> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleUpdateRequest request,
            @SessionAttribute(name = "loginUser") SessionUser loginUser) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(
                loginUser.getId(), scheduleId, request));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            @SessionAttribute(name = "loginUser") SessionUser loginUser) {
        scheduleService.deleteSchedule(loginUser.getId(), scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
