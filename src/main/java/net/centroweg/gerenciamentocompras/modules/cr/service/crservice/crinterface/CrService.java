package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * Interface de serviço para operações de gerenciamento de {@link Cr}.
 */
public interface CrService {

    /**
     * Cria e persiste um novo CR no banco de dados.
     * @param dto dados do CR.
     * @return CR criado.
     */
    CrCompoundResponse create(CrRequest dto, UserPrincipal userPrincipal);

    /**
     * Lista todos os CRs cadastrados no banco de dados.
     * @return lista de todos os CRs encontrados.
     */
    List<CrCompoundResponse> listAll();

    /**
     * Busca um CR no banco de dados pelo ID informado.
     * @param id identificador do CR.
     * @return CR encontrado.
     */
    CrCompoundResponse listById(Long id);

    /**
     * Atualiza um CR existente no banco de dados.
     * @param id  identificador do CR.
     * @param dto novos dados do CR.
     * @return CR já atualizado.
     */
    CrCompoundResponse update(Long id, CrRequest dto);

    /**
     * Remove um CR do banco de dados.
     * @param id identificador do CR.
     * @return mensagem de confirmação da remoção.
     */
    MessageDTO delete(Long id);
}
