package com.pertamina.backend.service;

import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.BaseDataDto;
import com.pertamina.backend.model.dto.BaseDataDtoRes;
import com.pertamina.backend.model.dto.DashboardDto;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.model.entity.BaseData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataService {
    DashboardDto getDataDashboard();
    BaseDataDtoRes getDownloadedData(PageableRequest request);
    List<BaseData> getAllBaseDataCompleted();
    BaseDataDtoRes getAllBaseDataPaged(PageableRequest request);
    List<BaseData> uploadData(MultipartFile file);
    BaseData saveDraft(BaseDataDto dto);
    BaseData submit(BaseDataDto dto);
    BaseData verify(BaseDataDto dto, DataStatus status);
}
