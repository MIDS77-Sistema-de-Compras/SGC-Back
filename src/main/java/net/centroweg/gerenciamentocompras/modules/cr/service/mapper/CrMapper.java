package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrSimpleResponse;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pela conversão entre a entidade({@link Cr}) e seus DTOs de entrada({@link CrRequest}) e saída({@link CrCompoundResponse}, caso for completa) ou ({@link CrSimpleResponse}, caso for simples).
 */
@Component
public class CrMapper {

    /**
     * Converte um DTO de entrada para uma entidade CR.
     * @param dto DTO com os dados de entrada do CR.
     * @return conversão dos dados para uma entidade CR.
     */
    public Cr toEntity(CrRequest dto, Sector sector){
        Cr crSave = new Cr();
        crSave.setName(dto.name());
        crSave.setCode(dto.code());
        crSave.setMaster(dto.master());
        crSave.setBloco(sector);
        return crSave;
    }

    /**
     * Converte uma entidade CR em um DTO de saída do CR.
     * @param cr entidade com os dados.
     * @return DTO de saída com os dados já convertidos de forma simples.
     */
    public CrSimpleResponse toCrSimpleResponse(Cr cr){
        return new CrSimpleResponse(
                cr.getName()
        );
    }

    /**
     * Converte uma entidade CR para um DTO de saída do CR.
     * @param cr entidade com os dados.
     * @return DTO de saída com os dados já convertidos de forma completa.
     */
    public CrCompoundResponse toCrCompoundResponse(Cr cr){
        return new CrCompoundResponse(
                cr.getId(),
                cr.getName(),
                cr.getCode(),
                cr.getMaster(),
                cr.getBloco() != null ? cr.getBloco().getName() : null
        );
    }
}
