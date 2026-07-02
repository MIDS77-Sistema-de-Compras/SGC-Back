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
     * Cria uma nova filial.
     * @param branchRequest dados da filial a ser criada.
     * @return a filial criada.
     */
    BranchResponse create(BranchRequest branchRequest);

    /**
     * Lista todas as filiais cadastradas.
     * @return lista de filiais, vazia se não houver nenhuma.
     */
    List<BranchResponse> findAll();

    /**
     * Atualiza os dados de uma filial existente.
     * @param id identificador da filial a ser atualizada.
     * @param branchRequest novos dados da filial.
     * @return a filial com os dados atualizados.
     * @throws BranchNotFoundException se o ID não for encontrado.
     */
    BranchResponse update(Long id, BranchRequest branchRequest);

    /**
     * Busca uma filial pelo seu identificador.
     * @param id identificador da filial a ser pesquisada.
     * @return a filial encontrada.
     * @throws BranchNotFoundException se o ID não for encontrado.
     */
    BranchResponse findById(Long id);

    /**
     * Remove uma filial pelo seu identificador.
     * @param id identificador da filial a ser removida.
     * @return mensagem de confirmação da operação.
     */
    MessageDTO delete(Long id);
}
