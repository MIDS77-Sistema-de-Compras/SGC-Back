package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrSimpleResponse;
import org.springframework.stereotype.Component;

@Component
public class CrMapper {
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

    public CrCompoundResponse toCrCompoundResponse(Cr cr){
        return new CrCompoundResponse(
                cr.getId(),
                cr.getName(),
                cr.getCode(),
                cr.isMaster(),
                cr.getSector() != null ? cr.getSector().getName() : null
        );
    }
}
