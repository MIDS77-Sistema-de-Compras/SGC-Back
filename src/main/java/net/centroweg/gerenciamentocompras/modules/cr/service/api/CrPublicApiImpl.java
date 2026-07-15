package net.centroweg.gerenciamentocompras.modules.cr.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementação da {@link CrPublicApi}, encapsulando o acesso ao
 * {@link CrBranchRepository} para os demais módulos.
 *
 * @see CrPublicApi
 */
@Repository
@RequiredArgsConstructor
public class CrPublicApiImpl implements CrPublicApi {

    private final CrBranchRepository crBranchRepository;

    @Override
    public Optional<CrBranch> findCrBranchById(Long id) {
        return crBranchRepository.findById(id);
    }

}
