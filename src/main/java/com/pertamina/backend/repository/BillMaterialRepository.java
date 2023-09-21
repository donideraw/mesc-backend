package com.pertamina.backend.repository;

import com.pertamina.backend.model.entity.BillMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillMaterialRepository extends JpaRepository<BillMaterial, Long> {

    List<BillMaterial> findAllByEquipmentIdOrderByIdAsc(String equipmentId);

}
