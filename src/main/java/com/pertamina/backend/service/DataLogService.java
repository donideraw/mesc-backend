package com.pertamina.backend.service;

import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.DataLogDtoRes;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.model.entity.DataLog;

public interface DataLogService {

    DataLog save(String dataId, DataStatus status);
    DataLogDtoRes getPagedData(PageableRequest request);

}
