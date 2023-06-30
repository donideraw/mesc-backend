package com.pertamina.backend.controller;

import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.service.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    private final DataService dataService;

    public UploadController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/upload")
    public ResponseEntity<GlobalResponseEntity> uploadSettlement(
            @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponseEntity.ok(dataService.uploadData(file));
    }

}
