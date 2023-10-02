package com.pertamina.backend.controller;

import com.pertamina.backend.model.dto.BillMaterialDto;
import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.service.BillMaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill")
public class BillMaterialController {

    private final BillMaterialService service;

    public BillMaterialController(BillMaterialService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<GlobalResponseEntity> getAll(@RequestParam String equipmentId) {
        return GlobalResponseEntity.ok(service.getAll(equipmentId));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalResponseEntity> saveDraft(@RequestBody BillMaterialDto dto) {
        return GlobalResponseEntity.ok(service.save(dto));
    }

}
