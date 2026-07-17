package net.centroweg.gerenciamentocompras.modules.provision.service.api;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;

import java.util.Optional;

/**
 * A interface de API responsável por compartilhar métodos específicos do módulo {@code Provision}
 * @author gabrielEFagundes
 * @version 1.0
 */
public interface ProvisionPublicApi {

    /**
     * Busca uma provisão pelo seu identificador.
     *
     * @param id identificador da {@link Provision}
     * @return um {@link Optional} com a provisão, caso exista
     */
    Optional<Provision> findById(Long id);

    /**
     * Cria e persiste uma nova provisão a partir dos dados informados.
     *
     * @param name        nome da provisão
     * @param totalValue  valor total da provisão
     * @param description descrição da provisão
     * @return a {@link Provision} criada
     */
    Provision createProvision(String name, Double totalValue, String description);

}
