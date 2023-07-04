package com.pertamina.backend.controller;

import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.model.dto.PageableRequest;
import com.pertamina.backend.service.TypeDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/type")
public class TypeDataController {

    private final TypeDataService service;

    public TypeDataController(TypeDataService service) {
        this.service = service;
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<GlobalResponseEntity> getById(@PathVariable("typeId") String typeId) {
        return GlobalResponseEntity.ok(service.getTypeDataById(typeId));
    }

    @PostMapping
    public ResponseEntity<GlobalResponseEntity> getAllBaseDataPaged(@RequestBody PageableRequest request) {
        return GlobalResponseEntity.ok(service.getAllPagedData(request));
    }
}
