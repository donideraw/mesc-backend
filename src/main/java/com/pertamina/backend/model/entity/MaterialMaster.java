package com.pertamina.backend.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_materials")
@Data
public class MaterialMaster {

    @Id
    private String material;

    private String clientLevel;
    private String blockClientLevel;
    private String materialType;
    private String materialGroup;
    private String oldMaterialNumber;
    private String baseUnit;
    private String partNumber;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String poText;



}
