package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.BranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

/**
 * Interface de serviço para operações de gerenciamento de vínculo {@link CrBranch}.
 */
public interface CrBranchService {

    /**
     * Cria e persiste um novo CR-filial no banco de dados.
     * @param request dados do CR-filial.
     * @return CR-filial criado.
     * @throws BranchNotFoundException se a filial não for encontrada.
     * @throws CrNotFoundException se o CR não for encontrado.
     * @throws CrBranchAlreadyExistsException se já existir um vínculo entre o CR e a filial.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    CrBranchResponse create(CrBranchRequest request);

    /**
     * Listagem de todos os CR-filiais correspondentes a pesquisa cadastrados no banco de dados.
     * @param filter parâmetro para realizar alguma pesquisa específica.
     * @return lista com os CR-filiais encontrados, caso não tenha parâmetro, vai listar todos.
     */
    List<CrBranchResponse> findAll(CrBranchFilterRequest filter);

    /**
     * Busca um CR-filial no banco de dados pelo ID informado.
     * @param id identificador do CR-filial.
     * @return CR-filial encontrado, caso exista.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     */
    CrBranchResponse findById(Long id);

    /**
     * Atualiza um CR-filial existente no banco de dados.
     * @param id identificador do CR-filial.
     * @param request novos dados do CR-filial.
     * @return CR-filial já atualizado.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     * @throws BranchNotFoundException se a filial não for encontrada.
     * @throws CrNotFoundException se o CR não for encontrado.
     * @throws UsernameNotFoundException se o responsável informado não for encontrado.
     */
    CrBranchResponse update(Long id, CrBranchRequest request);

    /**
     * Remove um CR-filial do banco de dados.
     * @param id identificador do CR-filial.
     * @return mensagem de sucesso da remoção.
     */
    MessageDTO delete(Long id);

    /**
     * Lista todos os CR-filiais que pertencem a uma filial cadastrados no banco de dados.
     * @param branchId identificador da filial.
     * @return lista com os CR-filiais encontrados, caso exista.
     * @throws BranchNotFoundException se a filial não for encontrada.
     */
    List<CrBranchResponse> findCrBranchByBranch(Long branchId);

    /**
     * Atribuir um usuário responsável a um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do CR-filial.
     * @param userId identificador do usuário.
     * @return CR-filial atualizado.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId);

    /**
     * Remove um usuário responsável de um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do CR-filial.
     * @return CR-filial atualizado sem o usuário.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     */
    CrBranchResponse removeCrBranchResponsible(Long crBranchId);
}
