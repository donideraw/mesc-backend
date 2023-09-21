package com.pertamina.backend.service.impl;

import com.pertamina.backend.configuration.exception.CustomException;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.model.dto.TypeDataDtoRes;
import com.pertamina.backend.model.entity.BaseData;
import com.pertamina.backend.model.entity.TypeData;
import com.pertamina.backend.repository.TypeDataRepository;
import com.pertamina.backend.service.TypeDataService;
import com.pertamina.backend.utils.ExcelUtility;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TypeDataServiceImpl implements TypeDataService {

    private final TypeDataRepository repository;

    public TypeDataServiceImpl(TypeDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public TypeDataDtoRes getAllPagedData(PageableRequest request) {
        Sort sort = Sort.by(Sort.Direction.ASC, "typeId");
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<TypeData> typeDataPage = repository.findAll(pageable);

        TypeDataDtoRes res = new TypeDataDtoRes();
        res.setData(typeDataPage.getContent());
        res.setTotalPages(typeDataPage.getTotalPages());
        res.setTotalElements(typeDataPage.getTotalElements());
        return res;
    }

    @Override
    public TypeData getTypeDataById(String typeId) {
        return repository.findByTypeId(typeId);
    }

    @Override
    public List<TypeData> uploadData(MultipartFile file) {

        if (!ExcelUtility.hasExcelFormat(file)) throw new CustomException(
                "File is not Excel!",
                HttpStatus.BAD_REQUEST
        );

        List<TypeData> baseDataList;
        try {
            var workbook = new XSSFWorkbook(file.getInputStream());
            baseDataList = ExcelUtility.importTypeData(workbook);
            workbook.close();
        } catch (IOException e) {
            throw new CustomException("Fail to store data", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return repository.saveAll(baseDataList);
    }
}
