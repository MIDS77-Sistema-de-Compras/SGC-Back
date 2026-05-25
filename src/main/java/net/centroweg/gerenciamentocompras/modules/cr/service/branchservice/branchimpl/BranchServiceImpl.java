package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchinterface.BranchService;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final CreateBranch createBranch;
    private final findAllBranch listAllBranch;
    private final UpdateBranch updateBranch;
    private final FindById findById;
    private final DeleteBranch deleteBranch;

    @Override
    public BranchResponse create(BranchRequest branchRequest){
        return createBranch.create(branchRequest);
    }

    @Override
    public List<BranchResponse> findAll(){
        return listAllBranch.listAll();
    }

    @Override
    public BranchResponse update(long id, BranchRequest branchRequest){
        return updateBranch.update(id, branchRequest);
    }

    @Override
    public BranchResponse findById(long id){
        return findById.findById(id);
    }

    @Override
    public MessageDTO delete(long id){
        deleteBranch.delete(id);
        return new MessageDTO("Deletado");
    }
}
