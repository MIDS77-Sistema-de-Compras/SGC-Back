package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

/**
 * Contrato de serviço para as operações sobre vínculos entre CR e filial.
 *
 * <p>Define os casos de uso disponíveis: CRUD básico, consulta por filial e
 * gerenciamento do usuário responsável pelo vínculo.</p>
 */
public interface CrBranchService {

    CrBranchResponse create(CrBranchRequest request);
    List<CrBranchResponse> findAll(CrBranchFilterRequest filter);
    CrBranchResponse findById(Long id);
    CrBranchResponse update(Long id, CrBranchRequest request);
    MessageDTO delete(Long id);
    List<CrBranchResponse> findCrBranchByBranch(Long branchId);
    CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId);
    CrBranchResponse removeCrBranchResponsible(Long crBranchId);
}
