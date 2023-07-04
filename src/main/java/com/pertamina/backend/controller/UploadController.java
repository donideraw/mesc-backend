package com.pertamina.backend.controller;

import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.service.DataService;
import com.pertamina.backend.service.TypeDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    private final DataService dataService;
    private final TypeDataService typeDataService;

    public UploadController(DataService dataService, TypeDataService typeDataService) {
        this.dataService = dataService;
        this.typeDataService = typeDataService;
    }

    @PostMapping("/upload")
    public ResponseEntity<GlobalResponseEntity> uploadData(
            @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponseEntity.ok(dataService.uploadData(file));
    }

    @PostMapping("/upload/type")
    public ResponseEntity<GlobalResponseEntity> uploadType(
            @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponseEntity.ok(typeDataService.uploadData(file));
    }

}
