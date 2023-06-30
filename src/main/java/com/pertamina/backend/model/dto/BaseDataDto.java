package com.pertamina.backend.model.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class BaseDataDto {

    private String dataId;
    private String issuer;
    private String linkReference;
    private String comment;
    private JsonNode jsonData;

}
