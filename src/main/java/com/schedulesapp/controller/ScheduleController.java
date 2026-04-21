package com.schedulesapp.controller;

import com.schedulesapp.dto.*;
import com.schedulesapp.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @SessionAttribute(name = "loginUser") SessionUser loginUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.saveSchedule(loginUser.getId(), request));
    }

    @GetMapping
    public ResponseEntity<ScheduleGetListResponse> getAllSchedule() {
        return  ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAllSchedule());
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleGetDetailsResponse> getOneSchedule(
            @PathVariable Long scheduleId,
            @SessionAttribute(name = "loginUser") SessionUser loginUser){
        return  ResponseEntity.status(HttpStatus.OK).body(scheduleService.getOneSchedule(
                loginUser.getId(), scheduleId));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleUpdateResponse> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleUpdateRequest request,
            @SessionAttribute(name = "loginUser") SessionUser loginUser){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(
                loginUser.getId(), scheduleId, request));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            @SessionAttribute(name = "loginUser") SessionUser loginUser){
        scheduleService.deleteSchedule(loginUser.getId(), scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
