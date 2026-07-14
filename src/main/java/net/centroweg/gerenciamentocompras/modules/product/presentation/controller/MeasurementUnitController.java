package net.centroweg.gerenciamentocompras.modules.product.presentation.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.measurementUnit.MeasurementUnitService;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento da {@link MeasurementUnit}.
 */
@Tag(name = "ENDPOINTS da entidade unidade de medida")
@RestController
@RequestMapping("/measurement-unit")
@RequiredArgsConstructor
public class MeasurementUnitController {
    
    private final MeasurementUnitService measurementUnitService;

    /**
     * Cria uma unidade de medida.
     * @param request dados da unidade de medida.
     * @return unidade de medida criada.
     */
    @Operation(description = "ENDPOINT responsável pela criação de uma unidade de medida")
    @PostMapping
    public ResponseEntity<MeasurementUnitResponse> createMeasurementUnit(@Valid @RequestBody MeasurementUnitRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(measurementUnitService.createMeasurementUnit(request));
    }

    /**
     * Lista as unidades de medida cadastradas.
     * @return lista com todas as unidades de medida encontradas.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todas as unidades de medida")
    @GetMapping
    public ResponseEntity<List<MeasurementUnitResponse>> readMeasurementUnit(){
        return ResponseEntity.ok(measurementUnitService.readMeasurementUnit());
    }

    /**
     * Busca uma unidade de medida pelo seu identificador.
     * @param id identificador da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de uma unidade de medida pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<MeasurementUnitResponse> findMeasurementUnitById(@PathVariable Long id){
        return ResponseEntity.ok(measurementUnitService.findMeasurementUnitById(id));
    }

    /**
     * Busca uma unidade de medida pela abreviação(sigla).
     * @param abbreviation abreviação(sigla) da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de uma unidade de medida pela abreviação")
    @GetMapping("/search")
    public ResponseEntity<MeasurementUnitResponse> findMeasurementUnitByAbbreviation(@RequestParam String abbreviation){
        return ResponseEntity.ok(measurementUnitService.findMeasurementUnitByAbbreviation(abbreviation));
    }

    /**
     * Atualiza uma unidade de medida existente.
     * @param id identificador da unidade de medida.
     * @param request novos dados da unidade de medida.
     * @return unidade de medida atualizada.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de unidades de medida")
    @PutMapping("/{id}")
    public ResponseEntity<MeasurementUnitResponse> updateMeasurementUnit( @PathVariable Long id, @Valid @RequestBody MeasurementUnitRequest request){
        return ResponseEntity.ok(measurementUnitService.updateMeasurementUnit(id, request));
    }
}
