package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.AuditParam;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "ENDPOINTS da entidade REQUEST")
@RequiredArgsConstructor
@RequestMapping("/requests")
@RestController
public class RequestController {

    private final RequestService requestService;

    @Operation(description = "ENDPOINT responsável pela criação de Request")
    @PostMapping
    @Auditable(action = "CRIAR_SOLICITACAO", targetFromReturn = true)
    public ResponseEntity<RequestResponse> createRequest(@Valid @AuditParam(value="request") @RequestBody RequestRequest request, @AuditParam("user") @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(request, userPrincipal));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Request")
    @GetMapping
    public ResponseEntity<List<RequestResponse>> findAllRequest(
            @RequestParam(required = false) String crCode,
            @RequestParam(required = false) String statusName,
            @RequestParam(required = false) String supervisorName,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ){
        RequestFilterRequest filter = new RequestFilterRequest(
                crCode,
                statusName,
                supervisorName,
                startDate,
                endDate
        );

        return ResponseEntity.ok(requestService.findAllRequest(filter));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Request por id")
    @GetMapping("/{id}")
    public ResponseEntity<RequestResponse> findRequestById(@PathVariable Long id){
        return ResponseEntity.ok(requestService.findRequestById(id));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de Request")
    @PutMapping("/{id}")
    @Auditable(action = "ATUALIZAR_SOLICITACAO")
    public ResponseEntity<RequestResponse> updateRequest(@Valid @RequestBody UpdateRequestRequest request, @AuditParam("request") @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateRequest(request, id));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Request")
    @DeleteMapping("/{id}")
    @Auditable(action = "DESATIVAR_SOLICITACAO")
    public ResponseEntity<Void> deleteRequest(@AuditParam(value = "request") @PathVariable Long id){
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "ENDPOINT responsável pela atualização do feedback de Request")
    @PatchMapping("/{id}")
    @Auditable(action = "ADICIONAR_FEEDBACK")
    public ResponseEntity<RequestResponse> updateFeedback(@Valid @RequestBody UpdateFeedback feedback, @AuditParam(value = "request") @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateFeedback(feedback, id));
    }

    @Operation(description = "ENDPOINT responsável pela atualização do status de Request")
    @PatchMapping("/{id}/status")
    @Auditable(action = "ATUALIZAR_STATUS_SOLICITACAO")
    public ResponseEntity<RequestResponse> updateStatus(
            @AuditParam(value = "request") @PathVariable Long id,
            @Valid @RequestBody UpdateRequestStatus request
    ) {
        return ResponseEntity.ok(requestService.updateStatus(id, request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<RequestResponse>> findAllByUser(
            @RequestParam(required = false) String crCode,
            @RequestParam(required = false) String statusName,
            @RequestParam(required = false) String supervisorName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        RequestFilterRequest filter = new RequestFilterRequest(crCode, statusName, supervisorName, startDate, endDate);
        return ResponseEntity.ok(requestService.findAllByUser(filter, userPrincipal));
    }

    @Operation(description = "Adiciona anexos em uma solicitação")
    @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Auditable(action = "ANEXAR_ARQUIVOS_SOLICITACAO")
    public ResponseEntity<List<RequestAttachmentResponse>> uploadAttachments(@AuditParam(value = "request") @PathVariable Long id, @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.uploadAttachments(id, files));
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<RequestResponse> findRequestByIdOwnUser(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(requestService.findRequestByIdOwnUser(id, userPrincipal));
    }

    @PutMapping("/me/{id}")
    @Auditable(action = "ATUALIZAR_SOLICITACAO")
    public ResponseEntity<RequestResponse> updateRequestByOwnUser(@Valid @RequestBody RequestRequest request, @AuditParam(value = "request") @PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(requestService.updateRequestByOwnUser(request, id, userPrincipal));
    }

    @DeleteMapping("/me/{id}")
    @Auditable(action = "DELETAR_SOLICITACAO")
    public ResponseEntity<Void> delete(@AuditParam(value = "request") @PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        requestService.deleteRequestByOwnUser(id, userPrincipal);
        return ResponseEntity.status(204).build();
    }



}
