package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CreateApprovalDelegationRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.approvaldelegationinterface.ApprovalDelegationService;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import net.centroweg.gerenciamentocompras.shared.security.annotation.SupervisorOnly;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Delegações temporárias de aprovação")
@RestController
@RequiredArgsConstructor
@RequestMapping("/approval-delegations")
public class ApprovalDelegationController {

    private final ApprovalDelegationService approvalDelegationService;

    @Operation(description = "Cria uma delegação temporária para todas as cr-filiais do supervisor autenticado")
    @PostMapping
    @SupervisorOnly
    @Auditable(action = "DELEGAR_APROVACAO")
    public ResponseEntity<ApprovalDelegationResponse> create(
            @Valid @RequestBody CreateApprovalDelegationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(approvalDelegationService.create(request));
    }

    @Operation(description = "Lista as delegações criadas pelo usuário autenticado")
    @GetMapping("/me")
    public ResponseEntity<List<ApprovalDelegationResponse>> findMine() {
        return ResponseEntity.ok(approvalDelegationService.findMine());
    }
}
