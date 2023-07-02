package com.pertamina.backend.repository;

import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.entity.BaseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseDataRepository extends JpaRepository<BaseData, Long> {

    List<BaseData> findAllByAssignee(String assignee);
    List<BaseData> findAllByStatus(DataStatus dataStatus);
    List<BaseData> findAllByStatusAndAssignee(DataStatus dataStatus, String assignee);

    BaseData findByDataId(String dataId);

    Page<BaseData> findAll(Specification<BaseData> specification, Pageable pageable);
}
