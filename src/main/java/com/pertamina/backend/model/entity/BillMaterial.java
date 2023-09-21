package com.pertamina.backend.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mst_material_bill")
@Data
public class BillMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String equipmentId;
    private String plant;
    private String itemNumber;
    private String itemCategory;
    private String component;
    private String quantity;
    private String uom;
    private String sortString;
    @Column(columnDefinition = "TEXT")
    private String textLine1;
    @Column(columnDefinition = "TEXT")
    private String textLine2;
    @Column(columnDefinition = "TEXT")
    private String poText;

}
