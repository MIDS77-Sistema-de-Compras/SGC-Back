package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
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
    public ResponseEntity<RequestResponse> createRequest(@Valid @RequestBody RequestRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal){
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
    public ResponseEntity<RequestResponse> updateRequest(@Valid @RequestBody RequestRequest request, @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateRequest(request, id));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Request")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id){
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "ENDPOINT responsável pela atualização do feedback de Request")
    @PatchMapping("/{id}")
    public ResponseEntity<RequestResponse> updateFeedback(@Valid @RequestBody UpdateFeedback feedback, @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateFeedback(feedback, id));
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
    public ResponseEntity<List<RequestAttachmentResponse>> uploadAttachments(@PathVariable Long id, @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.uploadAttachments(id, files));
    }
}
