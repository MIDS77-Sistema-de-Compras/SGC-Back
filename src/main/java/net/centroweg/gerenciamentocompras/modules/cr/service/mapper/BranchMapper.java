package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pela conversão entre a entidade({@link Branch}) e seus DTOs de entrada({@link BranchRequest}) e saída({@link BranchResponse}).
 */
@Component
public class BranchMapper {

    /**
     * Converte um DTO de entrada da filial em uma entidade filial.
     * @param branchRequest DTO com os dados de entrada da filial.
     * @return conversão dos dados para uma entidade filial.
     */
    public Branch toEntity(BranchRequest branchRequest){
        return new Branch(
                branchRequest.name()
        );
    }

    /**
     * Converte uma entidade filial em um DTO de saída da filial.
     * @param branch entidade com os dados.
     * @return DTO de saída com os dados da filial já convertidos.
     */
    public BranchResponse toResponse(Branch branch){
        return new BranchResponse(
                branch.getId(),
                branch.getName()
        );
    }
}
