package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrSimpleResponse;
import org.springframework.stereotype.Component;

@Component
public class CrMapper {
    public Cr toEntity(CrRequest dto){
        return new Cr(
                dto.name(),
                dto.code(),
                dto.master()
        );
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
                cr.isMaster()
        );
    }
}
