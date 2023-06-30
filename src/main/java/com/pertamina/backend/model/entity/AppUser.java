package com.pertamina.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pertamina.backend.helper.AppUserType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mst_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@JsonIgnoreProperties({"password"})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 35)
    private String username;

    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppUserType userType;

    @Column(length = 64)
    @JsonIgnore
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String createdBy;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonIgnore
    private String updatedBy;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted;
}
