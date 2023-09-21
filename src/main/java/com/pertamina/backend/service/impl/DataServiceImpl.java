package com.pertamina.backend.service.impl;

import com.pertamina.backend.configuration.exception.CustomException;
import com.pertamina.backend.helper.AppUserType;
import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.*;
import com.pertamina.backend.model.entity.BaseData;
import com.pertamina.backend.repository.BaseDataRepository;
import com.pertamina.backend.service.DataService;
import com.pertamina.backend.utils.ExcelUtility;
import com.pertamina.backend.utils.SecurityUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    private final BaseDataRepository baseDataRepository;

    public DataServiceImpl(BaseDataRepository baseDataRepository) {
        this.baseDataRepository = baseDataRepository;
    }

    private final Path root = Paths.get("/Users/doniderawibisana/Documents/mesc/file-uploaded");

    @Override
    public DashboardDto getDataDashboard() {

        AppAuth auth = SecurityUtil.getAuth();

        List<BaseData> totalData = baseDataRepository.findAll();
        List<BaseData> totalAssignedData = baseDataRepository.findAll();
        List<BaseData> totalCompletedData = baseDataRepository.findAll();
        List<BaseData> totalRequestedData = baseDataRepository.findAll();
        List<BaseData> totalTodoData = baseDataRepository.findAll();

        DashboardDto dto = new DashboardDto();
        dto.setTotalTask(totalData.size());
        dto.setTotalAssigned(totalAssignedData.size());
        dto.setTotalCompleted(totalCompletedData.size());
        dto.setTodo(totalTodoData.size() > 5 ? totalTodoData.subList(0, 5) : totalTodoData);
        dto.setRequested(totalRequestedData.size() > 5 ? totalRequestedData.subList(0, 5) : totalRequestedData);

        return dto;
    }

    @Override
    public List<BaseData> getAllBaseDataCompleted() {
        return baseDataRepository.findAllByOrderByEquipmentIdAsc();
    }

    @Override
    public BaseDataDtoRes getDownloadedData(PageableRequest request) {

        BaseDataDtoRes res = new BaseDataDtoRes();
        Page<BaseData> baseDataPage;

        Sort sort = Sort.by(Sort.Direction.ASC, "equipmentId");
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<BaseData> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), DataStatus.SUBMITTED);
        baseDataPage = baseDataRepository.findAll(specification, pageable);

        res.setData(baseDataPage.getContent());
        res.setTotalPages(baseDataPage.getTotalPages());
        res.setTotalElements(baseDataPage.getTotalElements());

        return res;
    }

    @Override
    public BaseDataDtoRes getAllBaseDataPaged(PageableRequest request) {
        AppAuth auth = SecurityUtil.getAuth();
        BaseDataDtoRes res = new BaseDataDtoRes();
        Page<BaseData> baseDataPage;

        Sort sort = Sort.by(Sort.Direction.ASC, "equipmentId");
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        if (AppUserType.STAFF.equals(auth.getUserType())) {
            Specification<BaseData> specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("equipmentId")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("category")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("typeId")), "%" + request.getSearch().toUpperCase() + "%")
                    )
                    );
            baseDataPage = baseDataRepository.findAll(specification, pageable);
        } else if (AppUserType.VERIFICATOR.equals(auth.getUserType())) {
            Specification<BaseData> specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("status"), DataStatus.SUBMITTED),
                            criteriaBuilder.or(
                                    criteriaBuilder.like(criteriaBuilder.upper(root.get("dataId")), "%" + request.getSearch().toUpperCase() + "%"),
                                    criteriaBuilder.like(criteriaBuilder.upper(root.get("issuer")), "%" + request.getSearch().toUpperCase() + "%"),
                                    criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + request.getSearch().toUpperCase() + "%")
                            )
                    );
            baseDataPage = baseDataRepository.findAll(specification, pageable);
        } else {
            Specification<BaseData> specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("equipmentId")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("category")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("typeId")), "%" + request.getSearch().toUpperCase() + "%")
                    );
            baseDataPage = baseDataRepository.findAll(specification, pageable);
        }

        res.setData(baseDataPage.getContent());
        res.setTotalPages(baseDataPage.getTotalPages());
        res.setTotalElements(baseDataPage.getTotalElements());

        return res;
    }

    @Override
    public List<BaseData> uploadData(MultipartFile file) {
        if (!ExcelUtility.hasExcelFormat(file)) throw new CustomException(
                "File is not Excel!",
                HttpStatus.BAD_REQUEST
        );

        List<BaseData> baseDataList;
        try {
            var workbook = new XSSFWorkbook(file.getInputStream());
            baseDataList = ExcelUtility.importData(workbook);
            workbook.close();
        } catch (IOException e) {
            throw new CustomException("Fail to store data", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return baseDataRepository.saveAll(baseDataList);
    }

    @Override
    public BaseData saveDraft(BaseDataDto dto) {
        BaseData data = baseDataRepository.findByEquipmentId(dto.getEquipmentId());

        if (data == null) {
            data = new BaseData();
            data.setEquipmentId(dto.getEquipmentId());
        }

        data.setCategory(dto.getCategory());
        data.setDescription(dto.getDescription());
        data.setWeight(dto.getWeight());
        data.setUom(dto.getUom());
        data.setSize(dto.getSize());
        data.setTypeId(dto.getTypeId());
        data.setLocation(dto.getLocation());
        data.setFunctionalLocation(dto.getFunctionalLocation());
        data.setIdentificationNo(dto.getIdentificationNo());
        data.setDrawingNo(dto.getDrawingNo());
        data.setManufacturer(dto.getManufacturer());
        data.setModel(dto.getModel());
        data.setPartNo(dto.getPartNo());
        data.setSerialNo(dto.getSerialNo());
        data.setOriginCountry(dto.getOriginCountry());
        data.setConstructionYear(dto.getConstructionYear());
        data.setConstructionMonth(dto.getConstructionMonth());
        data.setFilePath(dto.getFilePath());
        data.setClassification(dto.getClassification());
        data.setStatus(DataStatus.DRAFT);

        return baseDataRepository.save(data);
    }

    @Override
    public BaseData submit(BaseDataDto dto) {
        BaseData data = baseDataRepository.findByEquipmentId(dto.getEquipmentId());

        if (data == null) {
            data = new BaseData();
            data.setEquipmentId(dto.getEquipmentId());
        }

        AppAuth auth = SecurityUtil.getAuth();

        data.setCategory(dto.getCategory());
        data.setDescription(dto.getDescription());
        data.setWeight(dto.getWeight());
        data.setUom(dto.getUom());
        data.setSize(dto.getSize());
        data.setTypeId(dto.getTypeId());
        data.setLocation(dto.getLocation());
        data.setFunctionalLocation(dto.getFunctionalLocation());
        data.setIdentificationNo(dto.getIdentificationNo());
        data.setDrawingNo(dto.getDrawingNo());
        data.setManufacturer(dto.getManufacturer());
        data.setModel(dto.getModel());
        data.setPartNo(dto.getPartNo());
        data.setSerialNo(dto.getSerialNo());
        data.setOriginCountry(dto.getOriginCountry());
        data.setConstructionYear(dto.getConstructionYear());
        data.setConstructionMonth(dto.getConstructionMonth());
        data.setFilePath(dto.getFilePath());
        data.setClassification(dto.getClassification());
        data.setStatus(DataStatus.DRAFT);
        data.setStatus(DataStatus.SUBMITTED);
        data.setSubmittedBy(auth.getUsername());
        data.setSubmittedAt(LocalDateTime.now());

        return baseDataRepository.save(data);
    }

    @Override
    public BaseData verify(BaseDataDto dto, DataStatus status) {
        BaseData data = baseDataRepository.findByEquipmentId(dto.getEquipmentId());

        if (data == null) {
            throw new CustomException("Data not found!", HttpStatus.BAD_REQUEST);
        }

        AppAuth auth = SecurityUtil.getAuth();

        data.setComment(dto.getComment());
        data.setStatus(status);
        data.setCheckedBy(auth.getUsername());
        data.setCheckedAt(LocalDateTime.now());

        return baseDataRepository.save(data);
    }

    @Override
    public String uploadReference(MultipartFile file) throws IOException {
        checkFolder(root);

        Path filePath = root.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    private void checkFolder(Path path) {
        File directory = new File(path.toUri());
        if(!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public byte[] getDocument(DocumentDto dto) throws IOException {
        File file = new File(dto.getFilePath());
        return Files.readAllBytes(file.toPath());
    }
}
