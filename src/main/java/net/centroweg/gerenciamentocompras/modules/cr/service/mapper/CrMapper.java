package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
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
    public Cr toEntity(CrRequest dto){
        return new Cr(
                dto.name(),
                dto.code(),
                dto.master()
        );
    }

    /**
     * Converte uma entidade {@link Cr} para o DTO de resposta {@link CrResponse}.
     *
     * @param cr entidade a ser convertida
     * @return {@link CrResponse} com os dados do CR
     */
    public CrResponse toResponse(Cr cr){
        return new CrResponse(
                cr.getId(),
                cr.getName(),
                cr.getCode(),
                cr.isMaster()
        );
    }
}
