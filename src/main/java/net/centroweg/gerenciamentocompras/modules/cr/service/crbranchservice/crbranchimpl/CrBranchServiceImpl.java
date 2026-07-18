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
     * Componente responsável pela criação de um CR-filial.
     */
    private final CreateCrBranch createCrBranch;

    /**
     * Componente responsável pela listagem de CR-filiais.
     */
    private final FindAllCrBranch findAllCrBranch;

    /**
     * Componente responsável por buscar um CR-filial pelo ID informado.
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
     * Componente responsável por buscar um CR-filial pela filial informada.
     */
    private final FindCrBranchByBranch findCrBranchByBranch;

    /**
     * Componente responsável por adicionar um ou mais responsáveis no CR-filial.
     */
    private final AssignCrBranchResponsible assignCrBranchResponsible;

    /**
     * Componente responsável por remover um ou mais responsáveis no CR-filial.
     */
    private final RemoveCrBranchResponsible removeCrBranchResponsible;

    /**
     * Cria e persistir um novo vínculo entre CR e filial no banco de dados.
     * @param request dados do CR-filial.
     * @return vínculo criado.
     */
    @Override
    public CrBranchResponse create(CrBranchRequest request) {
        return createCrBranch.create(request);
    }

    /**
     * Lista todos os vínculos CR-filial cadastrados no banco de dados.
     * @return lista todos os vínculos encontrados, caso exista.
     */
    @Override
    public List<CrBranchResponse> findAll(CrBranchFilterRequest filter) {
        return findAllCrBranch.findAll(filter);
    }

    /**
     * Busca um vínculo CR-filial no banco de dados pelo ID informado.
     * @param id identificador do CR-filial.
     * @return vínculo encontrado, caso exista.
     */
    @Override
    public CrBranchResponse findById(Long id) {
        return findByIdCrBranch.findById(id);
    }

    /**
     * Atualiza um vínculo CR-filial existente no banco de dados.
     * @param id identificador do CR-filial.
     * @param request novos dados do CR-filial.
     * @return vínculo já atualizado.
     */
    @Override
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        return updateCrBranch.update(id, request);
    }

    /**
     * Remove um vínculo CR-filial do banco de dados.
     * @param id identificador do CR-filial.
     * @return uma mensagem de confirmação da remoção.
     */
    @Override
    public MessageDTO delete(Long id) {
        deleteCrBranch.delete(id);
        return new MessageDTO("Deletado!");
    }

    /**
     * Lista todos os vínculos CR-filial pertencentes a uma filial cadastrados no banco de dados.
     * @param branchId identificador do CR-filial.
     * @return lista todos os vínculos encontrados, caso exista.
     */
    @Override
    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        return findCrBranchByBranch.findCrBranchByBranch(branchId);
    }

    /**
     * Atribui um usuário responsável a um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do CR-filial.
     * @param userId identificador do usuário.
     * @return vínculo já atualizado.
     */
    @Override
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId) {
        return assignCrBranchResponsible.assignCrBranchResponsible(crBranchId, List.of(userId));
    }

    /**
     * Remove um usuário responsável de um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do CR-filial.
     * @return vínculo já atualizado.
     */
    @Override
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        return removeCrBranchResponsible.removeCrBranchResponsible(crBranchId);
    }

}