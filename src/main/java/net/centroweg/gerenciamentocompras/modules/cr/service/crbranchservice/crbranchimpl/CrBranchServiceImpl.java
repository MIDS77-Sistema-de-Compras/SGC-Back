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
     * Componente responsável pela criação de CR-filial.
     */
    private final CreateCrBranch createCrBranch;

    /**
     * Componente responsável pela listagem de CR-filial.
     */
    private final FindAllCrBranch findAllCrBranch;

    /**
     * Componente responsável pela busca por ID do CR-filial.
     */
    private final FindByIdCrBranch findByIdCrBranch;

    /**
     * Componente responsável pela atualização do CR-filial.
     */
    private final UpdateCrBranch updateCrBranch;

    /**
     * Componente responsável por deletar um CR-filial.
     */
    private final DeleteCrBranch deleteCrBranch;

    /**
     * Componente responsável por buscar um CR-filial pelo ID da filial.
     */
    private final FindCrBranchByBranch findCrBranchByBranch;

    /**
     * Componente responsável por adicionar um ou mais responsáveis no CR-filial.
     */
    private final AssignCrBranchResponsible assignCrBranchResponsible;

    /**
     * Componente responsável por remover um ou mais responsáves no CR-filial.
     */
    private final RemoveCrBranchResponsible removeCrBranchResponsible;

    /**
     * Cria um novo vínculo entre CR e filial.
     * @param request dados do CR-filial para criação.
     * @return o vínculo criado.
     */
    @Override
    public CrBranchResponse create(CrBranchRequest request) {
        return createCrBranch.create(request);
    }

    /**
     * Lista todos os vínculos CR-filial cadastrados.
     * @return a lista de vínculos encontrados.
     */
    @Override
    public List<CrBranchResponse> findAll(CrBranchFilterRequest filter) {
        return findAllCrBranch.findAll(filter);
    }

    /**
     * Busca um vínculo CR-filial pelo seu identificador.
     * @param id identificador do CR-filial.
     * @return o vínculo encontrado.
     */
    @Override
    public CrBranchResponse findById(Long id) {
        return findByIdCrBranch.findById(id);
    }

    /**
     * Atualiza um vínculo CR-filial existente.
     * @param id identificador do CR-filial.
     * @param request novos dados do CR-filial.
     * @return o vínculo atualizado.
     */
    @Override
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        return updateCrBranch.update(id, request);
    }

    /**
     * Remove um vínculo CR-filial pelo seu identificador.
     * @param id identificador do CR-filial.
     * @return uma mensagem de confirmação da remoção.
     */
    @Override
    public MessageDTO delete(Long id) {
        deleteCrBranch.delete(id);
        return new MessageDTO("Deletado.");
    }

    /**
     * Lista todos os vínculos CR-filial pertencentes a uma filial.
     * @param branchId identificador do CR-filial.
     * @return a lista de vínculos da filial.
     */
    @Override
    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        return findCrBranchByBranch.findCrBranchByBranch(branchId);
    }

    /**
     * Atribui um usuário responsável a um vínculo CR-filial.
     * @param crBranchId identificador do CR-filial que receberá um usuário.
     * @param userId identificador do usuário que será atribuido ao CR-filial.
     * @return o vínculo atualizado.
     */
    @Override
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId) {
        return assignCrBranchResponsible.assignCrBranchResponsible(crBranchId, List.of(userId));
    }

    /**
     * Remove o usuário responsável de um vínculo CR-filial.
     * @param crBranchId identificador do CR-filial que terá um usuário removido.
     * @return o vínculo atualizado sem responsável.
     */
    @Override
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        return removeCrBranchResponsible.removeCrBranchResponsible(crBranchId);
    }

}