package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface.CrService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * Classe de serviço da {@link Cr} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link CrService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@RequiredArgsConstructor
@Component
public class CrServiceImpl implements CrService {

    /**
     * Componente responsável pela criação de um CR.
     */
    private final CreateCr createCr;

    /**
     * Componente responsável pela listagem de todos os CRs cadastrados.
     */
    private final FindAllCr findAllCr;

    /**
     * Componente responsável pela busca de um CR pelo ID informado.
     */
    private final FindById findById;

    /**
     * Componente responsável por atualizar um CR.
     */
    private final UpdateCr updateCr;

    /**
     * Componente responsável por remover um CR.
     */
    private final DeleteCr deleteCr;

    /**
     * Cria e persiste um novo CR no banco de dados.
     * @param dto dados do CR.
     * @param userPrincipal dados do usuário que será atribuído.
     * @return CR criado.
     */
    @Override
    public CrCompoundResponse create(CrRequest dto, UserPrincipal userPrincipal) {
        return createCr.create(dto, userPrincipal);
    }

    /**
     * Lista todos os CRs cadastrados no banco de dados.
     * @return lista com todos os CRs encontrados, caso exista.
     */
    @Override
    public List<CrCompoundResponse> listAll(){
        return findAllCr.listAll();
    }

    /**
     * Busca um CR no banco de dados pelo ID informado.
     * @param id identificador do CR.
     * @return CR encontrado, caso exista.
     */
    @Override
    public CrCompoundResponse listById(Long id){
        return findById.listById(id);
    }

    /**
     * Atualiza um CR existente no banco de dados.
     * @param id identificador do CR.
     * @param dto novos dados do CR.
     * @return CR já atualizado.
     */
    @Override
    public CrCompoundResponse update(Long id, CrRequest dto){
        return updateCr.update(id, dto);
    }

    /**
     * Remove um CR do banco de dados.
     * @param id identificador do CR.
     * @return mensagem de confirmação da remoção.
     */
    @Override
    public MessageDTO delete(Long id){
        return deleteCr.delete(id);
    }
}
