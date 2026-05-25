package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    public Branch toEntity(BranchRequest branchRequest){
        return new Branch(
                branchRequest.name()
        );
    }

    public BranchResponse toResponse(Branch branch){
        return new BranchResponse(
                branch.getId(),
                branch.getName()
        );
    }
}
