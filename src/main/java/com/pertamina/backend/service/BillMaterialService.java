package com.pertamina.backend.service;

import com.pertamina.backend.model.dto.BillMaterialDto;
import com.pertamina.backend.model.entity.BillMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BillMaterialService {

    List<BillMaterial> getAll(String equipmentId);
    BillMaterial save(BillMaterialDto dto);

    List<BillMaterial> uploadData(MultipartFile file);

}
