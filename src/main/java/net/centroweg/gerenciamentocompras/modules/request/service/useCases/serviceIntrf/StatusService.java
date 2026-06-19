package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;

import java.util.List;
/**
 * Interface responsável por definir os serviços
 * relacionados à entidade Status.
 *
 * <p>Contém as operações de criação, busca,
 * listagem, atualização e remoção de status.</p>
 *
 * @author André
 * @since 1.0
 */
public interface StatusService {
    /**
     * Realiza o cadastro de um novo status.
     *
     * @param statusRequest dados do status
     * @return status cadastrado
     */
    StatusResponse createStatus (StatusRequest statusRequest);
    /**
     * Busca um status pelo identificador.
     *
     * @param id identificador do status
     * @return status encontrado
     */
    StatusResponse findStatusById (Long id);
    StatusResponse findStatusByName (String name);
    /**
     * Lista todos os status cadastrados.
     *
     * @return lista de status
     */
    List<StatusResponse> findAllStatus ();
    /**
     * Atualiza os dados de um status existente.
     *
     * @param id identificador do status
     * @param statusRequest novos dados do status
     * @return status atualizado
     */
    StatusResponse editStatus (Long id, StatusRequest statusRequest);
    /**
     * Remove um status pelo identificador.
     *
     * @param id identificador do status
     */
    void deleteStatus (Long id);

}
