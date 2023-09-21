package com.pertamina.backend.service.impl;

import com.pertamina.backend.configuration.exception.CustomException;
import com.pertamina.backend.model.dto.MaterialMasterDtoRes;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.model.entity.MaterialMaster;
import com.pertamina.backend.repository.MaterialMasterRepository;
import com.pertamina.backend.service.MaterialMasterService;
import com.pertamina.backend.utils.ExcelUtility;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MaterialMasterServiceImpl implements MaterialMasterService {

    private final MaterialMasterRepository repository;

    public MaterialMasterServiceImpl(MaterialMasterRepository repository) {
        this.repository = repository;
    }

    @Override
    public void uploadMaterial(MultipartFile file) {
        if (!ExcelUtility.hasExcelFormat(file)) throw new CustomException(
                "File is not Excel!",
                HttpStatus.BAD_REQUEST
        );

        List<MaterialMaster> materials;
        try {
            var workbook = new XSSFWorkbook(file.getInputStream());
            materials = ExcelUtility.importMaterials(workbook);
            workbook.close();
            repository.saveAll(materials);
        } catch (IOException e) {
            throw new CustomException("Fail to store data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MaterialMasterDtoRes getAllPaged(PageableRequest request) {

        MaterialMasterDtoRes res = new MaterialMasterDtoRes();
        Page<MaterialMaster> materialMasterPage;
        Sort sort = Sort.by(Sort.Direction.ASC, "material");
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<MaterialMaster> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.upper(root.get("material")), "%" + request.getSearch().toUpperCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.upper(root.get("poText")), "%" + request.getSearch().toUpperCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + request.getSearch().toUpperCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.upper(root.get("partNumber")), "%" + request.getSearch().toUpperCase() + "%")
                        )
                );
        materialMasterPage = repository.findAll(specification, pageable);

        res.setData(materialMasterPage.getContent());
        res.setTotalPages(materialMasterPage.getTotalPages());
        res.setTotalElements(materialMasterPage.getTotalElements());

        return res;
    }
}
