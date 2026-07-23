package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.StatusService;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/**
 * Classe de serviço do {@link Status} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link StatusService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    /**
     * Componente responsável pela criação de um status.
     */
    private final AddStatusService addStatusService;

    /**
     * Componente responsável por qualquer tipo de busca de status.
     */
    private final ListStatusService listStatusService;

    /**
     * Componente responsável pela busca de um status pelo seu identificador.
     */
    private final FindStatusByIdService findStatusByIdService;

    /**
     * Componente responsável pela busca de um status pelo seu nome.
     */
    private final FindStatusByNameService findStatusByNameService;

    /**
     * Componente responsável pela atualização de um status.
     */
    private final EditStatusService editStatusService;

    /**
     * Componente responsável por remover um status.
     */
    private final DeleteStatusService deleteStatusService;

    /**
     * Cria e persiste um novo status no banco de dados.
     * @param statusRequest dados do status.
     * @return status criado.
     */
    @Override
    public StatusResponse createStatus(StatusRequest statusRequest) {
        return addStatusService.addStatus(statusRequest);
    }

    /**
     * Busca um status no banco de dados pelo ID informado.
     * @param id identificador do status.
     * @return status encontrado, caso exista.
     */
    @Override
    public StatusResponse findStatusById(Long id) {
        return findStatusByIdService.findStatusById(id);
    }

    /**
     * Busca um status no banco de dados pelo nome informado.
     * @param name nome do status.
     * @return status encontrado, caso exista.
     */
    @Override
    public StatusResponse findStatusByName(String name){
        return findStatusByNameService.findStatusByName(name);
    }

    /**
     * Lista todos os status cadastrados no banco de dados.
     * @return lista com todos os status encontrados, caso exista.
     */
    @Override
    public List<StatusResponse> findAllStatus() {
        return listStatusService.listStatus();
    }

    /**
     * Atualiza um status existente no banco de dados.
     * @param id identificador do status.
     * @param statusRequest novos dados do status.
     * @return status já atualizado.
     */
    @Override
    public StatusResponse editStatus(Long id, StatusRequest statusRequest) {
        return editStatusService.editStatus(id, statusRequest);
    }

    /**
     * Remove um status do banco de dados.
     * @param id identificador do status.
     */
    @Override
    public void deleteStatus(Long id) {
        deleteStatusService.deleteStatus(id);
    }

}
