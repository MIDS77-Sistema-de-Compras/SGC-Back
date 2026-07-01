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
     * Componente responsável pela criação de uma {@link Branch}.
     */
    private final CreateBranch createBranch;

    /**
     * Componente responsável pela listagem de todas as {@link Branch}.
     */
    private final findAllBranch listAllBranch;

    /**
     * Componente responsável por atualizar uma {@link Branch}.
     */
    private final UpdateBranch updateBranch;

    /**
     * Componente responsável por buscar uma {@link Branch} pelo ID.
     */
    private final FindByIdBranch findById;

    /**
     * Componente responsável por deletar uma {@link Branch}.
     */
    private final DeleteBranch deleteBranch;

    /**
     * Cria uma nova branch com base nos dados passados.
     * @param branchRequest dados da branch a ser criada.
     * @return a nova branch criada.
     */
    @Override
    public BranchResponse create(BranchRequest branchRequest){
        return createBranch.create(branchRequest);
    }

    /**
     * Lista todas as branches que foram criadas e não foram excluídas.
     * @return lista com todas as branches encontradas.
     */
    @Override
    public List<BranchResponse> findAll(){
        return listAllBranch.listAll();
    }

    /**
     * Atualiza a branch que corresponde ao ID informado com os dados que foram passados.
     * @param id identificador da branch a ser atualizada.
     * @param branchRequest novos dados da branch.
     * @return a branch atualizada.
     */
    @Override
    public BranchResponse update(Long id, BranchRequest branchRequest){
        return updateBranch.update(id, branchRequest);
    }

    /**
     * Busca uma branch pelo ID informado.
     * @param id identificador da branch.
     * @return a branch encontrada, caso existir, se não retorna uma excessão.
     */
    @Override
    public BranchResponse findById(Long id){
        return findById.findById(id);
    }

    /**
     * Deleta a branch que corresponde ao ID informado.
     * @param id identificador da branch a ser removida.
     * @return mensagem de sucesso caso não ocorra erros durante o processo.
     */
    @Override
    public MessageDTO delete(Long id){
        deleteBranch.delete(id);
        return new MessageDTO("Deletado");
    }
}
