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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link Request}.
 */
@Tag(name = "ENDPOINTS da entidade solicitação")
@RequiredArgsConstructor
@RequestMapping("/requests")
@RestController
public class RequestController {

    private final RequestService requestService;

    /**
     * Cria uma nova solicitação.
     * @param request dados da solicitação.
     * @param userPrincipal usuário autenticado responsável pela criação.
     * @return solicitação criada.
     */
    @Operation(description = "ENDPOINT responsável pela criação de uma solicitação")
    @PostMapping
    public ResponseEntity<RequestResponse> createRequest(@Valid @RequestBody RequestRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(request, userPrincipal));
    }

    /**
     * Lista todas as solicitações que correspondem ao filtro informado.
     * @param crCode trecho do código do CR.
     * @param statusName nome do status.
     * @param supervisorName trecho do nome do supervisor.
     * @param startDate data inicial do intervalo de criação.
     * @param endDate data final do intervalo de criação.
     * @return lista com todas as solicitações encontradas, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todas as solicitações que correspondem ao filtro")
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

    /**
     * Busca uma solicitação pelo seu identificador.
     * @param id identificador da solicitação.
     * @return solicitação encontrada, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de uma solicitação por id")
    @GetMapping("/{id}")
    public ResponseEntity<RequestResponse> findRequestById(@PathVariable Long id){
        return ResponseEntity.ok(requestService.findRequestById(id));
    }

    /**
     * Atualiza uma solicitação existente.
     * @param id identificador da solicitação.
     * @param request novos dados da solicitação.
     * @return solicitação já atualizada.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de uma solicitação")
    @PutMapping("/{id}")
    public ResponseEntity<RequestResponse> updateRequest(@Valid @RequestBody UpdateRequestRequest request, @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateRequest(request, id));
    }

    /**
     * Remove uma solicitação.
     * @param id identificador da solicitação.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar uma solicitação")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id){
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza o feedback de uma solicitação.
     * @param id identificador da solicitação.
     * @param feedback novo feedback da solicitação.
     * @return solicitação com o feedback atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização do feedback de uma solicitação")
    @PatchMapping("/{id}")
    public ResponseEntity<RequestResponse> updateFeedback(@Valid @RequestBody UpdateFeedback feedback, @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateFeedback(feedback, id));
    }

    /**
     * Atualiza o status de uma solicitação.
     * @param id identificador da solicitação.
     * @param request novo status da solicitação.
     * @return solicitação com o status atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização do status da solicitação")
    @PatchMapping("/{id}/status")
    public ResponseEntity<RequestResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequestStatus request
    ) {
        return ResponseEntity.ok(requestService.updateStatus(id, request));
    }

    /**
     * Lista todas as solicitações do usuário autenticado que correspondem ao filtro informado.
     * @param crCode trecho do código do CR.
     * @param statusName nome do status.
     * @param supervisorName trecho do nome do supervisor.
     * @param startDate data inicial do intervalo de criação.
     * @param endDate data final do intervalo de criação.
     * @param userPrincipal usuário autenticado.
     * @return lista com todas as solicitações do usuário encontradas, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todas as solicitações do usuário autenticado")
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

    /**
     * Adiciona anexos a uma solicitação.
     * @param id identificador da solicitação.
     * @param files arquivos a serem anexados.
     * @return lista com os anexos criados.
     */
    @Operation(description = "Adiciona anexos em uma solicitação")
    @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<RequestAttachmentResponse>> uploadAttachments(@PathVariable Long id, @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.uploadAttachments(id, files));
    }

    /**
     * Busca uma solicitação do usuário autenticado pelo seu identificador.
     * @param id identificador da solicitação.
     * @param userPrincipal usuário autenticado.
     * @return solicitação encontrada, caso exista e pertença ao usuário autenticado.
     */
    @Operation(description = "ENDPOINT responsável pela busca de uma solicitação do usuário autenticado por id")
    @GetMapping("/me/{id}")
    public ResponseEntity<RequestResponse> findRequestByIdOwnUser(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(requestService.findRequestByIdOwnUser(id, userPrincipal));
    }

    /**
     * Atualiza uma solicitação do usuário autenticado.
     * @param id identificador da solicitação.
     * @param request novos dados da solicitação.
     * @param userPrincipal usuário autenticado.
     * @return solicitação já atualizada.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de uma solicitação do usuário autenticado")
    @PutMapping("/me/{id}")
    public ResponseEntity<RequestResponse> updateRequestByOwnUser(@Valid @RequestBody RequestRequest request, @PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(requestService.updateRequestByOwnUser(request, id, userPrincipal));
    }

    /**
     * Remove uma solicitação do usuário autenticado.
     * @param id identificador da solicitação.
     * @param userPrincipal usuário autenticado.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar uma solicitação do usuário autenticado")
    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        requestService.deleteRequestByOwnUser(id, userPrincipal);
        return ResponseEntity.status(204).build();
    }



}
