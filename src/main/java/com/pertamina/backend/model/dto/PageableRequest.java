package com.pertamina.backend.model.dto;

import lombok.Data;

@Data
public class PageableRequest {

    private int page;
    private int size;
    private String sort;
    private String search;
    private String category;

}
