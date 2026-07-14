package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchinterface.BranchService;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import java.util.List;

/**
 * Classe de serviço da {@link Branch} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link BranchService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    /**
     * Componente responsável pela criação de uma filial.
     */
    private final CreateBranch createBranch;

    /**
     * Componente responsável pela listagem de todas as filiais cadastradas.
     */
    private final findAllBranch listAllBranch;

    /**
     * Componente responsável por atualizar uma filial.
     */
    private final UpdateBranch updateBranch;

    /**
     * Componente responsável por buscar uma filial pelo ID.
     */
    private final FindByIdBranch findById;

    /**
     * Componente responsável por remover uma filial.
     */
    private final DeleteBranch deleteBranch;

    /**
     * Cria e persiste uma nova filial no banco de dados.
     * @param branchRequest dados da filial.
     * @return filial criada.
     */
    @Override
    public BranchResponse create(BranchRequest branchRequest){
        return createBranch.create(branchRequest);
    }

    /**
     * Lista todas as filiais cadastradas no banco de dados.
     * @return lista com todas as filiais encontradas.
     */
    @Override
    public List<BranchResponse> findAll(){
        return listAllBranch.listAll();
    }

    /**
     * Atualiza uma filial existente no banco de dados.
     * @param id identificador da filial.
     * @param branchRequest novos dados da filial.
     * @return filial já atualizada.
     */
    @Override
    public BranchResponse update(Long id, BranchRequest branchRequest){
        return updateBranch.update(id, branchRequest);
    }

    /**
     * Busca uma filial no banco de dados pelo ID informado.
     * @param id identificador da filial.
     * @return filial encontrada, caso exista.
     */
    @Override
    public BranchResponse findById(Long id){
        return findById.findById(id);
    }

    /**
     * Remove uma filial do banco de dados.
     * @param id identificador da filial.
     * @return mensagem de sucesso da remoção.
     */
    @Override
    public MessageDTO delete(Long id){
        deleteBranch.delete(id);
        return new MessageDTO("Deletado!");
    }
}
