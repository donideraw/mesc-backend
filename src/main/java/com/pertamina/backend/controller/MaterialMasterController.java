package com.pertamina.backend.controller;

import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.service.MaterialMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material")
public class MaterialMasterController {

    private final MaterialMasterService service;

    public MaterialMasterController(MaterialMasterService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GlobalResponseEntity> getAllBaseDataPaged(@RequestBody PageableRequest request) {
        return GlobalResponseEntity.ok(service.getAllPaged(request));
    }

}
