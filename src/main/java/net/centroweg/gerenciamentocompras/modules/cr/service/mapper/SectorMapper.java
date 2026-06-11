package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SectorMapper {

    private final CrMapper crMapper;

    public Sector toEntity(SectorRequest sector){
        return new Sector(sector.name());
    }

    public SectorSimpleResponse toResponseSimple(Sector sector){
        return new SectorSimpleResponse(
                sector.getId(),
                sector.getName());
    }

    public SectorCompoundResponse toResponseCompound(Sector sector){
        List<CrSimpleResponse> crSimpleResponse = new ArrayList<>();
        if(sector.getCrs() != null){
             crSimpleResponse = sector.getCrs()
                    .stream()
                    .map(crMapper::toCrSimpleResponse)
                    .toList();
        }


        return new SectorCompoundResponse(
                sector.getId(),
                sector.getName(),
                sector.getCrs() != null? crSimpleResponse : List.of()
        );
    }

    public List<SectorSimpleResponse> toResponseSimpleList(List<Sector> sectors){
        return sectors
                .stream()
                .map(this::toResponseSimple)
                .toList();
    }

    public List<SectorCompoundResponse> toResponseCompoundList(List<Sector> sectors){
        return sectors
                .stream()
                .map(this::toResponseCompound)
                .toList();
    }
}
