package net.centroweg.gerenciamentocompras.modules.provision.service.interfaces;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;

/**
 * Interface detentora dos métodos de manipulação da classe {@code Provision}
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionResponse
 * @see ProvisionRequest
 */
public interface ProvisionService {
    /**
     * Método responsável por manipular a criação de entidades de serviço.
     * @param request A requisição do usuário.
     * @return O DTO de corpo de resposta de serviço.
     */
    ProvisionResponse createProvision(ProvisionRequest request);

    /**
     * Método responsável por manipular a busca de todas as entidades de serviço.
     * @return {@code List<ProvisionResponse>} Uma lista com todos os serviços no banco de dados.
     */
    List<ProvisionResponse> getAllProvisions();

    /**
     * Método responsável por manipular a busca de uma entidade de serviço baseada em seu ID.
     * @param id O ID do serviço desejado.
     * @return O DTO de corpo de resposta de serviço.
     */
    ProvisionResponse getProvisionById(Long id);

    /**
     * Método responsável por atualizar uma entidade de serviço baseada em seu ID.
     * @param id O ID do serviço desejado.
     * @param request A requisição do usuário.
     * @return O DTO de corpo de resposta do serviço.
     */
    ProvisionResponse updateProvision(Long id, ProvisionRequest request);

    /**
     * Método responsável por deletar uma entidade de serviço baseada em seu ID.
     * @param id O ID do serviço desejado.
     */
    void deleteProvision(Long id);
}
