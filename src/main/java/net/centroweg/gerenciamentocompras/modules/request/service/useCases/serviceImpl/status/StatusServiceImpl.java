package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.StatusService;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Implementação principal do serviço da entidade Status.
 *
 * <p>Responsável por centralizar as operações de criação,
 * busca, listagem, atualização e remoção de status.</p>
 *
 * @author André
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final AddStatusService addStatusService;
    private final ListStatusService listStatusService;
    private final FindStatusByIdService findStatusByIdService;
    private final FindStatusByNameService findStatusByNameService;
    private final EditStatusService editStatusService;
    private final DeleteStatusService deleteStatusService;
    /**
     * Realiza o cadastro de um novo status.
     *
     * @param statusRequest dados do status
     * @return status cadastrado
     */
    @Override
    public StatusResponse createStatus(StatusRequest statusRequest) {
        return addStatusService.addStatus(statusRequest);
    }
    /**
     * Busca um status pelo identificador.
     *
     * @param id identificador do status
     * @return status encontrado
     */
    @Override
    public StatusResponse findStatusById(Long id) {
        return findStatusByIdService.findStatusById(id);
    }
    /**
     * Lista todos os status cadastrados.
     *
     * @return lista de status
     */
    @Override
    public StatusResponse findStatusByName(String name){
        return findStatusByNameService.findStatusByName(name);
    }
    @Override
    public List<StatusResponse> findAllStatus() {
        return listStatusService.listStatus();
    }
    /**
     * Atualiza os dados de um status existente.
     *
     * @param id identificador do status
     * @param statusRequest novos dados do status
     * @return status atualizado
     */
    @Override
    public StatusResponse editStatus(Long id, StatusRequest statusRequest) {
        return editStatusService.editStatus(id, statusRequest);
    }
    /**
     * Remove um status pelo identificador.
     *
     * @param id identificador do status
     */
    @Override
    public void deleteStatus(Long id) {
        deleteStatusService.deleteStatus(id);
    }

}
