package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/**
 * Interface de serviço para operações de gerenciamento do {@link Status}.
 */
public interface StatusService {

    /**
     * Cria e persiste um novo status no banco de dados.
     * @param statusRequest dados do status.
     * @return status criado.
     */
    StatusResponse createStatus (StatusRequest statusRequest);

    /**
     * Busca um status no banco de dados pelo ID informado.
     * @param id identificador do status.
     * @return status encontrado, caso exista.
     */
    StatusResponse findStatusById (Long id);

    /**
     * Busca um status no banco de dados pelo nome informado.
     * @param name nome do status.
     * @return status encontrado, caso exista.
     */
    StatusResponse findStatusByName (String name);

    /**
     * Lista todos os status cadastrados no banco de dados.
     * @return lista com todos os status encontrados, caso exista.
     */
    List<StatusResponse> findAllStatus ();

    /**
     * Atualiza um status existente no banco de dados.
     * @param id identificador do status.
     * @param statusRequest novos dados do status.
     * @return status já atualizado.
     */
    StatusResponse editStatus (Long id, StatusRequest statusRequest);

    /**
     * Remove um status do banco de dados.
     * @param id identificador do status.
     */
    void deleteStatus (Long id);

}
