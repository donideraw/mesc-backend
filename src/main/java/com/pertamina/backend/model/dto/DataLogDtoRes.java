package com.pertamina.backend.model.dto;

import com.pertamina.backend.model.entity.DataLog;
import lombok.Data;

import java.util.List;

@Data
public class DataLogDtoRes {

    private List<DataLog> data;
    private int totalPages;
    private long totalElements;

}
