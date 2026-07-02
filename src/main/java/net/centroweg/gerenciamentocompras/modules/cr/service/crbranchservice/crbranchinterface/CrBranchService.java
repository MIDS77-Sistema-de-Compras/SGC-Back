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
     * Cria um novo CR-filial.
     * @param request dados do CR-filial a ser criado.
     * @return CR-filial já criado.
     */
    CrBranchResponse create(CrBranchRequest request);

    /**
     * Listagem de todos os CR-filiais que correspondem a pesquisa.
     * @param filter parâmetro para realizar alguma pesquisa específica.
     * @return lista com os CR-filiais encontrados da pesquisa, caso não tenha parâmetro, vai listar todos.
     */
    List<CrBranchResponse> findAll(CrBranchFilterRequest filter);

    /**
     * Busca um CR-filial pelo seu ID.
     * @param id identificador do CR-filial.
     * @return CR-filial que foi encontrado na pesquisa.
     */
    CrBranchResponse findById(Long id);

    /**
     * Atualiza um CR-filial existente.
     * @param id identificador do CR-filial a ser atualizado.
     * @param request novos dados do CR-filial.
     * @return CR-filial com os dados já atualizados.
     */
    CrBranchResponse update(Long id, CrBranchRequest request);

    /**
     * Deleta um CR-filial no banco de dados.
     * @param id identificador do CR-filial a ser excluído.
     * @return mensagem de sucesso se tudo ocorrer certo.
     */
    MessageDTO delete(Long id);

    /**
     * Lista os CR-filiais que pertencem a uma filial.
     * @param branchId identificador da filial.
     * @return lista com os CR-filiais encontrados, vazio caso não tenha nenhum.
     */
    List<CrBranchResponse> findCrBranchByBranch(Long branchId);

    /**
     * Adicionar um responsável a um CR-filial.
     * @param crBranchId identificador do CR-filial que vai receber um usuário.
     * @param userId identificador do usuário que será atribuído a um CR-filial.
     * @return CR-filial atualizado com a atribuição.
     */
    CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId);

    /**
     * Remove um responsável de um CR-filial.
     * @param crBranchId identificador do CR-filial.
     * @return CR-filial atualizado.
     */
    CrBranchResponse removeCrBranchResponsible(Long crBranchId);
}
