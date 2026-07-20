package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface.CrBranchService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação de {@link CrBranchService}.
 *
 * <p>Atua como orquestrador: cada operação é delegada a uma classe de funcionalidade
 * específica, mantendo a responsabilidade de cada caso de uso isolada.</p>
 */
@Service
@RequiredArgsConstructor
public class CrBranchServiceImpl implements CrBranchService {

    private final CreateCrBranch createCrBranch;
    private final FindAllCrBranch findAllCrBranch;
    private final FindByIdCrBranch findByIdCrBranch;
    private final UpdateCrBranch updateCrBranch;
    private final DeleteCrBranch deleteCrBranch;
    private final FindCrBranchByBranch findCrBranchByBranch;
    private final AssignCrBranchResponsible assignCrBranchResponsible;
    private final RemoveCrBranchResponsible removeCrBranchResponsible;

    /**
     * Cria um novo vínculo entre CR e filial.
     *
     * @param request
     * @return o vínculo criado
     */
    @Override
    public CrBranchResponse create(CrBranchRequest request) {
        return createCrBranch.create(request);
    }

    /**
     * Lista todos os vínculos CR-filial cadastrados.
     *
     * @return a página de vínculos
     */
    @Override
    public Page<CrBranchResponse> findAll(CrBranchFilterRequest filter, Pageable pageable) {
        return findAllCrBranch.findAll(filter, pageable);
    }

    /**
     * Busca um vínculo CR-filial pelo seu identificador.
     *
     * @param id
     * @return o vínculo encontrado
     */
    @Override
    public CrBranchResponse findById(Long id) {
        return findByIdCrBranch.findById(id);
    }

    /**
     * Atualiza um vínculo CR-filial existente.
     *
     * @param id
     * @param request
     * @return o vínculo atualizado
     */
    @Override
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        return updateCrBranch.update(id, request);
    }

    /**
     * Remove um vínculo CR-filial pelo seu identificador.
     *
     * @param id
     * @return uma mensagem de confirmação da remoção
     */
    @Override
    public MessageDTO delete(Long id) {
        deleteCrBranch.delete(id);
        return new MessageDTO("Deletado.");
    }

    /**
     * Lista todos os vínculos CR-filial pertencentes a uma filial.
     *
     * @param branchId
     * @return a lista de vínculos da filial
     */
    @Override
    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        return findCrBranchByBranch.findCrBranchByBranch(branchId);
    }

    /**
     * Adiciona um usuário responsável a um vínculo CR-filial.
     *
     * @param crBranchId
     * @param userId
     * @return o vínculo atualizado
     */
    @Override
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId) {
        return assignCrBranchResponsible.assignCrBranchResponsible(crBranchId, userId);
    }

    /**
     * Remove um usuário responsável de um vínculo CR-filial.
     *
     * @param crBranchId
     * @param userId
     * @return o vínculo atualizado sem o responsável removido
     */
    @Override
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId, Long userId) {
        return removeCrBranchResponsible.removeCrBranchResponsible(crBranchId, userId);
    }

}