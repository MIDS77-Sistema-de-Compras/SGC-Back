package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

import java.util.List;

/**
 * Interface de serviço para operações de gerenciamento de vínculo {@link CrBranch}.
 */
public interface CrBranchService {

    /**
     * Cria um novo CR-Branch.
     * @param request dados do CR-Branch a ser criado.
     * @return CR-Branch já criado.
     */
    CrBranchResponse create(CrBranchRequest request);

    /**
     * Listagem de todos os CR-Branches que correspondem a pesquisa.
     * @param filter parâmetro para realizar alguma pesquisa específica.
     * @return lista com os CR-Branches encontrados da pesquisa, caso não tenha parâmetro, vai listar todos.
     */
    List<CrBranchResponse> findAll(CrBranchFilterRequest filter);

    /**
     * Busca um CR-Branch pelo seu ID.
     * @param id identificador do CR-Branch.
     * @return CR-Branch que foi encontrado na pesquisa.
     */
    CrBranchResponse findById(Long id);

    /**
     * Atualiza um CR-Branch existente.
     * @param id identificador do CR-Branch a ser atualizado.
     * @param request novos dados do CR-Branch.
     * @return CR-Branch com os dados já atualizados.
     */
    CrBranchResponse update(Long id, CrBranchRequest request);

    /**
     * Deleta um CR-Branch no banco de dados.
     * @param id identificador do CR-Branch a ser excluído.
     * @return mensagem de sucesso se tudo ocorrer certo.
     */
    MessageDTO delete(Long id);

    /**
     * Lista os CR-Branches que pertencem a uma branch.
     * @param branchId identificador da branch.
     * @return lista com os CR-Branches encontrados, vazio caso não tenha nenhum.
     */
    List<CrBranchResponse> findCrBranchByBranch(Long branchId);

    /**
     * Adicionar um responsável a um CR-Branch.
     * @param crBranchId identificador do CR-Branch que vai receber um usuário.
     * @param userId identificador do usuário que será atribuído a um CR-Branch.
     * @return CR-Branch atualizado com a atribuição.
     */
    CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId);

    /**
     * Remove um responsável de um CR-Branch.
     * @param crBranchId identificador do CR-Branch.
     * @return CR-Branch atualizado.
     */
    CrBranchResponse removeCrBranchResponsible(Long crBranchId);
}
