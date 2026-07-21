package net.centroweg.gerenciamentocompras.modules.provision.service.usecases.serviceintrf;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Interface de serviço para operações de gerenciamento do {@link Provision}.
 */
public interface ProvisionService {

    /**
     * Cria e persiste um novo serviço no banco de dados.
     * @param request dados do serviço.
     * @return serviço criado.
     */
    ProvisionResponse createProvision(ProvisionRequest request);

    /**
     * Lista todos os serviços cadastrados no banco de dados.
     * @return lista com todos os serviços encontrados, caso exista.
     */
    List<ProvisionResponse> getAllProvisions();

    /**
     * Busca um serviço no banco de dados pelo ID informado.
     * @param id identificador do serviço.
     * @return serviço encontrado, caso exista.
     */
    ProvisionResponse getProvisionById(Long id);

    /**
     * Atualiza um serviço existente no banco de dados.
     * @param id identificador do serviço.
     * @param request novos dados do serviço.
     * @return serviço já atualizado.
     */
    ProvisionResponse updateProvision(Long id, ProvisionRequest request);

    /**
     * Deleta um serviço do banco de dados.
     * @param id identificador do serviço.
     */
    void deleteProvision(Long id);
}
