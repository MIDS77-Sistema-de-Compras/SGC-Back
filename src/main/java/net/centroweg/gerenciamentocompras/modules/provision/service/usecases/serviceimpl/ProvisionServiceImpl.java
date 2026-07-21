package net.centroweg.gerenciamentocompras.modules.provision.service.usecases.serviceimpl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality.AddProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality.DeleteProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality.GetProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality.UpdateProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.service.usecases.serviceintrf.ProvisionService;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.product.IProductService;

/**
 * Classe de serviço do {@link Provision} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link IProductService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class ProvisionServiceImpl implements ProvisionService {

    /**
     * Componente responsável pela criação de um serviço.
     */
    private final AddProvisionService addProvisionService;

    /**
     * Componente responsável por qualquer tipo de busca de serviço.
     */
    private final GetProvisionService getProvisionService;

    /**
     * Componente responsável pela atualização de um serviço.
     */
    private final UpdateProvisionService updateProvisionService;

    /**
     * Componente responsável por remover um serviço.
     */
    private final DeleteProvisionService deleteProvisionService;

    /**
     * Cria e persiste um novo serviço no banco de dados.
     * @param request dados do serviço.
     * @return serviço criado.
     */
    @Override
    public ProvisionResponse createProvision(ProvisionRequest request) {
        return addProvisionService.saveNewProvision(request);
    }

    /**
     * Lista todos os serviços cadastrados no banco de dados.
     * @return lista com todos os serviços encontrados, caso exista.
     */
    @Override
    public List<ProvisionResponse> getAllProvisions() {
        return getProvisionService.getAllProvisions();
    }

    /**
     * Busca um serviço no banco de dados pelo ID informado.
     * @param id identificador do serviço.
     * @return serviço encontrado, caso exista.
     */
    @Override
    public ProvisionResponse getProvisionById(Long id) {
        return getProvisionService.getProvisionById(id);
    }

    /**
     * Atualiza um serviço existente no banco de dados.
     * @param id identificador do serviço.
     * @param request novos dados do serviço.
     * @return serviço já atualizado.
     */
    @Override
    public ProvisionResponse updateProvision(Long id, ProvisionRequest request) {
        return updateProvisionService.updateProvision(id, request);
    }

    /**
     * Deleta um serviço do banco de dados.
     * @param id identificador do serviço.
     */
    @Override
    public void deleteProvision(Long id) {
        deleteProvisionService.deleteProvisionById(id);
    }

}
