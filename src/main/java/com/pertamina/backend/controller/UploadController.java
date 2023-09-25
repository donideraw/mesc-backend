package com.pertamina.backend.controller;

import com.pertamina.backend.configuration.exception.CustomException;
import com.pertamina.backend.model.dto.AppException;
import com.pertamina.backend.model.dto.DocumentDto;
import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.service.BillMaterialService;
import com.pertamina.backend.service.DataService;
import com.pertamina.backend.service.MaterialMasterService;
import com.pertamina.backend.service.TypeDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {

    private final DataService dataService;
    private final TypeDataService typeDataService;
    private final MaterialMasterService materialMasterService;
    private final BillMaterialService billMaterialService;

    public UploadController(DataService dataService, TypeDataService typeDataService, MaterialMasterService materialMasterService, BillMaterialService billMaterialService) {
        this.dataService = dataService;
        this.typeDataService = typeDataService;
        this.materialMasterService = materialMasterService;
        this.billMaterialService = billMaterialService;
    }

    @PostMapping("/upload")
    public ResponseEntity<GlobalResponseEntity> uploadData(
            @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponseEntity.ok(dataService.uploadData(file));
    }

    @PostMapping("/upload/class")
    public ResponseEntity<GlobalResponseEntity> uploadDataClass(
            @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponseEntity.ok(dataService.uploadClassification(file));
    }

    @PostMapping("/upload/type")
    public ResponseEntity<GlobalResponseEntity> uploadType(
            @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponseEntity.ok(typeDataService.uploadData(file));
    }

    @PostMapping("/upload/material")
    public ResponseEntity<GlobalResponseEntity> uploadMaterial(
            @RequestPart("file") MultipartFile file
    ) {
        materialMasterService.uploadMaterial(file);
        return GlobalResponseEntity.ok("Upload Success");
    }

    @PostMapping("/upload/reference")
    public ResponseEntity<GlobalResponseEntity> uploadDocument(@RequestPart(value = "fileReference") MultipartFile file) {
        if (file.getSize() > 5097152) {
            throw new AppException("File too big!");
        }
        String path;
        try {
            path = dataService.uploadReference(file);
        } catch (IOException e) {
            throw new CustomException("Failed to save data!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map<String, String> res = new HashMap<>();
        res.put("filePath", path);

        return GlobalResponseEntity.ok(res);
    }

    @PostMapping("/upload/bill")
    public ResponseEntity<GlobalResponseEntity> uploadBill(
            @RequestPart("file") MultipartFile file
    ) {
        return GlobalResponseEntity.ok(billMaterialService.uploadData(file));
    }

    @PostMapping("/reference")
    public ResponseEntity<GlobalResponseEntity> getDocument(@RequestBody DocumentDto dto) {
        try {
            return GlobalResponseEntity.ok(dataService.getDocument(dto));
        } catch (IOException e) {
            throw new CustomException("File not found!", HttpStatus.NOT_FOUND);
        }
    }


}
