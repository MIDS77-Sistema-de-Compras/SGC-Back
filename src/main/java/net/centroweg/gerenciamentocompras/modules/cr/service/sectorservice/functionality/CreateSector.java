package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela criação de um {@link Sector}.
 */
@Service
@RequiredArgsConstructor
public class CreateSector {

    private final SectorMapper sectorMapper;
    private final SectorRepository repository;

    /**
     * Cria e persiste um bloco no banco de dados.
     * @param sector dados do bloco.
     * @return bloco criado.
     */
    public SectorSimpleResponse createSector(SectorRequest sector){
        Sector sectorSave = sectorMapper.toEntity(sector);
        return sectorMapper.toResponseSimple(repository.save(sectorSave));
    }
}
