package com.schedulesapp.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleGetListResponse {
    private List<ScheduleGetAllResponse> schedules;

    public ScheduleGetListResponse(List<ScheduleGetAllResponse> schedules) {
        this.schedules = schedules;
    }
}
