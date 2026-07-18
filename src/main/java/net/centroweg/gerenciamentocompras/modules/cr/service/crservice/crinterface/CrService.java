package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;

/**
 * Interface de serviço para operações de gerenciamento de {@link Cr}.
 */
public interface CrService {

    /**
     * Cria e persiste um novo CR no banco de dados.
     * @param dto dados do CR.
     * @return CR criado.
     * @throws AcessDeniedException caso o usuário não tenha permissão de coordenador.
     * @throws SectorNotFoundException caso o bloco informado não seja encontrado.
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
     * @throws CrNotFoundException se o CR não for encontrado.
     */
    CrCompoundResponse listById(Long id);

    /**
     * Atualiza um CR existente no banco de dados.
     * @param id  identificador do CR.
     * @param dto novos dados do CR.
     * @return CR já atualizado.
     * @throws CrNotFoundException se o CR não for encontrado.
     */
    CrCompoundResponse update(Long id, CrRequest dto);

    /**
     * Remove um CR do banco de dados.
     * @param id identificador do CR.
     * @return mensagem de confirmação da remoção.
     * @throws CrNotFoundException se o CR não for encontrado.
     */
    MessageDTO delete(Long id);
}
