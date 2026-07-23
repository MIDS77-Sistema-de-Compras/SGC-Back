package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Interface de serviço para operações de gerenciamento da {@link Request}.
 */
public interface RequestService {

    /**
     * Cria e persiste uma nova solicitação no banco de dados.
     * @param request dados da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return solicitação criada.
     */
    RequestResponse createRequest(RequestRequest request, UserPrincipal userPrincipal);

    /**
     * Lista todas as solicitações cadastradas no banco de dados associadas aos filtros informados.
     * @param filter dados de filtro da solicitação.
     * @return lista com todas as solicitações encontradas, caso exista.
     */
    List<RequestResponse> findAllRequest(RequestFilterRequest filter);

    /**
     * Busca uma solicitação no banco de dados pelo ID informado.
     * @param id identificador da solicitação.
     * @return solicitação encontrada, caso exista.
     */
    RequestResponse findRequestById(Long id);

    /**
     * Lista todas as solicitações cadastradas no banco de dados associadas ao usuário autenticado e aos filtros informados.
     * @param filter dados de filtro da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return lista com todas as solicitações encontradas, caso exista.
     */
    List<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal);

    /**
     * Atualiza uma solicitação existente no banco de dados.
     * @param request novos dados da solicitação.
     * @param id identificador da solicitação.
     * @return solicitação já atualizada.
     */
    RequestResponse updateRequest(UpdateRequestRequest request, Long id);

    /**
     * Remove uma solicitação do banco de dados.
     * @param id identificador da solicitação.
     */
    void deleteRequest(Long id);

    /**
     * Atualiza o feedback de uma solicitação existente no banco de dados.
     * @param feedback novos dados de feedback da solicitação.
     * @param id identificador da solicitação.
     * @return solicitação já atualizada.
     */
    RequestResponse updateFeedback(UpdateFeedback feedback, Long id);

    /**
     * Realiza o upload de anexos de uma solicitação no banco de dados.
     * @param requestId identificador da solicitação.
     * @param files arquivos a serem anexados.
     * @return lista com todos os anexos da solicitação criados.
     */
    List<RequestAttachmentResponse> uploadAttachments(Long requestId, List<MultipartFile> files);

    /**
     * Atualiza o status de uma solicitação existente no banco de dados.
     * @param id identificador da solicitação.
     * @param request novos dados de status da solicitação.
     * @return solicitação já atualizada.
     */
    RequestResponse updateStatus(Long id, UpdateRequestStatus request);

    /**
     * Remove uma solicitação do banco de dados pelo próprio usuário.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     */
    void deleteRequestByOwnUser(long id, UserPrincipal userPrincipal);

    /**
     * Atualiza uma solicitação existente no banco de dados pelo próprio usuário.
     * @param request novos dados da solicitação.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return solicitação já atualizada.
     */
    RequestResponse updateRequestByOwnUser(RequestRequest request, Long id, UserPrincipal userPrincipal);

    /**
     * Busca uma solicitação no banco de dados pelo ID informado e associada ao próprio usuário.
     * @param id identificador da solicitação.
     * @param userPrincipal dados do usuário autenticado.
     * @return solicitação encontrada, caso exista.
     */
    RequestResponse findRequestByIdOwnUser(Long id, UserPrincipal userPrincipal);

}
