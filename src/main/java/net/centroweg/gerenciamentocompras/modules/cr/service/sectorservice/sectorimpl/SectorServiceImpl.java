package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality.*;
import net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorinterface.SectorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorServiceImpl implements SectorService {

    private final CreateSector create;
    private final FindAllSectorSimple findAllSimple;
    private final FindAllSectorCompound findAllCompound;
    private final FindSectorByIdSimple findByIdSimple;
    private final FindSectorByIdCompound findByIdCompound;
    private final UpdateSector update;
    private final DeleteSector delete;

    @Override
    public SectorSimpleResponse createSector(SectorRequest sector){
        return create.createSector(sector);
    }

    @Override
    public List<SectorSimpleResponse> findAllSectorSimple(){
        return findAllSimple.findAllSimple();
    }

    @Override
    public List<SectorCompoundResponse> findAllSectorCompound(){
        return findAllCompound.findAllCompound();
    }

    @Override
    public SectorSimpleResponse findSectorByIdSimple(Long id){
        return findByIdSimple.findByIdSimple(id);
    }

    @Override
    public SectorCompoundResponse findSectorByIdCompound(Long id){
        return findByIdCompound.findSectorByIdCompound(id);
    }

    @Override
    public SectorSimpleResponse updateSector(Long id, SectorRequest sector){
        return update.updateSector(id, sector);
    }

    @Override
    public void deleteSector(Long id){
        delete.deleteSector(id);
    }

}
