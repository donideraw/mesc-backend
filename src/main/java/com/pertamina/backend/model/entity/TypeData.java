package com.pertamina.backend.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "mst_type")
@Data
@TypeDef(
        typeClass = JsonType.class,
        defaultForType = JsonNode.class
)
public class TypeData {

    @Id
    private String typeId;

    private String type;

    @Column(name = "sub_type_1")
    private String typeName;

    @Column(name = "sub_type_2")
    private String subTypeName;

    @Column(columnDefinition = "jsonb")
    private JsonNode attributes;

    @Column(columnDefinition = "jsonb")
    private JsonNode jsonFormat;

}
