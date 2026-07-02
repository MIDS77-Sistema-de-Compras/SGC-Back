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
     * Componente responsável pela listagem de todas as filiais.
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
     * Componente responsável por deletar uma filial.
     */
    private final DeleteBranch deleteBranch;

    /**
     * Cria uma nova filial com base nos dados passados.
     * @param branchRequest dados da filial a ser criada.
     * @return a nova filial criada.
     */
    @Override
    public BranchResponse create(BranchRequest branchRequest){
        return createBranch.create(branchRequest);
    }

    /**
     * Lista todas as filiais que foram criadas e não foram excluídas.
     * @return lista com todas as filiais encontradas.
     */
    @Override
    public List<BranchResponse> findAll(){
        return listAllBranch.listAll();
    }

    /**
     * Atualiza a filial que corresponde ao ID informado com os dados que foram passados.
     * @param id identificador da filial a ser atualizada.
     * @param branchRequest novos dados da filial.
     * @return a filial atualizada.
     */
    @Override
    public BranchResponse update(Long id, BranchRequest branchRequest){
        return updateBranch.update(id, branchRequest);
    }

    /**
     * Busca uma filial pelo ID informado.
     * @param id identificador da filial.
     * @return a filial encontrada, caso existir, se não retorna uma excessão.
     */
    @Override
    public BranchResponse findById(Long id){
        return findById.findById(id);
    }

    /**
     * Deleta a filial que corresponde ao ID informado.
     * @param id identificador da filial a ser removida.
     * @return mensagem de sucesso caso não ocorra erros durante o processo.
     */
    @Override
    public MessageDTO delete(Long id){
        deleteBranch.delete(id);
        return new MessageDTO("Deletado");
    }
}
