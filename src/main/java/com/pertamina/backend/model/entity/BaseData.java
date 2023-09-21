package com.pertamina.backend.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.pertamina.backend.helper.DataStatus;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mst_equipment_master")
@Data
@TypeDef(
        typeClass = JsonType.class,
        defaultForType = JsonNode.class
)
public class BaseData {

    @Id
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
    private String plant;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    private String comment;

    private String submittedBy;
    private LocalDateTime submittedAt;

    private String checkedBy;
    private LocalDateTime checkedAt;

    @Column(columnDefinition = "jsonb")
    private JsonNode classification;

}
