package com.pertamina.backend.controller;

import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.BaseDataDto;
import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.service.DataLogService;
import com.pertamina.backend.service.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/base")
public class BaseDataController {

    private final DataService dataService;
    private final DataLogService dataLogService;

    public BaseDataController(DataService dataService, DataLogService dataLogService) {
        this.dataService = dataService;
        this.dataLogService = dataLogService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponseEntity> getAllBaseData() {
        return GlobalResponseEntity.ok(dataService.getAllBaseDataCompleted());
    }

    @PostMapping
    public ResponseEntity<GlobalResponseEntity> getAllBaseDataPaged(@RequestBody PageableRequest request) {
        return GlobalResponseEntity.ok(dataService.getAllBaseDataPaged(request));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<GlobalResponseEntity> getDashboardData() {
        return GlobalResponseEntity.ok(dataService.getDataDashboard());
    }

    @PostMapping("/draft")
    public ResponseEntity<GlobalResponseEntity> saveDraft(@RequestBody BaseDataDto dto) {
        return GlobalResponseEntity.ok(dataService.saveDraft(dto));
    }

    @PostMapping("/submit")
    public ResponseEntity<GlobalResponseEntity> submit(@RequestBody BaseDataDto dto) {
        return GlobalResponseEntity.ok(dataService.submit(dto));
    }

    @PostMapping("/request")
    public ResponseEntity<GlobalResponseEntity> request(@RequestBody BaseDataDto dto) {
        return GlobalResponseEntity.ok(dataService.verify(dto, DataStatus.REQUESTED));
    }

    @PostMapping("/approve")
    public ResponseEntity<GlobalResponseEntity> approve(@RequestBody BaseDataDto dto) {
        return GlobalResponseEntity.ok(dataService.verify(dto, DataStatus.SUBMITTED));
    }

    @PostMapping("/download")
    public ResponseEntity<GlobalResponseEntity> getDownloadedData(@RequestBody PageableRequest request) {
        return GlobalResponseEntity.ok(dataService.getDownloadedData(request));
    }

    @PostMapping("/log")
    public ResponseEntity<GlobalResponseEntity> getAllBaseDataLogPaged(@RequestBody PageableRequest request) {
        return GlobalResponseEntity.ok(dataLogService.getPagedData(request));
    }
}
