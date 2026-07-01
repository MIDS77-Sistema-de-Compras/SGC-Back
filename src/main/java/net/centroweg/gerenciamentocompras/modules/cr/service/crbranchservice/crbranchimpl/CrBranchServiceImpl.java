package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface.CrBranchService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

import java.util.List;

/**
 *  Classe de serviço da {@link CrBranch} que delega cada operação à sua respectiva classe de funcionalidade.
 *  Implementa {@link CrBranchService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class CrBranchServiceImpl implements CrBranchService {

    /**
     * Componente responsável pela criação de {@link CrBranch}.
     */
    private final CreateCrBranch createCrBranch;

    /**
     * Componente responsável pela listagem de {@link CrBranch}.
     */
    private final FindAllCrBranch findAllCrBranch;

    /**
     * Componente responsável pela busca por ID do {@link CrBranch}.
     */
    private final FindByIdCrBranch findByIdCrBranch;

    /**
     * Componente responsável pela atualização do {@link CrBranch}.
     */
    private final UpdateCrBranch updateCrBranch;

    /**
     * Componente responsável por deletar um {@link CrBranch}.
     */
    private final DeleteCrBranch deleteCrBranch;

    /**
     * Componente responsável por buscar um {@link CrBranch} pelo ID da branch.
     */
    private final FindCrBranchByBranch findCrBranchByBranch;

    /**
     * Componente responsável por adicionar um ou mais responsáveis no {@link CrBranch}.
     */
    private final AssignCrBranchResponsible assignCrBranchResponsible;

    /**
     * Componente responsável por remover um ou mais responsáves no {@link CrBranch}.
     */
    private final RemoveCrBranchResponsible removeCrBranchResponsible;

    /**
     * Cria um novo vínculo entre CR e branch.
     * @param request dados do CR-Branch para criação.
     * @return o vínculo criado.
     */
    @Override
    public CrBranchResponse create(CrBranchRequest request) {
        return createCrBranch.create(request);
    }

    /**
     * Lista todos os vínculos CR-Branch cadastrados.
     * @return a lista de vínculos encontrados.
     */
    @Override
    public List<CrBranchResponse> findAll(CrBranchFilterRequest filter) {
        return findAllCrBranch.findAll(filter);
    }

    /**
     * Busca um vínculo CR-Branch pelo seu identificador.
     * @param id identificador do CR-Branch.
     * @return o vínculo encontrado.
     */
    @Override
    public CrBranchResponse findById(Long id) {
        return findByIdCrBranch.findById(id);
    }

    /**
     * Atualiza um vínculo CR-Branch existente.
     * @param id identificador do CR-Branch.
     * @param request novos dados do CR-Branch.
     * @return o vínculo atualizado.
     */
    @Override
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        return updateCrBranch.update(id, request);
    }

    /**
     * Remove um vínculo CR-Branch pelo seu identificador.
     * @param id identificador do CR-Branch.
     * @return uma mensagem de confirmação da remoção.
     */
    @Override
    public MessageDTO delete(Long id) {
        deleteCrBranch.delete(id);
        return new MessageDTO("Deletado.");
    }

    /**
     * Lista todos os vínculos CR-Branch pertencentes a uma branch.
     * @param branchId identificador do CR-Branch.
     * @return a lista de vínculos da branch.
     */
    @Override
    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        return findCrBranchByBranch.findCrBranchByBranch(branchId);
    }

    /**
     * Atribui um usuário responsável a um vínculo CR-Branch.
     * @param crBranchId identificador do CR-Branch que receberá um usuário.
     * @param userId identificador do usuário que será atribuido a CR-Branch.
     * @return o vínculo atualizado.
     */
    @Override
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId) {
        return assignCrBranchResponsible.assignCrBranchResponsible(crBranchId, List.of(userId));
    }

    /**
     * Remove o usuário responsável de um vínculo CR-Branch.
     * @param crBranchId identificador do CR-Branch que terá um usuário removido.
     * @return o vínculo atualizado sem responsável.
     */
    @Override
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        return removeCrBranchResponsible.removeCrBranchResponsible(crBranchId);
    }

}