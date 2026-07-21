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
 * Interface de serviço para operações de gerenciamento do vínculo {@link CrBranch}.
 */
public interface CrBranchService {

    /**
     * Cria e persiste um novo vínculo CR-filial no banco de dados.
     * @param request dados do vínculo.
     * @return vínculo criado.
     * @throws BranchNotFoundException se a filial não for encontrada.
     * @throws CrNotFoundException se o CR não for encontrado.
     * @throws CrBranchAlreadyExistsException se já existir um vínculo entre o CR e a filial informados.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    CrBranchResponse create(CrBranchRequest request);

    /**
     * Lista todos os vínculos CR-filiais correspondentes a pesquisa cadastrados no banco de dados.
     * @param filter parâmetro para realizar alguma pesquisa específica.
     * @return lista com todos os vínculos CR-filiais encontrados, caso exista.
     */
    List<CrBranchResponse> findAll(CrBranchFilterRequest filter);

    /**
     * Busca um vínculo CR-filial no banco de dados pelo ID informado.
     * @param id identificador do vínculo.
     * @return vínculo encontrado, caso exista.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     */
    CrBranchResponse findById(Long id);

    /**
     * Atualiza um vínculo CR-filial existente no banco de dados.
     * @param id identificador do vínculo.
     * @param request novos dados do vínculo.
     * @return vínculo já atualizado.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     * @throws BranchNotFoundException se a filial não for encontrada.
     * @throws CrNotFoundException se o CR não for encontrado.
     * @throws UsernameNotFoundException se o responsável informado não for encontrado.
     */
    CrBranchResponse update(Long id, CrBranchRequest request);

    /**
     * Remove um vínculo CR-filial do banco de dados.
     * @param id identificador do vínculo.
     * @return mensagem de sucesso da remoção.
     */
    MessageDTO delete(Long id);

    /**
     * Lista todos os CR-filiais que pertencem a uma filial cadastrados no banco de dados.
     * @param branchId identificador da filial.
     * @return lista com todos os vínculos encontrados, caso exista.
     * @throws BranchNotFoundException se a filial não for encontrada.
     */
    List<CrBranchResponse> findCrBranchByBranch(Long branchId);

    /**
     * Atribui um usuário responsável a um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do vínculo.
     * @param userId identificador do usuário.
     * @return vínculo já atualizado.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId);

    /**
     * Remove um usuário responsável de um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do CR-filial.
     * @return CR-filial já atualizado.
     * @throws CrBranchNotFoundException se o vínculo não for encontrado.
     */
    CrBranchResponse removeCrBranchResponsible(Long crBranchId);
}
