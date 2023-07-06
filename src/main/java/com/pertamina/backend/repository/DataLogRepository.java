package com.pertamina.backend.repository;

import com.pertamina.backend.model.entity.DataLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataLogRepository extends JpaRepository<DataLog, Long> {

    Page<DataLog> findAll(Specification<DataLog> specification, Pageable pageable);

}
