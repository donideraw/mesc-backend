package com.pertamina.backend.model.dto;

import com.pertamina.backend.model.entity.BaseData;
import lombok.Data;

import java.util.List;

@Data
public class BaseDataDtoRes {

    private List<BaseData> data;
    private int totalPages;
    private long totalElements;

}
