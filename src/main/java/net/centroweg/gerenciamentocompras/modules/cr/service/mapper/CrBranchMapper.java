package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Component;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;

/**
 * Componente responsável pela conversão entre a entidade({@link CrBranch}) e seus DTOs de entrada({@link CrBranchRequest}) e saída({@link CrBranchResponse}).
 */
@Component
public class CrBranchMapper {

    /**
     * Converte um DTO de entrada do CR-filial em uma entidade CR-filial.
     * @param branch dados da filial.
     * @param cr dados do CR.
     * @param responsibleUsers usuários responsáveis.
     * @return dados convertidos para uma entidade.
     */
    public CrBranch toEntity(Branch branch, Cr cr, List<User> responsibleUsers) {
        return new CrBranch(
                branch,
                cr,
                responsibleUsers
        );
    }

    /**
     * Converte uma entidade CR-filial em um DTO de saída do CR-filial.
     * @param crBranch entidade com os dados do CR-filial.
     * @return dados convertidos para um DTO de saída.
     */
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

    /**
     * Converte uma lista de entidades CR-filial em uma lista de DTOs de saída do CR-filial.
     * @param crBranches lista de entidades com os dados do CR-filial.
     * @return dados convertido para uma lista de DTOs de saída.
     */
    public List<CrBranchResponse> toResponseList(List<CrBranch> crBranches){
        return crBranches
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
