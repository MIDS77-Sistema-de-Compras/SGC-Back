package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.BranchMapper;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBranch {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public MessageDTO delete(long id){
        branchRepository.deleteById(id);
        return new MessageDTO("Error");
    }
}
