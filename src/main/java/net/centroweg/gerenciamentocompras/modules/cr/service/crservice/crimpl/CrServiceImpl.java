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
     * Componente responsável pela listagem dos CRs.
     */
    private final FindAllCr findAllCr;

    /**
     * Componente responsável pela busca de um CR pelo ID.
     */
    private final FindById findById;

    /**
     * Componente responsável pela atualização de um CR.
     */
    private final UpdateCr updateCr;

    /**
     * Componente responsável por deletar um CR.
     */
    private final DeleteCr deleteCr;

    /**
     * Cria um CR.
     * @param dto dados do CR a ser criado.
     * @param userPrincipal dados do usuário que vai ser atribuído a CR.
     * @return CR criada.
     */
    @Override
    public CrCompoundResponse create(CrRequest dto, UserPrincipal userPrincipal) {
        return createCr.create(dto, userPrincipal);
    }

    /**
     * Lista todos os CRs cadastrados.
     * @return lista com todos os CRs cadastrados.
     */
    @Override
    public List<CrCompoundResponse> listAll(){
        return findAllCr.listAll();
    }

    /**
     * Busca um CR pelo ID.
     * @param id identificador do CR.
     * @return CR encontrado.
     */
    @Override
    public CrCompoundResponse listById(Long id){
        return findById.listById(id);
    }

    /**
     * Atualiza um CR existente.
     * @param id  identificador do CR.
     * @param dto novos dados do CR.
     * @return CR atualizado.
     */
    @Override
    public CrCompoundResponse update(Long id, CrRequest dto){
        return updateCr.update(id, dto);
    }

    /**
     * Deleta um CR.
     * @param id identificador do CR a ser removido.
     * @return mensagem de confirmação da remoção.
     */
    @Override
    public MessageDTO delete(Long id){
        return deleteCr.delete(id);
    }
}
