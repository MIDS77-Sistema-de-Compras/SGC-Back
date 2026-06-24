package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Conversor entre a entidade {@link CrBranch} e seus objetos de transferência (DTOs).
 *
 * <p>Responsável por montar a entidade a partir das suas associações e por
 * transformar a entidade na resposta exposta pela API.</p>
 */
@Component
public class CrBranchMapper {

    public CrBranch toEntity(Branch branch, Cr cr, List<User> responsibleUsers) {
        return new CrBranch(
                branch,
                cr,
                responsibleUsers
        );
    }

    public CrBranchResponse toResponse(CrBranch crBranch) {
        List<String> responsibleUsers = crBranch.getResponsibleUsers() != null
                ? crBranch.getResponsibleUsers().stream().map(User::getName).toList()
                : List.of();
        return new CrBranchResponse(
                crBranch.getId(),
                crBranch.getBranch().getName(),
                crBranch.getCr().getName(),
                crBranch.getCr().getCode(),
                responsibleUsers
        );
    }

    public List<CrBranchResponse> toResponseList(List<CrBranch> crBranches){
        return crBranches
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
