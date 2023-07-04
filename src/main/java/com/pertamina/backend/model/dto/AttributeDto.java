package com.pertamina.backend.model.dto;

import lombok.Data;

@Data
public class AttributeDto {

    private String key;
    private String tagName;

    public AttributeDto(String key, String tagName) {
        this.key = key;
        this.tagName = tagName;
    }

}
