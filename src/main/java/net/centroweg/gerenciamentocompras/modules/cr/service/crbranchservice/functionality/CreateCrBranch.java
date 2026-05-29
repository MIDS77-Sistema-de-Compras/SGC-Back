package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchAlreadyExistsException;
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
 * Caso de uso responsável por criar um novo vínculo entre CR e filial.
 *
 * <p>Valida a existência da filial e do CR, impede a criação de vínculos duplicados
 * e, opcionalmente, associa um usuário responsável ao vínculo.</p>
 */
@Service
@RequiredArgsConstructor
public class CreateCrBranch {

    private final CrBranchRepository crBranchRepository;
    private final BranchRepository branchRepository;
    private final CrRepository crRepository;
    private final UserRepository userRepository;
    private final CrBranchMapper crBranchMapper;

    /**
     * Cria um vínculo entre CR e filial a partir dos dados informados.
     *
     * <p>O usuário responsável é opcional; quando informado, deve existir no sistema.</p>
     *
     * @param request
     * @return o vínculo criado
     * @throws BranchNotFoundException se a filial não for encontrada
     * @throws CrNotFoundException se o CR não for encontrado
     * @throws CrBranchAlreadyExistsException se já existir um vínculo entre o CR e a filial
     * @throws UsernameNotFoundException se o responsável informado não for encontrado
     */
    public CrBranchResponse create(CrBranchRequest request) {
        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new BranchNotFoundException());

        Cr cr = crRepository.findById(request.crId())
                .orElseThrow(() -> new CrNotFoundException(request.crId()));

        if (crBranchRepository.findByCrIdAndBranchId(request.crId(), request.branchId()).isPresent()) {
            throw new CrBranchAlreadyExistsException();
        }

        User user = null;
        if (request.responsibleUserId() != null) {
            user = userRepository.findById(request.responsibleUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
        }

        CrBranch crBranch = crBranchMapper.toEntity(branch, cr, user);
        crBranchRepository.save(crBranch);
        return crBranchMapper.toResponse(crBranch);
    }
}
