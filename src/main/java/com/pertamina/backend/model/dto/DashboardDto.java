package com.pertamina.backend.model.dto;

import com.pertamina.backend.model.entity.BaseData;
import lombok.Data;

import java.util.List;

@Data
public class DashboardDto {

    private int totalTask;
    private int totalAssigned;
    private int totalCompleted;
    private List<BaseData> todo;
    private List<BaseData> requested;

}
