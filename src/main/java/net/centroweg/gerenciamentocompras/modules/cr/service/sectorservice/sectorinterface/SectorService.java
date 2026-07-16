package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Interface de serviço para operações de gerenciamento de {@link Sector}.
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
     * @return lista com todos os blocos encontrados.
     */
    List<SectorSimpleResponse> findAllSectorSimple();

    /**
     * Lista todos os blocos cadastrados no banco de dados de forma completa.
     * @return lista com todos os blocos encontrados.
     */
    List<SectorCompoundResponse> findAllSectorCompound();

    /**
     * Busca um bloco no banco de dados pelo ID de forma simples.
     * @param id identificador do bloco.
     * @return bloco encontrado.
     */
    SectorSimpleResponse findSectorByIdSimple(Long id);

    /**
     * Busca um bloco no banco de dados pelo ID de forma completa.
     * @param id identificador do bloco.
     * @return bloco encontrado.
     */
    SectorCompoundResponse findSectorByIdCompound(Long id);

    /**
     * Atualiza um bloco existente no banco de dados.
     * @param id identificador do bloco.
     * @param sector novos dados do bloco.
     * @return bloco já atualizado.
     */
    SectorSimpleResponse updateSector(Long id, SectorRequest sector);

    /**
     * Remove um bloco do banco de dados.
     * @param id identificador do bloco.
     */
    void deleteSector(Long id);
}
