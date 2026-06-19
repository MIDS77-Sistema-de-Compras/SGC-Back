package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Component;

/**
 * Conversor entre a entidade {@link CrBranch} e seus objetos de transferência (DTOs).
 *
 * <p>Responsável por montar a entidade a partir das suas associações e por
 * transformar a entidade na resposta exposta pela API.</p>
 */
@Component
public class CrBranchMapper {

    public CrBranch toEntity(Branch branch, Cr cr, User responsibleUser) {
        return new CrBranch(
                branch,
                cr,
                responsibleUser
        );
    }

    public CrBranchResponse toResponse(CrBranch crBranch) {
        return new CrBranchResponse(
                crBranch.getId(),
                crBranch.getBranch().getName(),
                crBranch.getCr().getName(),
                crBranch.getCr().getCode(),
                crBranch.getResponsibleUser() != null ? crBranch.getResponsibleUser().getName() : null
        );
    }
}
