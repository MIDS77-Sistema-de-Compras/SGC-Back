package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Classe de serviço da {@link Request} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link RequestService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    /**
     * Componente responsável pela criação de uma solicitação.
     */
    private final CreateRequestServiceImpl createRequestService;

    /**
     * Componente responsável pela atualização de uma solicitação.
     */
    private final UpdateRequestServiceImpl updateRequestService;

    /**
     * Componente responsável por remover uma solicitação.
     */
    private final DeleteRequestServiceImpl deleteRequestService;

    /**
     * Componente responsável por qualquer tipo de busca de solicitação.
     */
    private final FindAllRequestServiceImpl findAllRequestService;

    /**
     * Componente responsável pela busca de uma solicitação pelo seu identificador.
     */
    private final FindRequestByIdServiceImpl findRequestByIdService;

    /**
     * Componente responsável pela atualização do feedback de uma solicitação.
     */
    private final UpdateFeedbackServiceImpl updateFeedbackService;

    /**
     * Componente responsável pela busca de solicitações pelo usuário autenticado.
     */
    private final FindAllByUser findAllByUser;

    /**
     * Componente responsável por remover uma solicitação pelo próprio usuário.
     */
    private final DeleteRequestByOwnUser deleteRequestByOwnUser;

    /**
     * Componente responsável pela atualização de uma solicitação pelo próprio usuário.
     */
    private final UpdateRequestByOwnUser updateRequestByOwnUser;

    /**
     * Componente responsável pela busca de uma solicitação pelo seu identificador e pelo próprio usuário.
     */
    private final FindRequestByIdOwnUser findRequestByIdOwnUser;

    /**
     * Componente responsável pela atualização do status de uma solicitação.
     */
    private final UpdateRequestStatusServiceImpl updateRequestStatusService;

    /**
     * Componente responsável pelo upload de anexos de uma solicitação.
     */
    private final UploadRequestAttachmentServiceImpl uploadRequestAttachmentService;

    /**
     * Cria e persiste uma nova solicitação no banco de dados.
     * @param request dados da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return solicitação criada.
     */
    @Override
    public RequestResponse createRequest(RequestRequest request, UserPrincipal userPrincipal){
        return createRequestService.createRequest(request, userPrincipal);
    }

    /**
     * Lista todas as solicitações cadastradas associadas aos filtros informados.
     * @param filter dados de filtro da solicitação.
     * @return lista com todas as solicitações encontradas, caso exista.
     */
    @Override
    public List<RequestResponse> findAllRequest(RequestFilterRequest filter) {
        return findAllRequestService.findAllRequest(filter);
    }

    /**
     * Busca uma solicitação no banco de dados pelo ID informado.
     * @param id identificador da solicitação.
     * @return solicitação encontrada, caso exista.
     */
    @Override
    public RequestResponse findRequestById(Long id) {
        return findRequestByIdService.findRequestById(id);
    }

    /**
     * Atualiza uma solicitação existente no banco de dados.
     * @param request novos dados da solicitação.
     * @param id identificador da solicitação.
     * @return solicitação já atualizada.
     */
    @Override
    public RequestResponse updateRequest(UpdateRequestRequest request, Long id){
        return updateRequestService.updateRequest(request, id);
    }

    /**
     * Lista todas as solicitações cadastradas associadas ao usuário autenticado e aos filtros informados.
     * @param filter dados de filtro da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return lista com todas as solicitações encontradas, caso exista.
     */
    @Override
    public List<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal) {
        return findAllByUser.findAllByUser(filter, userPrincipal);
    }

    /**
     * Remove uma solicitação do banco de dados.
     * @param id identificador da solicitação.
     */
    @Override
    public void deleteRequest(Long id){
        deleteRequestService.deleteRequest(id);
    }

    /**
     * Atualiza o feedback de uma solicitação existente no banco de dados.
     * @param feedback novos dados de feedback da solicitação.
     * @param id identificador da solicitação.
     * @return solicitação já atualizada.
     */
    @Override
    public RequestResponse updateFeedback(UpdateFeedback feedback, Long id){
        return updateFeedbackService.updateFeedback(feedback, id);
    }

    /**
     * Realiza o upload de anexos de uma solicitação no banco de dados.
     * @param requestId identificador da solicitação.
     * @param files arquivos a serem anexados.
     * @return lista com todos os anexos da solicitação criados.
     */
    @Override
    public List<RequestAttachmentResponse> uploadAttachments(
            Long requestId,
            List<MultipartFile> files
    ) {
        return uploadRequestAttachmentService.uploadAttachments(
                requestId,
                files
        );
    }

    /**
     * Remove uma solicitação do banco de dados pelo próprio usuário.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     */
    @Override
    public void deleteRequestByOwnUser(long id, UserPrincipal userPrincipal){
        this.deleteRequestByOwnUser.deleteRequestByOwnUser(id, userPrincipal);
    }

    /**
     * Atualiza uma solicitação existente no banco de dados pelo próprio usuário.
     * @param request novos dados da solicitação.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return solicitação já atualizada.
     */
    @Override
    public RequestResponse updateRequestByOwnUser(RequestRequest request, Long id, UserPrincipal userPrincipal){
        return this.updateRequestByOwnUser.updateRequest(request, id, userPrincipal);
    }

    /**
     * Busca uma solicitação no banco de dados pelo ID informado e associada ao próprio usuário.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return solicitação encontrada, caso exista.
     */
    @Override
    public RequestResponse findRequestByIdOwnUser(Long id, UserPrincipal userPrincipal){
        return this.findRequestByIdOwnUser.findRequestByIdOwnUser(id, userPrincipal);
    }

    /**
     * Atualiza o status de uma solicitação existente no banco de dados.
     * @param id identificador da solicitação.
     * @param request novos dados de status da solicitação.
     * @return solicitação já atualizada.
     */
    @Override
    public RequestResponse updateStatus(Long id, UpdateRequestStatus request) {
        return updateRequestStatusService.updateStatus(id, request);
    }

}
