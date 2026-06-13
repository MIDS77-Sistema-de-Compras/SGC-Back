package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCr{

    private final CrRepository crRepository;
    private final SectorRepository sectorRepository;
    private final CrMapper crMapper;

    public CrCompoundResponse create(CrRequest dto){
        Sector sector = sectorRepository.findByName(dto.sector())
                .orElseThrow(() -> new SectorNotFoundException());
        return crMapper.toCrCompoundResponse(crRepository.save(crMapper.toEntity(dto, sector)));
    }
}
