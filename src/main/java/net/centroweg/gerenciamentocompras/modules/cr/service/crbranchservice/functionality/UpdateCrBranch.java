package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por atualizar um vínculo entre CR e filial.
 *
 * <p>Valida a existência do vínculo, da filial e do CR informados e, opcionalmente,
 * atualiza o usuário responsável associado.</p>
 */
@Service
@RequiredArgsConstructor
public class UpdateCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final CrRepository crRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Atualiza os dados de um vínculo CR-filial existente.
     *
     * <p>O usuário responsável é opcional; quando informado, deve existir no sistema.
     * Quando ausente, o vínculo fica sem responsável.</p>
     *
     * @param id
     * @param request
     * @return o vínculo atualizado
     * @throws CrBranchNotFoundException se o vínculo não for encontrado
     * @throws BranchNotFoundException se a filial não for encontrada
     * @throws CrNotFoundException se o CR não for encontrado
     * @throws UsernameNotFoundException se o responsável informado não for encontrado
     */
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        CrBranch crBranch = crBranchRepository.findById(id)
                .orElseThrow(() -> new CrBranchNotFoundException(id));

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new BranchNotFoundException());

        Cr cr = crRepository.findById(request.crId())
                .orElseThrow(() -> new CrNotFoundException(request.crId()));

        User user = null;
        if (request.responsibleUserId() != null) {
            user = userRepository.findById(request.responsibleUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
        }

        crBranch.setBranch(branch);
        crBranch.setCr(cr);
        crBranch.setResponsibleUser(user);

        crBranchRepository.save(crBranch);
        return crBranchMapper.toResponse(crBranch);
    }
}