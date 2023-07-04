package com.pertamina.backend.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.pertamina.backend.helper.DataStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mst_basedata")
@Data
@TypeDef(
        typeClass = JsonType.class,
        defaultForType = JsonNode.class
)
public class BaseData {

    @Id
    private String dataId;

    private String issuer;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    private String assignee;

    private String comment;
    private String reference;

    private String uploadedBy;
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    private String submittedBy;
    private LocalDateTime submittedAt;

    private String checkedBy;
    private LocalDateTime checkedAt;

    @Column(columnDefinition = "jsonb")
    private JsonNode jsonData;

    private String typeId;

}
