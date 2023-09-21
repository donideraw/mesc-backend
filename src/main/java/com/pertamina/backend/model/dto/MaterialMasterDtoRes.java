package com.pertamina.backend.model.dto;

import com.pertamina.backend.model.entity.MaterialMaster;
import lombok.Data;

import java.util.List;

@Data
public class MaterialMasterDtoRes {

    private List<MaterialMaster> data;
    private int totalPages;
    private long totalElements;

}
