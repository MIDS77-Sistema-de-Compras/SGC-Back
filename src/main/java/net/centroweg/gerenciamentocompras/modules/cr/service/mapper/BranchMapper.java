package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pela conversão entre a entidade {@link Branch}
 * e seus DTOs de entrada ({@link BranchRequest}) e saída ({@link BranchResponse}).
 *
 * @author Leandro
 */
@Component
public class BranchMapper {

    /**
     * Converte um {@link BranchRequest} em uma entidade {@link Branch}.
     *
     * @param branchRequest DTO com os dados de entrada
     * @return entidade {@link Branch} pronta para persistência
     */
    public Branch toEntity(BranchRequest branchRequest){
        return new Branch(
                branchRequest.name()
        );
    }

    /**
     * Converte uma entidade {@link Branch} em um {@link BranchResponse}.
     *
     * @param branch entidade persistida
     * @return DTO de saída com os dados da branch
     */
    public BranchResponse toResponse(Branch branch){
        return new BranchResponse(
                branch.getId(),
                branch.getName()
        );
    }
}
