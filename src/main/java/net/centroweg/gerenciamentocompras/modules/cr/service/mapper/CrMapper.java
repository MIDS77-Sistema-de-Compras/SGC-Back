package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
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

    public CrResponse toResponse(Cr cr){
        return new CrResponse(
                cr.getId(),
                cr.getName(),
                cr.getCode(),
                cr.isMaster()
        );
    }
}
