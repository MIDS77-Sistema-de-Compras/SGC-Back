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
     * Converte um DTO de entrada de um CR-filial em uma entidade CR-filial.
     * @param branch dados da filial.
     * @param cr dados do CR.
     * @param responsibleUsers usuários responsáveis.
     * @return conversão dos dados para uma entidade CR-filial.
     */
    public CrBranch toEntity(Branch branch, Cr cr, List<User> responsibleUsers) {
        return new CrBranch(
                branch,
                cr,
                responsibleUsers
        );
    }

    /**
     * Converte uma entidade CR-filial para um DTO de saída do CR-filial.
     * @param crBranch entidade com os dados.
     * @return DTO de saída com os dados do CR-filial já convertidos.
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
     * Converte uma lista de entidades do CR-filial em uma lista de DTOs de saída do CR-filial.
     * @param crBranches lista com as entidades.
     * @return lista de DTOs de saída com os CR-filiais já convertidos.
     */
    public List<CrBranchResponse> toResponseList(List<CrBranch> crBranches){
        return crBranches
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
