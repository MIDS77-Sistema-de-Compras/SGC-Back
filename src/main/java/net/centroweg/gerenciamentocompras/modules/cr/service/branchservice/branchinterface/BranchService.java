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
     * Cria e persiste uma nova filial no banco de dados.
     * @param branchRequest dados da filial.
     * @return filial criada.
     */
    BranchResponse create(BranchRequest branchRequest);

    /**
     * Lista todas as filiais cadastradas no banco de dados.
     * @return lista com todas as filiais encontradas, caso exista.
     */
    List<BranchResponse> findAll();

    /**
     * Atualiza uma filial existente no banco de dados.
     * @param id identificador da filial.
     * @param branchRequest novos dados da filial.
     * @return filial já atualizada.
     * @throws BranchNotFoundException se a filial não for encontrada.
     */
    BranchResponse update(Long id, BranchRequest branchRequest);

    /**
     * Busca uma filial no banco de dados pelo ID informado.
     * @param id identificador da filial.
     * @return filial encontrada, caso exista.
     * @throws BranchNotFoundException se a filial não for encontrada.
     */
    BranchResponse findById(Long id);

    /**
     * Remove uma filial do banco de dados.
     * @param id identificador da filial.
     * @return mensagem de confirmação da remoção.
     */
    MessageDTO delete(Long id);
}
