package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
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
     * @return {@link CrResponse} com os dados persistidos
     */
    CrResponse create(CrRequest dto);

    /**
     * Retorna todos os Centros de Resultado cadastrados.
     *
     * @return lista de {@link CrResponse}
     */
    List<CrResponse> listAll();

    /**
     * Busca um Centro de Resultado pelo identificador.
     *
     * @param id identificador do CR
     * @return {@link CrResponse} correspondente
     */
    CrResponse listById(long id);

    /**
     * Atualiza os dados de um Centro de Resultado existente.
     *
     * @param id  identificador do CR
     * @param dto novos dados do CR
     * @return {@link CrResponse} com os dados atualizados
     */
    CrResponse update(long id, CrRequest dto);

    /**
     * Remove um Centro de Resultado pelo identificador.
     *
     * @param id identificador do CR a ser removido
     * @return {@link MessageDTO} com mensagem de confirmação
     */
    MessageDTO delete(long id);
}
