package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrSimpleResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre a entidade {@link Cr} e seus DTOs.
 */
@Component
public class CrMapper {
    /**
     * Converte um {@link CrRequest} para a entidade {@link Cr}.
     *
     * @param dto DTO de entrada com os dados do CR
     * @return entidade {@link Cr} pronta para persistência
     */
    public Cr toEntity(CrRequest dto, Sector sector){
        Cr crSave = new Cr();
        crSave.setName(dto.name());
        crSave.setCode(dto.code());
        crSave.setMaster(dto.master());
        crSave.setSector(sector);
        return crSave;
    }

    public CrSimpleResponse toCrSimpleResponse(Cr cr){
        return new CrSimpleResponse(
                cr.getName()
        );
    }
    /**
     * Converte uma entidade {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Cr} para o DTO de resposta {@link CrResponse}.
     *
     * @param cr entidade a ser convertida
     * @return {@link CrCompoundResponse} com os dados do CR
     */

    public CrCompoundResponse toCrCompoundResponse(Cr cr){
        return new CrCompoundResponse(
                cr.getId(),
                cr.getName(),
                cr.getCode(),
                cr.getMaster(),
                cr.getSector() != null ? cr.getSector().getName() : null
        );
    }
}
