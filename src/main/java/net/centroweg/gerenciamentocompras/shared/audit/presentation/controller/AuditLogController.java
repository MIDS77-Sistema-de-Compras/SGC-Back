package net.centroweg.gerenciamentocompras.shared.audit.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogFilterRequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.usecases.serviceIntrf.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "ENDPOINT da entidade AUDITLOG")
@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Operation(description = "ENDPOINT responsável pela listagem de todos os registros")
    @GetMapping
    public ResponseEntity<Page<AuditLogDTOResponse>> findAll(
            @RequestParam(required = false) String typeAction,
            @RequestParam(required = false) String agentEmail,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            Pageable pageable
    ){

        AuditLogFilterRequest filter = new AuditLogFilterRequest(
                typeAction,
                agentEmail,
                startDate,
                endDate
        );

        return ResponseEntity.status(200)
                .body(auditLogService.findAll(filter, pageable));
    }
}
