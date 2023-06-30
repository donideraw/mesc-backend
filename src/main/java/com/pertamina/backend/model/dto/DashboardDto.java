package com.pertamina.backend.model.dto;

import lombok.Data;

@Data
public class DashboardDto {

    private int totalTask;
    private int totalAssigned;
    private int totalCompleted;

}
