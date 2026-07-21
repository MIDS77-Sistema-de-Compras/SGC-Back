package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;

/**
 * Interface de serviço para operações de gerenciamento do {@link Sector}.
 */
public interface SectorService {

    /**
     * Cria e persiste um novo bloco no banco de dados.
     * @param sector dados do bloco.
     * @return bloco criado.
     */
    SectorSimpleResponse createSector(SectorRequest sector);

    /**
     * Lista todos os blocos cadastrados no banco de dados de forma simples.
     * @return lista com todos os blocos encontrados, caso exista.
     */
    List<SectorSimpleResponse> findAllSectorSimple();

    /**
     * Lista todos os blocos cadastrados no banco de dados de forma completa.
     * @return lista com todos os blocos encontrados, caso exista.
     */
    List<SectorCompoundResponse> findAllSectorCompound();

    /**
     * Busca um bloco no banco de dados pelo ID informado de forma simples.
     * @param id identificador do bloco.
     * @return bloco encontrado, caso exista.
     * @throws SectorNotFoundException se o bloco não for encontrado com ID informado.
     */
    SectorSimpleResponse findSectorByIdSimple(Long id);

    /**
     * Busca um bloco no banco de dados pelo ID informado de forma completa.
     * @param id identificador do bloco.
     * @return bloco encontrado, caso exista.
     * @throws SectorNotFoundException se o bloco não for encontrado com o ID informado.
     */
    SectorCompoundResponse findSectorByIdCompound(Long id);

    /**
     * Atualiza um bloco existente no banco de dados.
     * @param id identificador do bloco.
     * @param sector novos dados do bloco.
     * @return bloco já atualizado.
     * @throws SectorNotFoundException se o bloco não for encontrado com o ID informado.
     */
    SectorSimpleResponse updateSector(Long id, SectorRequest sector);

    /**
     * Remove um bloco do banco de dados.
     * @param id identificador do bloco.
     */
    void deleteSector(Long id);
}
