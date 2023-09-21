package com.pertamina.backend.repository;

import com.pertamina.backend.model.entity.MaterialMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialMasterRepository extends JpaRepository<MaterialMaster, String> {

    Page<MaterialMaster> findAll(Specification<MaterialMaster> specification, Pageable pageable);

}
