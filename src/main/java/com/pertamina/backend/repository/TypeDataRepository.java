package com.pertamina.backend.repository;

import com.pertamina.backend.model.entity.TypeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeDataRepository extends JpaRepository<TypeData, String> {

    TypeData findByTypeId(String typeId);

}
