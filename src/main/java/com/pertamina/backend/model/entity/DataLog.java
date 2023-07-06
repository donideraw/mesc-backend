package com.pertamina.backend.model.entity;

import com.pertamina.backend.helper.AppUserType;
import com.pertamina.backend.helper.DataStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mst_log")
@Data
public class DataLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dataId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataStatus status;

    private String updatedBy;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppUserType userType;

}
