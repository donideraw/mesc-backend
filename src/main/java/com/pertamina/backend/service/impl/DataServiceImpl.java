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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    private final BaseDataRepository baseDataRepository;

    public DataServiceImpl(BaseDataRepository baseDataRepository) {
        this.baseDataRepository = baseDataRepository;
    }

    @Override
    public DashboardDto getDataDashboard() {

        AppAuth auth = SecurityUtil.getAuth();

        List<BaseData> totalData = baseDataRepository.findAll();
        List<BaseData> totalAssignedData = baseDataRepository.findAllByAssignee(auth.getUsername());
        List<BaseData> totalCompletedData = baseDataRepository.findAllByStatus(DataStatus.SUBMITTED);
        List<BaseData> totalRequestedData = baseDataRepository.findAllByStatusAndAssignee(DataStatus.REQUESTED, auth.getUsername());
        List<BaseData> totalTodoData = baseDataRepository.findAllByStatusAndAssignee(DataStatus.ASSIGNED, auth.getUsername());

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
        return baseDataRepository.findAllByStatus(DataStatus.SUBMITTED);
    }

    @Override
    public BaseDataDtoRes getDownloadedData(PageableRequest request) {

        BaseDataDtoRes res = new BaseDataDtoRes();
        Page<BaseData> baseDataPage;

        Sort sort = Sort.by(Sort.Direction.ASC, "uploadedAt");
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

        Sort sort = Sort.by(Sort.Direction.DESC, "uploadedAt");
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        if (AppUserType.STAFF.equals(auth.getUserType())) {
            Specification<BaseData> specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("assignee"), auth.getUsername()),
                            criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("dataId")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("issuer")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + request.getSearch().toUpperCase() + "%")
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
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("dataId")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("issuer")), "%" + request.getSearch().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + request.getSearch().toUpperCase() + "%")
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
        BaseData data = baseDataRepository.findByDataId(dto.getDataId());

        if (data == null) {
            throw new CustomException("Data not found!", HttpStatus.BAD_REQUEST);
        }

        data.setIssuer(dto.getIssuer());
        data.setJsonData(dto.getJsonData());
        data.setReference(dto.getLinkReference());

        return baseDataRepository.save(data);
    }

    @Override
    public BaseData submit(BaseDataDto dto) {
        BaseData data = baseDataRepository.findByDataId(dto.getDataId());

        if (data == null) {
            throw new CustomException("Data not found!", HttpStatus.BAD_REQUEST);
        }

        AppAuth auth = SecurityUtil.getAuth();

        data.setIssuer(dto.getIssuer());
        data.setJsonData(dto.getJsonData());
        data.setReference(dto.getLinkReference());
        data.setStatus(DataStatus.SUBMITTED);
        data.setSubmittedBy(auth.getUsername());
        data.setSubmittedAt(LocalDateTime.now());

        return baseDataRepository.save(data);
    }

    @Override
    public BaseData verify(BaseDataDto dto, DataStatus status) {
        BaseData data = baseDataRepository.findByDataId(dto.getDataId());

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
}
