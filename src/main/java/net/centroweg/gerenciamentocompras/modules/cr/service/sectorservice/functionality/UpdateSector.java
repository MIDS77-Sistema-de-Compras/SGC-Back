package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela atualização de um {@link Sector}.
 */
@Service
@RequiredArgsConstructor
public class UpdateSector {

    private final SectorMapper sectorMapper;
    private final SectorRepository repository;

    /**
     * Atualiza um bloco existente.
     * @param id identificador do bloco.
     * @param sector novos dados do bloco.
     * @return bloco já atualizado.
     */
    public SectorSimpleResponse updateSector(Long id, SectorRequest sector){
        Sector sectorSave = repository.findById(id)
                .orElseThrow(() -> new SectorNotFoundException());
        sectorSave.setName(sector.name());
        return sectorMapper.toResponseSimple(repository.save(sectorSave));
    }
}
