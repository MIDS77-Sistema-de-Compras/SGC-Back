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

/**
 * Componente responsável pela conversão entre a entidade({@link Sector}) e seus DTOs de entrada({@link SectorRequest}) e saída({@link SectorCompoundResponse}, caso for completa) ou ({@link SectorSimpleResponse}, caso for simples).
 */
@Component
@RequiredArgsConstructor
public class SectorMapper {

    private final CrMapper crMapper;

    /**
     * Converte um DTO de entrada do bloco em uma entidade bloco.
     * @param sector DTO com os dados de entrada do bloco.
     * @return conversão dos dados para uma entidade bloco.
     */
    public Sector toEntity(SectorRequest sector){
        return new Sector(sector.name());
    }

    /**
     * Converte uma entidade bloco em um DTO de saída do bloco.
     * @param sector entidade com os dados.
     * @return DTO de saída com os dados lá convertidos de forma simples.
     */
    public SectorSimpleResponse toResponseSimple(Sector sector){
        return new SectorSimpleResponse(
                sector.getId(),
                sector.getName());
    }

    /**
     * Converte uma entidade bloco em um DTO de saída do bloco.
     * @param sector entidade com os dados.
     * @return DTO de saída com os dados lá convertidos de forma completa.
     */
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

    /**
     * Converte uma lista de entidade bloco em uma lista de DTOs de saída do bloco.
     * @param sectors lista com as entidades.
     * @return lista de DTOs de saída com os blocos já convertidos de forma simples.
     */
    public List<SectorSimpleResponse> toResponseSimpleList(List<Sector> sectors){
        return sectors
                .stream()
                .map(this::toResponseSimple)
                .toList();
    }

    /**
     * Converte uma lista de entidade bloco em uma lista de DTOs de saída do bloco.
     * @param sectors lista com as entidades.
     * @return lista de DTOs de saída com os blocos já convertidos de forma completa.
     */
    public List<SectorCompoundResponse> toResponseCompoundList(List<Sector> sectors){
        return sectors
                .stream()
                .map(this::toResponseCompound)
                .toList();
    }
}
