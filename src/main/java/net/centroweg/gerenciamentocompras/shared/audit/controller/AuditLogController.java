package net.centroweg.gerenciamentocompras.shared.audit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.usecase.interfaces.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ENDPOINT da entidade AUDITLOG")
@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Operation(description = "ENDPOINT responsável pela listagem de todos os registros")
    @GetMapping
    public ResponseEntity<List<AuditLogDTOResponse>> findAll(){
        return ResponseEntity.status(200)
                .body(auditLogService.findAll());
    }
}
