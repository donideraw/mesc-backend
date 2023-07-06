package com.pertamina.backend.service.impl;

import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.AppAuth;
import com.pertamina.backend.model.dto.DataLogDtoRes;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.model.entity.BaseData;
import com.pertamina.backend.model.entity.DataLog;
import com.pertamina.backend.repository.DataLogRepository;
import com.pertamina.backend.service.DataLogService;
import com.pertamina.backend.utils.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DataLogServiceImpl implements DataLogService {

    private final DataLogRepository repository;

    public DataLogServiceImpl(DataLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public DataLog save(String dataId, DataStatus status) {
        AppAuth auth = SecurityUtil.getAuth();

        DataLog log = new DataLog();
        log.setDataId(dataId);
        log.setStatus(status);
        log.setUpdatedBy(auth.getUsername());
        log.setUserType(auth.getUserType());

        return repository.save(log);
    }

    @Override
    public DataLogDtoRes getPagedData(PageableRequest request) {

        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<DataLog> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("dataId")), "%" + request.getSearch().toUpperCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("updatedBy")), "%" + request.getSearch().toUpperCase() + "%")
                );

        Page<DataLog> baseDataPaged = repository.findAll(specification, pageable);

        DataLogDtoRes res = new DataLogDtoRes();

        res.setData(baseDataPaged.getContent());
        res.setTotalPages(baseDataPaged.getTotalPages());
        res.setTotalElements(baseDataPaged.getTotalElements());

        return res;


    }
}
