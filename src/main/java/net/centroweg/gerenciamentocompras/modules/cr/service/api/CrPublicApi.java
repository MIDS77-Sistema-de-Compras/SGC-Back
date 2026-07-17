package net.centroweg.gerenciamentocompras.modules.cr.service.api;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

import java.util.Optional;

/**
 * Fachada de comunicação entre módulos para o módulo {@code cr}.
 *
 * <p>Expõe apenas o necessário para que outros módulos consultem dados de
 * Centros de Responsabilidade/Filiais sem acessar o repositório interno
 * diretamente, respeitando o isolamento do monólito modular.</p>
 */
public interface CrPublicApi {

    /**
     * Busca um vínculo CR-filial pelo seu identificador.
     *
     * @param id identificador do {@link CrBranch}
     * @return um {@link Optional} com o vínculo, caso exista
     */
    Optional<CrBranch> findCrBranchById(Long id);

}
