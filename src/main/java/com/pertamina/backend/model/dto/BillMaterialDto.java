package com.pertamina.backend.model.dto;

import lombok.Data;

@Data
public class BillMaterialDto {

    private Long id;
    private String equipmentId;
    private String plant;
    private String itemNumber;
    private String itemCategory;
    private String component;
    private String quantity;
    private String uom;
    private String sortString;
    private String textLine1;
    private String textLine2;
    private String poText;

}
