package com.pertamina.backend.model.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class BaseDataDto {

    private String equipmentId;

    private String category;
    private String description;
    private String weight;
    private String uom;
    private String size;

    private String typeId;

    private String location;
    private String functionalLocation;
    private String identificationNo;
    private String drawingNo;
    private String manufacturer;
    private String model;
    private String partNo;
    private String serialNo;
    private String originCountry;
    private String constructionYear;
    private String constructionMonth;
    private String filePath;
    private String fileURL;
    private String plant;
    private JsonNode classification;

    private String comment;

}
