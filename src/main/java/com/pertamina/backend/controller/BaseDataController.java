package com.pertamina.backend.controller;

import com.pertamina.backend.model.dto.BaseDataDto;
import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.service.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/base")
public class BaseDataController {

    private final DataService dataService;

    public BaseDataController(DataService dataService) {
        this.dataService = dataService;
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

    @PostMapping("/download")
    public ResponseEntity<GlobalResponseEntity> getDownloadedData(@RequestBody PageableRequest request) {
        return GlobalResponseEntity.ok(dataService.getDownloadedData(request));
    }
}
