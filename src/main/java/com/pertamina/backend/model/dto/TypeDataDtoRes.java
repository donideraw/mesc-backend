package com.pertamina.backend.model.dto;

import com.pertamina.backend.model.entity.TypeData;
import lombok.Data;

import java.util.List;

@Data
public class TypeDataDtoRes {

    private List<TypeData> data;
    private int totalPages;
    private long totalElements;

}
