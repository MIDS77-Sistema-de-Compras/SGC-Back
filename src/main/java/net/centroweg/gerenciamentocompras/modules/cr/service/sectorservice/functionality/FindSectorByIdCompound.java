package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindSectorByIdCompound {

    private final SectorMapper sectorMapper;
    private final SectorRepository repository;

    public SectorCompoundResponse findSectorByIdCompound(Long id){
        return sectorMapper.toResponseCompound(repository.findById(id)
                .orElseThrow(() -> new SectorNotFoundException()));
    }
}
