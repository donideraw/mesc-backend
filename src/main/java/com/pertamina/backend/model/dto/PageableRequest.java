package com.pertamina.backend.model.dto;

import com.pertamina.backend.helper.DataStatus;
import lombok.Data;

@Data
public class PageableRequest {

    private int page;
    private int size;
    private String sort;
    private String search;
    private DataStatus status;

}
