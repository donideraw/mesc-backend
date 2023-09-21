package com.pertamina.backend.service;

import com.pertamina.backend.model.dto.MaterialMasterDtoRes;
import com.pertamina.backend.model.dto.PageableRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MaterialMasterService {

    void uploadMaterial(MultipartFile file);

    MaterialMasterDtoRes getAllPaged(PageableRequest request);

}
