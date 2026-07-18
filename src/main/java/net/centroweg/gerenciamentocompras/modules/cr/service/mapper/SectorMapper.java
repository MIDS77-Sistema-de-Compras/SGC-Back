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
     * @return dados convertidos para entidade.
     */
    public Sector toEntity(SectorRequest sector){
        return new Sector(sector.name());
    }

    /**
     * Converte uma entidade bloco em um DTO de saída do bloco.
     * @param sector entidade com os dados do bloco.
     * @return dados convertidos de forma simples para DTO de saída.
     */
    public SectorSimpleResponse toResponseSimple(Sector sector){
        return new SectorSimpleResponse(
                sector.getId(),
                sector.getName());
    }

    /**
     * Converte uma entidade bloco em um DTO de saída do bloco.
     * @param sector entidade com os dados do bloco.
     * @return dados convertidos de forma completa para DTO de saída.
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
     * Converte uma lista de entidades bloco em uma lista de DTOs de saída do bloco.
     * @param sectors lista de entidades com os dados do bloco.
     * @return dados convertidos de forma simples para uma lista de DTOs de saída.
     */
    public List<SectorSimpleResponse> toResponseSimpleList(List<Sector> sectors){
        return sectors
                .stream()
                .map(this::toResponseSimple)
                .toList();
    }

    /**
     * Converte uma lista de entidade bloco em uma lista de DTOs de saída do bloco.
     * @param sectors lista de entidades com os dados do bloco.
     * @return dados convertidos de forma completa para uma lista de DTOs de saída.
     */
    public List<SectorCompoundResponse> toResponseCompoundList(List<Sector> sectors){
        return sectors
                .stream()
                .map(this::toResponseCompound)
                .toList();
    }
}
