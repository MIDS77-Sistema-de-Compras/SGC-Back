package net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;

import java.util.List;

/**
 * Interface de serviço para operações de gerenciamento de {@link Branch}.
 */
public interface BranchService {

    /**
     * Cria uma nova branch.
     * @param branchRequest dados da branch a ser criada.
     * @return a branch criada.
     */
    BranchResponse create(BranchRequest branchRequest);

    /**
     * Lista todas as branches cadastradas.
     * @return lista de branches, vazia se não houver nenhuma.
     */
    List<BranchResponse> findAll();

    /**
     * Atualiza os dados de uma branch existente.
     * @param id identificador da branch a ser atualizada.
     * @param branchRequest novos dados da branch.
     * @return a branch com os dados atualizados.
     * @throws BranchNotFoundException se o ID não for encontrado.
     */
    BranchResponse update(Long id, BranchRequest branchRequest);

    /**
     * Busca uma branch pelo seu identificador.
     * @param id identificador da branch a ser pesquisada.
     * @return a branch encontrada.
     * @throws BranchNotFoundException se o ID não for encontrado.
     */
    BranchResponse findById(Long id);

    /**
     * Remove uma branch pelo seu identificador.
     * @param id identificador da branch a ser removida.
     * @return mensagem de confirmação da operação.
     */
    MessageDTO delete(Long id);
}
