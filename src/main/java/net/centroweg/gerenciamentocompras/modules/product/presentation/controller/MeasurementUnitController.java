package net.centroweg.gerenciamentocompras.modules.product.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.MeasurementUnitService;

@RestController
@RequestMapping("/measurement-unit")
@RequiredArgsConstructor
public class MeasurementUnitController {
    
    private final MeasurementUnitService measurementUnitService;

    @PostMapping
    public ResponseEntity<MeasurementUnitResponse> createMeasurementUnit(@Valid @RequestBody MeasurementUnitRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(measurementUnitService.createMeasurementUnit(request));
    }

    @GetMapping
    public ResponseEntity<List<MeasurementUnitResponse>> readMeasurementUnit(){
        return ResponseEntity.ok(measurementUnitService.readMeasurementUnit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementUnitResponse> findMeasurementUnitById(@PathVariable Long id){
        return ResponseEntity.ok(measurementUnitService.findMeasurementUnitById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<MeasurementUnitResponse> findMeasurementUnitByAbbreviation(@RequestParam String abbreviation){
        return ResponseEntity.ok(measurementUnitService.findMeasurementUnitByAbbreviation(abbreviation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeasurementUnitResponse> updateMeasurementUnit( @PathVariable Long id, @Valid @RequestBody MeasurementUnitRequest request){
        return ResponseEntity.ok(measurementUnitService.updateMeasurementUnit(id, request));
    }



}
