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
     * Componente responsável pela criação de um vínculo CR-filial.
     */
    private final CreateCrBranch createCrBranch;

    /**
     * Componente responsável pela listagem de todos os vínculos CR-filiais cadastrados.
     */
    private final FindAllCrBranch findAllCrBranch;

    /**
     * Componente responsável por buscar um vínculo CR-filial pelo ID informado.
     */
    private final FindByIdCrBranch findByIdCrBranch;

    /**
     * Componente responsável por atualizar um vínculo CR-filial.
     */
    private final UpdateCrBranch updateCrBranch;

    /**
     * Componente responsável por remover um vínculo CR-filial.
     */
    private final DeleteCrBranch deleteCrBranch;

    /**
     * Componente responsável por buscar um vínculo CR-filial pela filial informada.
     */
    private final FindCrBranchByBranch findCrBranchByBranch;

    /**
     * Componente responsável por adicionar um ou mais responsáveis no vínculo CR-filial.
     */
    private final AssignCrBranchResponsible assignCrBranchResponsible;

    /**
     * Componente responsável por remover um ou mais responsáveis do vínculo CR-filial.
     */
    private final RemoveCrBranchResponsible removeCrBranchResponsible;

    /**
     * Cria e persiste um novo vínculo CR-filial no banco de dados.
     * @param request dados do vínculo.
     * @return vínculo criado.
     */
    @Override
    public CrBranchResponse create(CrBranchRequest request) {
        return createCrBranch.create(request);
    }

    /**
     * Lista todos os vínculos CR-filial cadastrados no banco de dados.
     * @return lista com todos os vínculos encontrados, caso exista.
     */
    @Override
    public List<CrBranchResponse> findAll(CrBranchFilterRequest filter) {
        return findAllCrBranch.findAll(filter);
    }

    /**
     * Busca um vínculo CR-filial no banco de dados pelo ID informado.
     * @param id identificador do vínculo.
     * @return vínculo encontrado, caso exista.
     */
    @Override
    public CrBranchResponse findById(Long id) {
        return findByIdCrBranch.findById(id);
    }

    /**
     * Atualiza um vínculo CR-filial existente no banco de dados.
     * @param id identificador do vínculo.
     * @param request novos dados do vínculo.
     * @return vínculo já atualizado.
     */
    @Override
    public CrBranchResponse update(Long id, CrBranchRequest request) {
        return updateCrBranch.update(id, request);
    }

    /**
     * Remove um vínculo CR-filial do banco de dados.
     * @param id identificador do vínculo.
     * @return mensagem de confirmação da remoção.
     */
    @Override
    public MessageDTO delete(Long id) {
        deleteCrBranch.delete(id);
        return new MessageDTO("Deletado!");
    }

    /**
     * Lista todos os vínculos CR-filial pertencentes a uma filial cadastrados no banco de dados.
     * @param branchId identificador do vínculo.
     * @return lista com todos os vínculos encontrados, caso exista.
     */
    @Override
    public List<CrBranchResponse> findCrBranchByBranch(Long branchId) {
        return findCrBranchByBranch.findCrBranchByBranch(branchId);
    }

    /**
     * Atribui um usuário responsável a um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do vínculo.
     * @param userId identificador do usuário.
     * @return vínculo já atualizado.
     */
    @Override
    public CrBranchResponse assignCrBranchResponsible(Long crBranchId, Long userId) {
        return assignCrBranchResponsible.assignCrBranchResponsible(crBranchId, List.of(userId));
    }

    /**
     * Remove um usuário responsável de um vínculo CR-filial no banco de dados.
     * @param crBranchId identificador do vínculo.
     * @return vínculo já atualizado.
     */
    @Override
    public CrBranchResponse removeCrBranchResponsible(Long crBranchId) {
        return removeCrBranchResponsible.removeCrBranchResponsible(crBranchId);
    }

}