package com.pertamina.backend.service.impl;

import com.pertamina.backend.configuration.exception.CustomException;
import com.pertamina.backend.model.dto.BillMaterialDto;
import com.pertamina.backend.model.entity.BillMaterial;
import com.pertamina.backend.model.entity.TypeData;
import com.pertamina.backend.repository.BillMaterialRepository;
import com.pertamina.backend.service.BillMaterialService;
import com.pertamina.backend.utils.ExcelUtility;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BillMaterialServiceImpl implements BillMaterialService {

    private final BillMaterialRepository repository;

    public BillMaterialServiceImpl(BillMaterialRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BillMaterial> getAll(String equipmentId) {
        return repository.findAllByEquipmentIdOrderByIdAsc(equipmentId);
    }

    @Override
    public BillMaterial save(BillMaterialDto dto) {
        return repository.save(toEntity(dto));
    }

    @Override
    public List<BillMaterial> uploadData(MultipartFile file) {
        if (!ExcelUtility.hasExcelFormat(file)) throw new CustomException(
                "File is not Excel!",
                HttpStatus.BAD_REQUEST
        );

        List<BillMaterial> baseDataList;
        try {
            var workbook = new XSSFWorkbook(file.getInputStream());
            baseDataList = ExcelUtility.importBOM(workbook);
            workbook.close();
        } catch (IOException e) {
            throw new CustomException("Fail to store data", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return repository.saveAll(baseDataList);
    }

    private BillMaterial toEntity(BillMaterialDto dto) {
        BillMaterial entity = new BillMaterial();
        entity.setId(dto.getId());
        entity.setEquipmentId(dto.getEquipmentId());
        entity.setPlant(dto.getPlant());
        entity.setItemNumber(dto.getItemNumber());
        entity.setItemCategory(dto.getItemCategory());
        entity.setComponent(dto.getComponent());
        entity.setQuantity(dto.getQuantity());
        entity.setUom(dto.getUom());
        entity.setSortString(dto.getSortString());
        entity.setTextLine1(dto.getTextLine1());
        entity.setTextLine2(dto.getTextLine2());
        entity.setPoText(dto.getPoText());
        return entity;
    }
}
