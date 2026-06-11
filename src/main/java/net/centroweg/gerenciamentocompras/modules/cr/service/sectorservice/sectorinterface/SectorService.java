package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;

import java.util.List;

public interface SectorService {

    SectorSimpleResponse createSector(SectorRequest sector);
    List<SectorSimpleResponse> findAllSectorSimple();
    List<SectorCompoundResponse> findAllSectorCompound();
    SectorSimpleResponse findSectorByIdSimple(Long id);
    SectorCompoundResponse findSectorByIdCompound(Long id);
    SectorSimpleResponse updateSector(Long id, SectorRequest sector);
    void deleteSector(Long id);
}
