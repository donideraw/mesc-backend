package com.pertamina.backend.service;

import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.*;
import com.pertamina.backend.model.entity.BaseData;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DataService {
    DashboardDto getDataDashboard();
    BaseDataDtoRes getDownloadedData(PageableRequest request);
    List<BaseData> getAllBaseDataCompleted();
    BaseDataDtoRes getAllBaseDataPaged(PageableRequest request);
    List<BaseData> uploadData(MultipartFile file);
    List<BaseData> uploadClassification(MultipartFile file);
    BaseData saveDraft(BaseDataDto dto);
    BaseData submit(BaseDataDto dto);
    BaseData verify(BaseDataDto dto, DataStatus status);
    String uploadReference(MultipartFile file) throws IOException;
    byte[] getDocument(DocumentDto dto) throws IOException;
}
