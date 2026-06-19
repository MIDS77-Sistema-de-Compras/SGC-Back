package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

/**
 * Contrato de serviço para operações sobre Centros de Resultado (CR).
 */
public interface CrService {

    /**
     * Cria um novo Centro de Resultado.
     *
     * @param dto dados do CR a ser criado
     * @return {@link CrCompoundResponse} com os dados persistidos
     */
    CrCompoundResponse create(CrRequest dto, UserPrincipal userPrincipal);

    /**
     * Retorna todos os Centros de Resultado cadastrados.
     *
     * @return lista de {@link CrCompoundResponse}
     */
    List<CrCompoundResponse> listAll();

    /**
     * Busca um Centro de Resultado pelo identificador.
     *
     * @param id identificador do CR
     * @return {@link CrCompoundResponse} correspondente
     */
    CrCompoundResponse listById(Long id);

    /**
     * Atualiza os dados de um Centro de Resultado existente.
     *
     * @param id  identificador do CR
     * @param dto novos dados do CR
     * @return {@link CrCompoundResponse} com os dados atualizados
     */
    CrCompoundResponse update(Long id, CrRequest dto);

    /**
     * Remove um Centro de Resultado pelo identificador.
     *
     * @param id identificador do CR a ser removido
     * @return {@link MessageDTO} com mensagem de confirmação
     */
    MessageDTO delete(Long id);
}
