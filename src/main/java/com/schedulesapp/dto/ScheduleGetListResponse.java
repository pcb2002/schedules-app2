package com.schedulesapp.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleGetListResponse {
    private List<ScheduleGetOneResponse> schedules;

    public ScheduleGetListResponse(List<ScheduleGetOneResponse> schedules) {
        this.schedules = schedules;
    }
}
