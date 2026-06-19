package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;

/**
 * Classe responsável pela conversão entre entidades e DTOs
 * de unidades de medida.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
@Component
public class MeasurementUnitMapper {

    /**
     * Converte um DTO de requisição em uma entidade
     * de unidade de medida.
     *
     * @param request Dados recebidos na requisição.
     * @return Entidade de unidade de medida.
     */
    public MeasurementUnit toEntity(MeasurementUnitRequest request) {

        return new MeasurementUnit(
            request.name(),
            request.abbreviation()
        );
    }

    /**
     * Converte uma entidade de unidade de medida em um DTO
     * de resposta.
     *
     * @param measurementUnit Entidade de unidade de medida.
     * @return DTO contendo os dados da unidade de medida.
     */
    public MeasurementUnitResponse toResponse(
            MeasurementUnit measurementUnit) {

        return new MeasurementUnitResponse(
            measurementUnit.getId(),
            measurementUnit.getName(),
            measurementUnit.getAbbreviation()
        );
    }
}