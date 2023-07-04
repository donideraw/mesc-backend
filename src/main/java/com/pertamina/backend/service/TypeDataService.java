package com.pertamina.backend.service;

import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.model.dto.TypeDataDtoRes;
import com.pertamina.backend.model.entity.TypeData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TypeDataService {

    List<TypeData> uploadData(MultipartFile file);

    TypeDataDtoRes getAllPagedData(PageableRequest request);

    TypeData getTypeDataById(String typeId);

}
