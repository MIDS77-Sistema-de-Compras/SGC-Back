package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindSectorByIdSimple {

    private final SectorRepository repository;
    private final SectorMapper sectorMapper;

    public SectorSimpleResponse findByIdSimple(Long id){
        return sectorMapper.toResponseSimple(repository.findById(id)
                .orElseThrow(() -> new SectorNotFoundException()));
    }
}
