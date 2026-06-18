package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllSectorCompound {

    private final SectorRepository repository;
    private final SectorMapper sectorMapper;

    public List<SectorCompoundResponse> findAllCompound(){
        return sectorMapper.toResponseCompoundList(repository.findAll());
    }
}
