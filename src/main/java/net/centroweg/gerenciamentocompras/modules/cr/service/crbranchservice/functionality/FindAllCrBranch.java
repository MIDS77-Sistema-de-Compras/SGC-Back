package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso responsável por listar todos os vínculos entre CR e filial.
 */
@Service
@RequiredArgsConstructor
public class FindAllCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Lista todos os vínculos CR-filial cadastrados.
     *
     * @return a lista de vínculos (vazia se não houver nenhum)
     */
    public List<CrBranchResponse> findAll() {
        return crBranchRepository.findAll()
                .stream()
                .map(crBranchMapper::toResponse)
                .toList();
    }
}
