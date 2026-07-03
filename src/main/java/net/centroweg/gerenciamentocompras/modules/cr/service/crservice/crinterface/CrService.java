package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * Contrato de serviço para operações de gerenciamento de {@link Cr}.
 */
public interface CrService {

    /**
     * Cria um novo CR.
     * @param dto dados do CR.
     * @return o CR criado.
     */
    CrCompoundResponse create(CrRequest dto, UserPrincipal userPrincipal);

    /**
     * Lista todos os CRs cadastrados.
     * @return lista dos CRs encontrados.
     */
    List<CrCompoundResponse> listAll();

    /**
     * Busca um CR pelo ID.
     * @param id identificador do CR.
     * @return CR encontrado na busca.
     */
    CrCompoundResponse listById(Long id);

    /**
     * Atualiza os dados de um CR existente.
     * @param id  identificador do CR.
     * @param dto novos dados do CR.
     * @return CR com os dados atualizados.
     */
    CrCompoundResponse update(Long id, CrRequest dto);

    /**
     * Remove um CR pelo identificador.
     * @param id identificador do CR a ser removido.
     * @return mensagem de confirmação da remoção.
     */
    MessageDTO delete(Long id);
}
